package com.alibaba.smart.framework.engine.persister.mongo.repository;

import com.alibaba.smart.framework.engine.persister.mongo.User;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

}