package com.alibaba.smart.framework.engine.persister.mongo.repository;

import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

}