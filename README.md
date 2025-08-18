# AI Agent App — 智能体应用平台

AI Agent App 是一个基于 DDD 分层架构设计的智能体应用平台，提供智能体管理、客户端/模型/工具配置、RAG 知识库检索与流式对话等能力，支持以统一分页与规范化接口风格对外提供管理与对话服务。

## 架构与模块

项目采用 DDD 分层与多模块结构：
- ai-agent-app-app：应用启动模块（Spring Boot 入口）
- ai-agent-app-trigger：接口适配层（Admin 管理端与 Agent 对话端 HTTP 接口）
- ai-agent-app-domain：领域层（智能体编排、对话、RAG 检索等领域服务）
- ai-agent-app-infrastructure：基础设施层（MyBatis-Plus、DAO、数据持久化）
- ai-agent-app-types：通用类型与枚举
- ai-agent-app-api：公共 API/DTO 定义（如有）

## 核心能力

- 智能体管理：
  - 分页查询、增删改查，按 ID/状态/创建时间范围等条件过滤
  - 统一分页响应：PageResponse + BaseQueryRequest
- 客户端/模型/工具配置：
  - 支持多客户端 API、模型、工具的配置与查询
  - 基于 MyBatis-Plus 的分页与条件查询
- RAG 检索（可选）：
  - 基于 pgvector 的向量检索，按知识标签进行相似度搜索，增强回答准确性
- 对话与流式输出：
  - 基于 Spring AI 的 ChatClient/ChatModel 能力，支持 Flux<ChatResponse> 的流式对话
  - 提供 AutoAgent（自动化执行）与普通对话接口
- 统一接口风格：
  - ResponseEntity 包装返回值，异常与日志规范化
  - Swagger/OpenAPI 注解完善接口文档

## 技术栈

- 语言与框架：Java 17、Spring Boot 3.x、Spring Web
- LLM 能力：Spring AI（ChatClient/ChatModel/Prompt 等）
- 响应式：Project Reactor（Flux/Mono）
- 持久化：MyBatis-Plus、MySQL 8.x
- 向量检索：PostgreSQL + pgvector（可选）
- 文档与工具：Swagger/OpenAPI、Lombok、Docker & docker-compose

## 统一分页约定

- 入参：BaseQueryRequest
  - pageNum、pageSize、id、status、createTimeStart、createTimeEnd 等
- 出参：PageResponse<T>
  - pageNum、pageSize、total、pages、list、hasNextPage、hasPreviousPage
- 控制器示例（管理端）：
  - /api/v1/ai/admin/agent/queryAiAgentList
  - /api/v1/ai/admin/client/api/queryClientApiList

## 目录速览

- /ai-agent-app-app/src/main/java/com/mingliu/Application.java：应用启动类
- /ai-agent-app-trigger/src/main/java/com/mingliu/trigger/http/admin：管理端 REST 接口
- /ai-agent-app-trigger/src/main/java/com/mingliu/trigger/http/agent：对话/流式执行接口
- /ai-agent-app-domain/src/main/java/com/mingliu/domain：领域服务（对话、RAG、编排）
- /ai-agent-app-infrastructure/src/main/java/com/mingliu/infrastructure：DAO、PO、数据访问
- /docs/dev-ops：容器编排、数据库初始化脚本（mysql/pgvector 等）

## 快速开始

1. 环境要求
   - JDK 17+
   - Maven 3.9+
   - MySQL 8.x
   - （可选）PostgreSQL + pgvector，用于 RAG 检索

2. 初始化数据库
   - 参考 docs/dev-ops/mysql 下的 SQL 脚本初始化数据表
   - 如启用 RAG，请部署 PostgreSQL 并安装 pgvector，导入向量数据

3. 构建与运行
   - 构建：mvn clean package -DskipTests
   - 运行：启动模块 ai-agent-app-app，运行 Application.main

4. 接口文档
   - 启动后访问 Swagger UI：/swagger-ui/index.html

## 典型能力举例

- 流式对话：基于 Spring AI 的 ChatModel.stream，返回 Flux<ChatResponse>
- RAG 增强：根据 ragId 查询知识标签，检索相似文档后拼接提示词
- 多客户端聚合：按智能体绑定的多个客户端依次调用，聚合输出

## 贡献与反馈

欢迎提交 Issue 或 PR，共同完善智能体应用平台能力。

