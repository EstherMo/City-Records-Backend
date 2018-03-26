package com.example.microservicesandtesting.repostitories;


import com.example.microservicesandtesting.models.Record;
import org.springframework.data.repository.CrudRepository;

public interface RecordRepository extends CrudRepository<Record, Long> {

}
