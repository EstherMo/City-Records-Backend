package com.example.recordsapi.repositories;

import com.example.recordsapi.models.Record;
import com.google.common.collect.Iterables;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RecordRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private RecordRepository recordRepository;
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

        entityManager.persist(firstRecord);
        entityManager.persist(secondRecord);
        entityManager.flush();
    }

    @Test
    public void findAll_returnsAllRecords() {
        Iterable<Record> recordFromDb = recordRepository.findAll();

        assertThat(Iterables.size(recordFromDb), is(2));
    }

    @Test
    public void findAll_returnsUrlLink() {
        Iterable<Record> recordsFromDb = recordRepository.findAll();

        String secondRecordsUrlLink = Iterables.get(recordsFromDb, 1).getUrlLink();

        assertThat(secondRecordsUrlLink, is("netflix"));
    }

    @Test
    public void findAll_returnsCategory() {
        Iterable<Record> recordsFromDb = recordRepository.findAll();

        String secondRecordsCategory = Iterables.get(recordsFromDb, 1).getCategory();

        assertThat(secondRecordsCategory, is("court notices"));
    }


}


