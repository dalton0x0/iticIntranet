package com.itic.intranet.repositories;

import com.itic.intranet.models.mongo.Log;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LogRepository extends MongoRepository<Log, String> {
    List<Log> findByActor(String actor);
}
