package com.example.demo.controllers;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.demo.models.TestModel;
import com.example.demo.models.TestModelInput;
import com.example.demo.repositories.TestRepository;
import com.example.demo.services.TestService;

@SpringBootTest
public class TestsControllerTest {
	@Autowired
	TestsController testController = new TestsController();
	@Autowired
	TestService testService = new TestService();
	
	TestRepository testRepositoryMock = Mockito.mock(TestRepository.class);
	
	
	@Test
	void testGetTest() {
		testService.setTestRepository(testRepositoryMock);
		
		ArrayList<TestModel> testListMock = new ArrayList<TestModel>();
		TestModel test = new TestModel();
		test.setIdTest(Long.parseLong("1"));
		test.setName("Test 2");
		test.setDescription("Descripción test 1");
		testListMock.add(test);
		
		Mockito.when(testService.getTests()).thenReturn(testListMock);
		
		ResponseEntity<ArrayList<TestModel>> responseGetTestNoEmpty = testController.getTest();
		Assertions.assertEquals("Test 2", responseGetTestNoEmpty.getBody().get(0).getName());
		Assertions.assertEquals(1, responseGetTestNoEmpty.getBody().get(0).getIdTest());
		Assertions.assertEquals("Descripción test 1", responseGetTestNoEmpty.getBody().get(0).getDescription());
		Assertions.assertEquals(HttpStatus.OK, responseGetTestNoEmpty.getStatusCode());
		Assertions.assertEquals(1, responseGetTestNoEmpty.getBody().size());
	}

	@Test
	void testGetTestEmpty() {
		testService.setTestRepository(testRepositoryMock);
		
		ArrayList<TestModel> testListMock = new ArrayList<TestModel>();
				
		Mockito.when(testService.getTests()).thenReturn(testListMock);
		
		ResponseEntity<ArrayList<TestModel>> responseGetTestEmpty = testController.getTest();
		Assertions.assertEquals(HttpStatus.NO_CONTENT, responseGetTestEmpty.getStatusCode());
		Assertions.assertEquals(true, responseGetTestEmpty.getBody() == null || responseGetTestEmpty.getBody().isEmpty());
	}
	
	@Test
	void testGetTestByIdTestIdBlank() {
		ResponseEntity<?> responseGetTestByIdTestIdBlank = testController.getTestByIdTest("");
		Assertions.assertEquals(HttpStatus.NOT_FOUND, responseGetTestByIdTestIdBlank.getStatusCode());
		Assertions.assertEquals("El idTest ingresado no puede ser vacío.", responseGetTestByIdTestIdBlank.getBody().toString());
	}
	
	@Test
	void testGetTestByIdTestIdNoParseLong() {
		ResponseEntity<?> responseGetTestByIdTestIdNoPArseLong = testController.getTestByIdTest("fds");
		Assertions.assertEquals(HttpStatus.NOT_FOUND, responseGetTestByIdTestIdNoPArseLong.getStatusCode());
		Assertions.assertEquals("El idTest ingresado no es del tipo requerido.", responseGetTestByIdTestIdNoPArseLong.getBody().toString());
	}
	
	@Test
	void testGetTestByIdTestIdExists() {
		testService.setTestRepository(testRepositoryMock);
		TestModel test = new TestModel();
		test.setIdTest(Long.parseLong("1"));
		test.setName("Test 2");
		test.setDescription("Descripción test 1");
		Optional<TestModel> testOptionalMock = java.util.Optional.of(test);
		
		
		Mockito.when(testService.getByIdTest(Long.parseLong("1"))).thenReturn(testOptionalMock);
		
		ResponseEntity<?> responseGetTestByIdTestIdExists = testController.getTestByIdTest("1");
		Assertions.assertEquals(HttpStatus.OK, responseGetTestByIdTestIdExists.getStatusCode());
		Assertions.assertEquals(Optional.class, responseGetTestByIdTestIdExists.getBody().getClass());
		Assertions.assertEquals("Optional[TestModel [idTest=1, name=Test 2, description=Descripción test 1]]", responseGetTestByIdTestIdExists.getBody().toString());		
		
	}
	
	@Test
	void testGetTestByIdTestIdNoExists() {
		testService.setTestRepository(testRepositoryMock);
		
		Optional<TestModel> testOptionalMock = java.util.Optional.empty();
		
		
		Mockito.when(testService.getByIdTest(Long.parseLong("1"))).thenReturn(testOptionalMock);
		
		ResponseEntity<?> responseGetTestByIdTestIdExists = testController.getTestByIdTest("1");
		Assertions.assertEquals(HttpStatus.NOT_FOUND, responseGetTestByIdTestIdExists.getStatusCode());
		Assertions.assertEquals(String.class, responseGetTestByIdTestIdExists.getBody().getClass());
		Assertions.assertEquals("El test que se intenta obtener no existe.", responseGetTestByIdTestIdExists.getBody().toString());		
		
	}
	
