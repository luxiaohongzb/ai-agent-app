package com.mingliu.test;

import com.mingliu.infrastructure.dao.IAiClientToolMcpDao;
import com.mingliu.infrastructure.dao.po.AiClientToolMcp;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Title: AiClientToolMcpDaoTest
 * @Author mingliu0608
 * @Package com.mingliu.test
 * @Date 2025/8/11 17:35
 * @description:
 */

@Slf4j
@SpringBootTest
public class AiClientToolMcpDaoTest {
    @Resource
    private IAiClientToolMcpDao aiClientToolMcpDao;

    @Test
    public void test_insert() {

        AiClientToolMcp aiClientToolMcp = AiClientToolMcp.builder().
                 id(10L)
                .mcpName("测试MCP工具")
                .transportType("sse")
                .transportConfig("{\"baseUri\":\"http://localhost:8080\",\"sseEndpoint\":\"/sse\"}")
                .requestTimeout(180)
                .status(1)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now()).
                build();
            aiClientToolMcpDao.insert(aiClientToolMcp);
    }

}
