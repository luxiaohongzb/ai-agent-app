package com.mingliu.domain.agent.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * RAG 知识库服务接口
 * @author Fuzhengwei bugstack.cn @小傅哥
 * 2025-05-05 16:40
 */
public interface IAiAgentRagService {

    void storeRagFile(String name, String tag, List<MultipartFile> files);
    void analyzeGitRepository (String repoUrl,  String userName, String token,String branch) throws Exception;
}