	@Test
	void testSaveTestNull() {
		testService.setTestRepository(testRepositoryMock);
		
		TestModel testMock = new TestModel();
				
		Mockito.when(testService.saveTest(testMock)).thenReturn(testMock);
		
		ResponseEntity<String> responseSaveTestNull = testController.saveTest(testMock);
		Assertions.assertEquals(HttpStatus.NOT_FOUND, responseSaveTestNull.getStatusCode());
		Assertions.assertEquals("Los campos correspondientes al nombre y la descripción no pueden ser nulos.", responseSaveTestNull.getBody().toString());		
	}
	
	@Test
	void testSaveTestEmpty() {
		testService.setTestRepository(testRepositoryMock);
		
		TestModel testMock = new TestModel();
		testMock.setName("  ");
		testMock.setDescription("Descripción ...");
				
		Mockito.when(testService.saveTest(testMock)).thenReturn(testMock);
		
		ResponseEntity<String> responseSaveTestEmpty = testController.saveTest(testMock);
		Assertions.assertEquals(HttpStatus.NOT_FOUND, responseSaveTestEmpty.getStatusCode());
		Assertions.assertEquals("Los campos correspondientes al nombre y la descripción no pueden estar vacíos.", responseSaveTestEmpty.getBody().toString());		
	}
	
	@Test
	void testSaveTestOk() {
		testService.setTestRepository(testRepositoryMock);
		
		TestModel test = new TestModel();
		test.setIdTest(Long.parseLong("1"));
		test.setName("Test 2");
		test.setDescription("Descripción test 1");
				
		Mockito.when(testService.saveTest(test)).thenReturn(test);
		
		ResponseEntity<String> responseSaveTestOk = testController.saveTest(test);
		Assertions.assertEquals(HttpStatus.CREATED, responseSaveTestOk.getStatusCode());
		Assertions.assertEquals("Test con id 1 creado exitosamente: TestModel [idTest=1, name=Test 2, description=Descripción test 1]", responseSaveTestOk.getBody().toString());		
	}
	
	@Test
	void testUpdateTestNull() {
		testService.setTestRepository(testRepositoryMock);
		
		TestModelInput testInput = new TestModelInput();
		TestModel testMock = new TestModel();
				
		Mockito.when(testService.saveTest(testMock)).thenReturn(testMock);
		
		ResponseEntity<String> responseUpdateTestNull = testController.updateTest(testInput);
		Assertions.assertEquals(HttpStatus.NOT_FOUND, responseUpdateTestNull.getStatusCode());
		Assertions.assertEquals("Los campos correspondientes al nombre y/o la descripción no pueden ser nulos.", responseUpdateTestNull.getBody().toString());		
	}
	
	@Test
	void testUpdateTestEmpty() {
		testService.setTestRepository(testRepositoryMock);
		
		TestModelInput testInput = new TestModelInput();
		testInput.setName("  ");
		testInput.setDescription("Descripción ...");
		
		TestModel testMock = new TestModel();
		testMock.setName(testInput.getName());
		testMock.setDescription(testInput.getDescription());
				
		Mockito.when(testService.saveTest(testMock)).thenReturn(testMock);
		
		ResponseEntity<String> responseUpdateTestEmpty = testController.updateTest(testInput);
		Assertions.assertEquals(HttpStatus.NOT_FOUND, responseUpdateTestEmpty.getStatusCode());
		Assertions.assertEquals("Los campos correspondientes al nombre y/o la descripción no pueden estar vacíos.", responseUpdateTestEmpty.getBody().toString());		
	}
	
	@Test
	void testUpdateTestIdNoParseLong() {
		testService.setTestRepository(testRepositoryMock);
		
		TestModelInput testInput = new TestModelInput();
		testInput.setId("dfds");
		testInput.setName("Test 1");
		testInput.setDescription("Descripción 1");
		
		ResponseEntity<String> responseUpdateTestEmpty = testController.updateTest(testInput);
		Assertions.assertEquals(HttpStatus.NOT_FOUND, responseUpdateTestEmpty.getStatusCode());
		Assertions.assertEquals("El idTest ingresado no es del tipo requerido.", responseUpdateTestEmpty.getBody().toString());		
	}
	
