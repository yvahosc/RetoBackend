package com.example.demo.services;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.models.TestModel;
import com.example.demo.repositories.TestRepository;

@Service
public class TestService {
	@Autowired
	TestRepository testRepository;

	public void setTestRepository(TestRepository testRepository) {
		this.testRepository = testRepository;
	}

	public ArrayList<TestModel> getTests(){
		return (ArrayList<TestModel>) testRepository.findAll();
	}
	
	public TestModel saveTest(TestModel test) {
		testRepository.save(test);
		return test;
	}
	
	public Optional<TestModel> getByIdTest(long idTest){
		return testRepository.findById(idTest);
	}
	
	public void deleteTest(Long idTest) {
		testRepository.deleteById(idTest);
	}
}
