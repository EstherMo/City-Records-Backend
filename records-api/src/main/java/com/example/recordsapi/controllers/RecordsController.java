package com.example.recordsapi.controllers;


import com.example.recordsapi.models.Record;
import com.example.recordsapi.repositories.RecordRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RestController
public class RecordsController {

    @Autowired
    private RecordRepository recordRepository;

    @GetMapping("/")
    public Iterable<Record> findAllRecords() {
        return recordRepository.findAll();
    }

    //throw error if user not found

    @GetMapping("/{recordId}")
    public Record findRecordById(@PathVariable Long recordId) throws NotFoundException {

        Record foundRecord = recordRepository.findOne(recordId);

        if (foundRecord == null) {
            throw new NotFoundException("Record with ID of " + recordId + " was not found!");
        }

        return foundRecord;
    }

    //delete
    @DeleteMapping("/{recordId}")
    public HttpStatus deleteRecordById(@PathVariable Long recordId) throws EmptyResultDataAccessException {

        recordRepository.delete(recordId);
        return HttpStatus.OK;
    }

    //post route
    @PostMapping("/")
    public Record createNewRecord(@RequestBody Record newRecord) {
        return recordRepository.save(newRecord);
    }

    //update records-- patch route
    @PatchMapping("/{recordId}")
    public Record updateRecordById(@PathVariable Long recordId, @RequestBody Record userRequest) throws NotFoundException {
        Record recordFromDb = recordRepository.findOne(recordId);

        if (recordFromDb == null) {
            throw new NotFoundException("Record with ID of " + recordId + " was not found!");
        }

        recordFromDb.setUrlLink(userRequest.getUrlLink());
        recordFromDb.setCategory(userRequest.getCategory());

        return recordRepository.save(recordFromDb);
    }



    // EXCEPTION HANDLERS

    @ExceptionHandler
    void handleRecordNotFound(
            NotFoundException exception,
            HttpServletResponse response) throws IOException {

        response.sendError(HttpStatus.NOT_FOUND.value(), exception.getMessage());;
    }
    @ExceptionHandler
    void handleDeleteNotFoundException(
            EmptyResultDataAccessException exception,
            HttpServletResponse response) throws IOException {

        response.sendError(HttpStatus.NOT_FOUND.value());
    }

}