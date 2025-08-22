package com.mingliu.domain.agent.service.rag;

import com.mingliu.domain.agent.adapter.repository.IAgentRepository;
import com.mingliu.domain.agent.model.valobj.AiRagOrderVO;
import com.mingliu.domain.agent.service.IAiAgentRagService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.boot.autoconfigure.info.ProjectInfoProperties;
import org.springframework.core.io.PathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

/**
 * RAG 知识库服务
 *
 * @author Fuzhengwei bugstack.cn @小傅哥
 * 2025-05-05 16:41
 */
@Slf4j
@Service
public class AiAgentRagService implements IAiAgentRagService {

    @Resource
    private TokenTextSplitter tokenTextSplitter;

    @Resource
    private PgVectorStore vectorStore;

    @Resource
    private IAgentRepository repository;

    @Override
    public void storeRagFile(String name, String tag, List<MultipartFile> files) {
        for (MultipartFile file : files) {
            TikaDocumentReader documentReader = new TikaDocumentReader(file.getResource());
            List<Document> documentList = tokenTextSplitter.apply(documentReader.get());

            // 添加知识库标签
            documentList.forEach(doc -> doc.getMetadata().put("knowledge", tag));

            // 存储知识库文件
            vectorStore.accept(documentList);

            // 存储到数据库
            AiRagOrderVO aiRagOrderVO = new AiRagOrderVO();
            aiRagOrderVO.setRagName(name);
            aiRagOrderVO.setKnowledgeTag(tag);

            repository.createTagOrder(aiRagOrderVO);
        }
    }

    @Override
    public void analyzeGitRepository(String repoUrl, String userName, String token,String branch) throws Exception {
        String localPath = "./git-cloned-repo";
        String repoProjectName = extractProjectName(repoUrl);
        log.info("克隆路径：{}", new File(localPath).getAbsolutePath());

        FileUtils.deleteDirectory(new File(localPath));

        Git git = Git.cloneRepository()
                .setURI(repoUrl)
                .setDirectory(new File(localPath))
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(userName, token))
              //  .setBranch(branch)
                .call();
        // 使用Files.walkFileTree遍历目录
        Files.walkFileTree(Paths.get(localPath), new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                // 跳过 .git 目录，避免不必要的遍历和可能的文件句柄占用
                if (".git".equals(dir.getFileName().toString())) {
                    return FileVisitResult.SKIP_SUBTREE;
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                log.info("{} 遍历解析路径，上传知识库:{}", repoProjectName, file.getFileName());
                try {
                    TikaDocumentReader reader = new TikaDocumentReader(new PathResource(file));
                    List<Document> documents = reader.get();
                    List<Document> documentSplitterList = tokenTextSplitter.apply(documents);

                    documents.forEach(doc -> doc.getMetadata().put("knowledge", repoProjectName));

                    documentSplitterList.forEach(doc -> doc.getMetadata().put("knowledge", repoProjectName));

                    vectorStore.accept(documentSplitterList);
                } catch (Exception e) {
                    log.error("遍历解析路径，上传知识库失败:{}", file.getFileName());
                }

                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                log.info("Failed to access file: {} - {}", file.toString(), exc.getMessage());
                return FileVisitResult.CONTINUE;
            }
        });

        // 添加知识库记录
        // 存储到数据库
        AiRagOrderVO aiRagOrderVO = new AiRagOrderVO();
        aiRagOrderVO.setRagName(repoProjectName);
        aiRagOrderVO.setKnowledgeTag(repoProjectName);

        repository.createTagOrder(aiRagOrderVO);
        git.close();

        // 关闭 Git 句柄后再删除目录，避免 Windows 文件锁导致的删除失败
        File repoDir = new File(localPath);
        try {
            // 遍历设置文件为可写
            Files.walkFileTree(repoDir.toPath(), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    File f = file.toFile();
                    if (!f.canWrite()) {
                        f.setWritable(true);
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    File f = dir.toFile();
                    if (!f.canWrite()) {
                        f.setWritable(true);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });

            // 尝试删除目录
            FileUtils.deleteDirectory(repoDir);
        } catch (IOException e) {
            log.warn("删除克隆目录失败，准备重试。path={}, err={}", repoDir.getAbsolutePath(), e.getMessage());
            try {
                Thread.sleep(200);
                // 再次尝试设置文件属性
                Files.walkFileTree(repoDir.toPath(), new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        file.toFile().setWritable(true);
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                        dir.toFile().setWritable(true);
                        return FileVisitResult.CONTINUE;
                    }
                });
                FileUtils.deleteDirectory(repoDir);
            } catch (Exception ex) {
                String msg = String.format("第二次删除克隆目录失败，请手工清理。path=%s, error=%s", 
                    repoDir.getAbsolutePath(), ex.getMessage());
                log.error(msg, ex);
                throw new RuntimeException(msg, ex);
            }
        }

        log.info("遍历解析路径，上传完成:{}", repoUrl);
    }

    private String extractProjectName(String repoUrl) {
        String[] parts = repoUrl.split("/");
        String projectNameWithGit = parts[parts.length - 1];
        return projectNameWithGit.replace(".git", "");
    }

}
