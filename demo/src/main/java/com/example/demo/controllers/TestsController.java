package com.example.demo.controllers;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.TestModel;
import com.example.demo.models.TestModelInput;
import com.example.demo.services.TestService;

@RestController
@RequestMapping("/api/controller/test")
public class TestsController {
	
	@Autowired
	TestService testService;
	
	@GetMapping()
	public ResponseEntity<ArrayList<TestModel>> getTest(){
		ArrayList<TestModel> testsList = testService.getTests();
		if (testsList.isEmpty() || testsList == null) {
			return new ResponseEntity<ArrayList<TestModel>>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<ArrayList<TestModel>>(testsList, HttpStatus.OK);
		}
	}
	
	@GetMapping( path = "/{idTest}")
	public ResponseEntity<?> getTestByIdTest(@PathVariable("idTest") String idTestString){
		Long idTest;
		try {
			if (idTestString.isBlank()) {
				throw new Exception("El idTest ingresado no puede ser vacío.");
			}else {
				try {
					idTest = Long.parseLong(idTestString);
				}catch (Exception err){
					throw new Exception("El idTest ingresado no es del tipo requerido.");
				}
				Optional<TestModel> test = this.testService.getByIdTest(idTest);
				if (test.isPresent()) {
					return new ResponseEntity<Optional<TestModel>>(test, HttpStatus.OK);
				}else {
					throw new Exception("El test que se intenta obtener no existe.");
				}
			}
		} catch(Exception err) {
			return new ResponseEntity<String>(err.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping()
	public ResponseEntity<String> saveTest(@RequestBody TestModel test) { 	
		try {
			if (test.getName() == null || test.getDescription() == null) {
				throw new Exception("Los campos correspondientes al nombre y la descripción no pueden ser nulos.");
			}else if (test.getName().isBlank() || test.getDescription().isBlank()) {
				throw new Exception("Los campos correspondientes al nombre y la descripción no pueden estar vacíos.");
			}else {
				testService.saveTest(test);
				return new ResponseEntity<String>("Test con id " + test.getIdTest() + " creado exitosamente: " + test.toString(), HttpStatus.CREATED);
			}
		}catch(Exception err) {
			return new ResponseEntity<String>(err.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
	
	@PutMapping()
	public ResponseEntity<String> updateTest(@RequestBody TestModelInput testInput) {
		TestModel test = new TestModel();
		test.setName(testInput.getName());
		test.setDescription(testInput.getDescription());
		try {
			if (test.getName() == null || test.getDescription() == null) {
				throw new Exception("Los campos correspondientes al nombre y/o la descripción no pueden ser nulos.");
			}else if (test.getName().isBlank() || test.getDescription().isBlank()) {
				throw new Exception("Los campos correspondientes al nombre y/o la descripción no pueden estar vacíos.");
			}else {
				try {
					test.setIdTest(Long.parseLong(testInput.getIdTest()));
					
				}catch(Exception err) {
					throw new Exception("El idTest ingresado no es del tipo requerido.");
				}
				Optional<TestModel> existsTest = testService.getByIdTest(test.getIdTest());
				if (existsTest.isPresent()) {
					testService.saveTest(test);
					return new ResponseEntity<String>("Test con id " + test.getIdTest() + " actualizado exitosamente: " + test.toString(), HttpStatus.CREATED);
				}else{
					throw new Exception("El test que se intenta actualizar no existe.");
				}
			}
		}catch(Exception err) {
			return new ResponseEntity<String>(err.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping( path = "/{idTest}")
	public ResponseEntity<String> deleteTestByIdTest(@PathVariable("idTest") String idTestString){
		Long idTest;
		try {
			if (idTestString.isBlank()) {
				throw new Exception("El idTest no puede ser vacío.");
			} else {
				try {
					idTest = Long.parseLong(idTestString);
				}catch(Exception err) {
					throw new Exception("El idTest ingresado no es del tipo requerido.");
				}
				Optional<TestModel> existsTest = this.testService.getByIdTest(idTest);
				if (existsTest.isPresent()) {
					testService.deleteTest(idTest);
					return new ResponseEntity<String>("Test con id " + idTest + " eliminado exitosamente: " + existsTest.toString(), HttpStatus.OK);
				}else{
					throw new Exception("El test que se intenta eliminar no existe.");
				}
			}
			
		}catch(Exception err) {
			return new ResponseEntity<String>(err.getMessage(), HttpStatus.NO_CONTENT);//El status podría ser bad request.
		}
	}
}
