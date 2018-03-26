package com.example.recordsapi.controllers;

import com.example.recordsapi.models.Record;
import com.example.recordsapi.repositories.RecordRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;



import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import static org.mockito.BDDMockito.given;
import static org.hamcrest.Matchers.is;

@RunWith(SpringRunner.class)
@WebMvcTest(RecordsController.class)
public class RecordsControllerTest {
    @Autowired
    private MockMvc mockMvc;
    private Record newRecord;
    private Record updatedSecondRecord;

    @Autowired
    private ObjectMapper jsonObjectMapper;


    @MockBean
    private RecordRepository mockRecordRepository;

    @Before
    public void setUp() {
        Record firstRecord = new Record(
                "google.com",
                "public hearing"
        );

        Record secondRecord = new Record(
                "netflix",
                "court notices"
        );
        newRecord = new Record(
                "new_link",
                "new category"
        );

        updatedSecondRecord = new Record(
                "updated link",
                "updated category"
        );
        given(mockRecordRepository.save(updatedSecondRecord)).willReturn(updatedSecondRecord);

        given(mockRecordRepository.save(newRecord)).willReturn(newRecord);

        Iterable<Record> mockRecords =
                Stream.of(firstRecord, secondRecord).collect(Collectors.toList());

        given(mockRecordRepository.findAll()).willReturn(mockRecords);

        given(mockRecordRepository.findOne(1L)).willReturn(firstRecord);

        given(mockRecordRepository.findOne(4L)).willReturn(null);

        // Mock out Delete to return EmptyResultDataAccessException for missing user with ID of 4
        doAnswer(invocation -> {
            throw new EmptyResultDataAccessException("ERROR MESSAGE FROM MOCK!!!", 1234);
        }).when(mockRecordRepository).delete(4L);
    }

    @Test
    public void findAllRecords_success_returnsStatusOK() throws Exception {

        this.mockMvc
                .perform(get("/"))
                .andExpect(status().isOk());
    }
    @Test
    public void findAllRecords_success_returnAllRecordsAsJSON() throws Exception {

        this.mockMvc
                .perform(get("/"))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void findAllRecords_success_returnUrlLinkForEachRecord() throws Exception {

        this.mockMvc
                .perform(get("/"))
                .andExpect(jsonPath("$[0].urlLink", is("google.com")));
    }

    @Test
    public void findAllRecords_success_returnCategoryForEachRecord() throws Exception {

        this.mockMvc
                .perform(get("/"))
                .andExpect(jsonPath("$[0].category", is("public hearing")));
    }
    //happy path tests
    @Test
    public void findRecordById_success_returnsStatusOK() throws Exception {

        this.mockMvc
                .perform(get("/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void findRecordById_success_returnUrlLink() throws Exception {

        this.mockMvc
                .perform(get("/1"))
                .andExpect(jsonPath("$.urlLink", is("google.com")));
    }

    @Test
    public void findRecordById_success_returnCategory() throws Exception {

        this.mockMvc
                .perform(get("/1"))
                .andExpect(jsonPath("$.category", is("public hearing")));
    }

    //unhappy path tests


    //make a MockMVC request for that bad ID, and expect that it gives us a "not found" status (404).

    @Test
    public void findRecordById_failure_recordNotFoundReturns404() throws Exception {

        this.mockMvc
                .perform(get("/4"))
                .andExpect(status().isNotFound());
    }

    ///clearer error message

    @Test
    public void findRecordById_failure_recordNotFoundReturnsNotFoundErrorMessage() throws Exception {

        this.mockMvc
                .perform(get("/4"))
                .andExpect(status().reason(containsString("Record with ID of 4 was not found!")));
    }

    //delete tests
    @Test
    public void deleteRecordById_success_returnsStatusOk() throws Exception {

        this.mockMvc
                .perform(delete("/1"))
                .andExpect(status().isOk());
    }


    //confirm method is actually deleting a record
    @Test
    public void deleteUserById_success_deletesViaRepository() throws Exception {

        this.mockMvc.perform(delete("/1"));

        verify(mockRecordRepository, times(1)).delete(1L);
    }

    //unhappy path delete. when deleting a record thtat doesnt exist, throw this error-- EmptyResultDataAccessException.

    @Test
    public void deleteRecordById_failure_recordNotFoundReturns404() throws Exception {

        this.mockMvc
                .perform(delete("/4"))
                .andExpect(status().isNotFound());
    }

    //create new users, testing the post route

    @Test
    public void createRecord_success_returnsStatusOk() throws Exception {

        this.mockMvc
                .perform(
                        post("/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonObjectMapper.writeValueAsString(newRecord))
                )
                .andExpect(status().isOk());
    }

    @Test
    public void createRecord_success_returnsUrlLink() throws Exception {

        this.mockMvc
                .perform(
                        post("/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonObjectMapper.writeValueAsString(newRecord))
                )
                .andExpect(jsonPath("$.urlLink", is("new_link")));
    }

    @Test
    public void createRecord_success_returnsCategory() throws Exception {

        this.mockMvc
                .perform(
                        post("/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonObjectMapper.writeValueAsString(newRecord))
                )
                .andExpect(jsonPath("$.category", is("new category")));
    }

    //test patch route
    @Test
    public void updateRecordById_success_returnsStatusOk() throws Exception {

        this.mockMvc
                .perform(
                        patch("/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonObjectMapper.writeValueAsString(updatedSecondRecord))
                )
                .andExpect(status().isOk());
    }

    @Test
    public void updateRecordById_success_returnsUpdatedUrlLink() throws Exception {

        this.mockMvc
                .perform(
                        patch("/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonObjectMapper.writeValueAsString(updatedSecondRecord))
                )
                .andExpect(jsonPath("$.urlLink", is("updated link")));
    }

    @Test
    public void updateRecordById_success_returnsUpdatedCategory() throws Exception {

        this.mockMvc
                .perform(
                        patch("/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonObjectMapper.writeValueAsString(updatedSecondRecord))
                )
                .andExpect(jsonPath("$.category", is("updated category")));
    }
    //unhappy path for patch route
    @Test
    public void updateRecordById_failure_userNotFoundReturns404() throws Exception {

        this.mockMvc
                .perform(
                        patch("/4")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonObjectMapper.writeValueAsString(updatedSecondRecord))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateRecordById_failure_userNotFoundReturnsNotFoundErrorMessage() throws Exception {

        this.mockMvc
                .perform(
                        patch("/4")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonObjectMapper.writeValueAsString(updatedSecondRecord))
                )
                .andExpect(status().reason(containsString("Record with ID of 4 was not found!")));
    }


}