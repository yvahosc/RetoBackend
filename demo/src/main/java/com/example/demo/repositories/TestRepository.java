package com.example.demo.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.models.TestModel;


@Repository
public interface TestRepository extends CrudRepository<TestModel, Long>{

}
