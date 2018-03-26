package com.example.recordsapi.repositories;

import com.example.recordsapi.models.Record;
import org.springframework.data.repository.CrudRepository;

public interface RecordRepository extends CrudRepository<Record, Long> {

}
