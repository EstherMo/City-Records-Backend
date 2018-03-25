package com.example.microservicesandtesting.repostitories;

import com.example.microservicesandtesting.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

}
