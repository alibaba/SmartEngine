package com.alibaba.smart.framework.engine.persister.mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.alibaba.smart.framework.engine.persister.mongo")
public class SimpleMongoConfig {
  
    @Bean
    public MongoClient mongo() {
        return MongoClients.create("localhost");
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        MongoClient mongo = mongo();
        return new MongoTemplate(mongo, "test");
    }
}