	@Test
	void testUpdateTestOk() {
		testService.setTestRepository(testRepositoryMock);
		
		TestModelInput testInput = new TestModelInput();
		testInput.setId("1");
		testInput.setName("Test 2");
		testInput.setDescription("Descripción test 2");
		
		TestModel testMock = new TestModel();
		testMock.setIdTest(Long.parseLong(testInput.getIdTest()));
		testMock.setName(testInput.getName());
		testMock.setDescription(testInput.getDescription());
		
		Optional<TestModel> testOptionalMock = java.util.Optional.of(testMock);		
		Mockito.when(testService.getByIdTest(1L)).thenReturn(testOptionalMock);
		
		Mockito.when(testService.saveTest(testMock)).thenReturn(testMock);
		
		ResponseEntity<String> responseUpdateTestOk = testController.updateTest(testInput);
		Assertions.assertEquals(HttpStatus.CREATED, responseUpdateTestOk.getStatusCode());
		Assertions.assertEquals("Test con id 1 actualizado exitosamente: TestModel [idTest=1, name=Test 2, description=Descripción test 2]", responseUpdateTestOk.getBody().toString());
	}
	
	@Test
	void testUpdateTestNotExists() {
		testService.setTestRepository(testRepositoryMock);
		TestModelInput testInput = new TestModelInput();
		testInput.setId("1");
		testInput.setName("Test 1");
		testInput.setDescription("Descripción test 1");
		Optional<TestModel> testOptionalMock = java.util.Optional.empty();
		
		Mockito.when(testService.getByIdTest(Long.parseLong("1"))).thenReturn(testOptionalMock);
		
		ResponseEntity<?> responseUdateTestNotIdExists = testController.updateTest(testInput);
		Assertions.assertEquals(HttpStatus.NOT_FOUND, responseUdateTestNotIdExists.getStatusCode());
		Assertions.assertEquals(String.class, responseUdateTestNotIdExists.getBody().getClass());
		Assertions.assertEquals("El test que se intenta actualizar no existe.", responseUdateTestNotIdExists.getBody().toString());		
		
	}
	
	@Test
	void testDeleteTestByIdTestBlank() {
		ResponseEntity<?> responseDeleteTestByIdTestBlank = testController.deleteTestByIdTest(" ");
		Assertions.assertEquals(HttpStatus.NO_CONTENT, responseDeleteTestByIdTestBlank.getStatusCode());
		Assertions.assertEquals("El idTest no puede ser vacío.", responseDeleteTestByIdTestBlank.getBody().toString());		
	
	}
	
	@Test
	void testDeleteTestByIdTestNoParseLong() {
		ResponseEntity<?> responseDeleteTestByIdTestNoParseLong = testController.deleteTestByIdTest("sffd");
		Assertions.assertEquals(HttpStatus.NO_CONTENT, responseDeleteTestByIdTestNoParseLong.getStatusCode());
		Assertions.assertEquals("El idTest ingresado no es del tipo requerido.", responseDeleteTestByIdTestNoParseLong.getBody().toString());		
	
	}
	
	@Test
	void testDeleteTestNotExists() {
		testService.setTestRepository(testRepositoryMock);
		Optional<TestModel> testOptionalMock = java.util.Optional.empty();
		
		Mockito.when(testService.getByIdTest(Long.parseLong("1"))).thenReturn(testOptionalMock);
		
		ResponseEntity<?> responseDeleteTestNotIdExists = testController.deleteTestByIdTest("1");
		Assertions.assertEquals(HttpStatus.NO_CONTENT, responseDeleteTestNotIdExists.getStatusCode());
		Assertions.assertEquals(String.class, responseDeleteTestNotIdExists.getBody().getClass());
		Assertions.assertEquals("El test que se intenta eliminar no existe.", responseDeleteTestNotIdExists.getBody().toString());		
		
	}
	
	@Test
	void testDeleteTestOk() {
		testService.setTestRepository(testRepositoryMock);
		
		TestModelInput testInput = new TestModelInput();
		testInput.setId("100");
		testInput.setName("Test 2");
		testInput.setDescription("Descripción test 2");
		
		TestModel testMock = new TestModel();
		testMock.setIdTest(Long.parseLong(testInput.getIdTest()));
		testMock.setName(testInput.getName());
		testMock.setDescription(testInput.getDescription());
		
		Optional<TestModel> testOptionalMock = java.util.Optional.of(testMock);	
		Mockito.when(testService.getByIdTest(Long.parseLong("100"))).thenReturn(testOptionalMock);
		Mockito.doNothing().when(testRepositoryMock).deleteById(100L);;
		
		ResponseEntity<String> responseDeletedTestOk = testController.deleteTestByIdTest("100");
		Assertions.assertEquals(HttpStatus.OK, responseDeletedTestOk.getStatusCode());
		Assertions.assertEquals("Test con id 100 eliminado exitosamente: Optional[TestModel [idTest=100, name=Test 2, description=Descripción test 2]]", responseDeletedTestOk.getBody().toString());
	}
}
