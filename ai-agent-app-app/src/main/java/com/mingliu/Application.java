package com.mingliu;

import com.mingliu.domain.agent.service.IAiAgentPreheatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;

import java.util.Arrays;

@SpringBootApplication
@Configurable
@Slf4j
public class Application implements CommandLineRunner {

    @Autowired
    private IAiAgentPreheatService aiAgentArmoryService;
    public static void main(String[] args){
        SpringApplication.run(Application.class);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("预热AiAgent服务，开始");
        aiAgentArmoryService.preheat(Arrays.asList("3001","3101","3102","3103","3104","3004"));
        log.info("预热AiAgent服务，完成");
    }
}
