package com.example.microservicesandtesting.features;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.microservicesandtesting.models.Record;
import com.example.microservicesandtesting.repostitories.RecordRepository;

import java.util.stream.Stream;


import static io.restassured.http.ContentType.JSON;
import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.Is.is;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RecordsApiFeatureTest {
    @Autowired
    private RecordRepository recordRepository;

    @Before
    public void setUp() {
        recordRepository.deleteAll();
    }

    @After
    public void tearDown() {
        recordRepository.deleteAll();
    }

    @Test
    public void shouldAllowFullCrudForAUser() throws Exception {


        //add test records
        Record firstRecord = new Record(
                "google.com",
                "public hearings"
        );

        Record secondRecord= new Record(
                "netflix.com",
                "court notices"
        );

        Stream.of(firstRecord, secondRecord)
                .forEach(record -> {
                    recordRepository.save(record);
                });

        when()
                .get("http://localhost:8080/records")
                .then()
                .statusCode(is(200))
                .and().body(containsString("google"))
                .and().body(containsString("netflix"));


        // Test creating a Record
        Record recordNotYetInDb = new Record(
                "new_record",
                "Not yet Created"
        );

        given()
                .contentType(JSON)
                .and().body(recordNotYetInDb)
                .when()
                .post("http://localhost:8080/records")
                .then()
                .statusCode(is(200))
                .and().body(containsString("new_record"));

// Test get all records
        when()
                .get("http://localhost:8080/records")
                .then()
                .statusCode(is(200))
                .and().body(containsString("google"))
                .and().body(containsString("netflix"))
                .and().body(containsString("Yet Created"));

// Test finding one record by ID
        when()
                .get("http://localhost:8080/records/" + secondRecord.getId())
                .then()
                .statusCode(is(200))
                .and().body(containsString("netflix"))
                .and().body(containsString("court"));

// Test updating a record
        secondRecord.setUrlLink("changed_url");

        given()
                .contentType(JSON)
                .and().body(secondRecord)
                .when()
                .patch("http://localhost:8080/records/" + secondRecord.getId())
                .then()
                .statusCode(is(200))
                .and().body(containsString("changed_url"));

// Test deleting a record
        when()
                .delete("http://localhost:8080/records/" + secondRecord.getId())
                .then()
                .statusCode(is(200));
    }
}
