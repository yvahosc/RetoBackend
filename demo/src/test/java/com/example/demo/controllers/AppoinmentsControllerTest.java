package com.example.demo.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.demo.models.AffiliateModel;
import com.example.demo.models.AppoinmentModel;
import com.example.demo.models.AppoinmentModelInput;
import com.example.demo.models.TestModel;
import com.example.demo.repositories.AppoinmentRepository;
import com.example.demo.services.AffiliateService;
import com.example.demo.services.AppoinmentService;
import com.example.demo.services.TestService;

@SpringBootTest
public class AppoinmentsControllerTest {
	@Autowired
	AppoinmentController appoinmentController = new AppoinmentController();
	@Autowired
	AppoinmentService appoinmentService = new AppoinmentService();
	@Autowired
	TestService testService = new TestService();
	@Autowired
	AffiliateService affiliateService = new AffiliateService();

	AppoinmentRepository appoinmentRepositoryMock = Mockito.mock(AppoinmentRepository.class);
	
	SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

	SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
	
	@Test
	void testGetAppoinments() throws ParseException {
		appoinmentService.setAppoinmentRepository(appoinmentRepositoryMock);
		
		TestModel test = new TestModel();
		test.setIdTest(Long.parseLong("1"));
		test.setName("Test 2");
		test.setDescription("Descripción test 1");
		
		AffiliateModel affiliate = new AffiliateModel();
		affiliate.setIdAffiliate(1L);
		affiliate.setName("Affiliate 2");
		affiliate.setAge(20);
		affiliate.setMail("ejemplo@gmail.com");
		
		AppoinmentModel appoinment = new AppoinmentModel();
		appoinment.setIdAppoinment(1L);
		appoinment.setDate(dateFormat.parse("10-10-2022"));
		appoinment.setHour(timeFormat.parse("10:20"));
		appoinment.setTest(test);
		appoinment.setAffiliate(affiliate);
		
		
		ArrayList<AppoinmentModel> appoinmentListMock = new ArrayList<AppoinmentModel>();
		appoinmentListMock.add(appoinment);
		
		Mockito.when(appoinmentService.getAppoinments()).thenReturn(appoinmentListMock);
		
		ResponseEntity<ArrayList<AppoinmentModel>> responseGetAppoinmentNoEmpty = appoinmentController.getAppoinment();
		Assertions.assertEquals(1L, responseGetAppoinmentNoEmpty.getBody().get(0).getIdAppoinment());
		Assertions.assertEquals(1L, responseGetAppoinmentNoEmpty.getBody().get(0).getTest().getIdTest());
		Assertions.assertEquals(1L, responseGetAppoinmentNoEmpty.getBody().get(0).getAffiliate().getIdAffiliate());
		Assertions.assertEquals(HttpStatus.OK, responseGetAppoinmentNoEmpty.getStatusCode());
		Assertions.assertEquals(1, responseGetAppoinmentNoEmpty.getBody().size());
	}

	@Test
	void testGetAppoinmentEmpty() {
		appoinmentService.setAppoinmentRepository(appoinmentRepositoryMock);
		
		ArrayList<AppoinmentModel> appoinmentListMock = new ArrayList<AppoinmentModel>();
				
		Mockito.when(appoinmentService.getAppoinments()).thenReturn(appoinmentListMock);
		
		ResponseEntity<ArrayList<AppoinmentModel>> responseGetAppoinmentEmpty = appoinmentController.getAppoinment();
		Assertions.assertEquals(HttpStatus.NO_CONTENT, responseGetAppoinmentEmpty.getStatusCode());
		Assertions.assertEquals(true, responseGetAppoinmentEmpty.getBody() == null || responseGetAppoinmentEmpty.getBody().isEmpty());
	}
	
	@Test
	void testGetAppoinmentByIdAppoinmentIdBlank() {
		ResponseEntity<?> responseGetAppoinmentByIdAppoinmentIdBlank = appoinmentController.getAppoinmentByIdAppoinment("");
		Assertions.assertEquals(HttpStatus.NOT_FOUND, responseGetAppoinmentByIdAppoinmentIdBlank.getStatusCode());
		Assertions.assertEquals("El idAppoinment ingresado no puede ser vacío.", responseGetAppoinmentByIdAppoinmentIdBlank.getBody().toString());
	}
	
	@Test
	void testGetAppoinmentByIdAppoinmentIdNoParseLong() {
		ResponseEntity<?> responseGetAppoinmentByIdAppoinmentIdNoPArseLong = appoinmentController.getAppoinmentByIdAppoinment("fds");
		Assertions.assertEquals(HttpStatus.NOT_FOUND, responseGetAppoinmentByIdAppoinmentIdNoPArseLong.getStatusCode());
		Assertions.assertEquals("El idAppoinment ingresado no es del tipo requerido.", responseGetAppoinmentByIdAppoinmentIdNoPArseLong.getBody().toString());
	}
	
	@Test
	void testGetAppoinmentByIdAppoinmentIdExists() throws ParseException {
		appoinmentService.setAppoinmentRepository(appoinmentRepositoryMock);
		TestModel test = new TestModel();
		test.setIdTest(Long.parseLong("1"));
		test.setName("Test 2");
		test.setDescription("Descripción test 1");
		
		AffiliateModel affiliate = new AffiliateModel();
		affiliate.setIdAffiliate(1L);
		affiliate.setName("Affiliate 2");
		affiliate.setAge(20);
		affiliate.setMail("ejemplo@gmail.com");
		
		AppoinmentModel appoinment = new AppoinmentModel();
		appoinment.setIdAppoinment(1L);
		appoinment.setDate(dateFormat.parse("10-10-2022"));
		appoinment.setHour(timeFormat.parse("10:20"));
		appoinment.setTest(test);
		appoinment.setAffiliate(affiliate);
		
		Optional<AppoinmentModel> appoinmentOptionalMock = java.util.Optional.of(appoinment);
		
		
		Mockito.when(appoinmentService.getByIdAppoinment(1L)).thenReturn(appoinmentOptionalMock);
		
		ResponseEntity<?> responseGetAppoinmentByIdAppoinmenteIdExists = appoinmentController.getAppoinmentByIdAppoinment("1");
		Assertions.assertEquals(HttpStatus.OK, responseGetAppoinmentByIdAppoinmenteIdExists.getStatusCode());
		Assertions.assertEquals(Optional.class, responseGetAppoinmentByIdAppoinmenteIdExists.getBody().getClass());
		Assertions.assertEquals("Optional[AppoinmentModel [date=Mon Oct 10 00:00:00 COT 2022, hour=Thu Jan 01 10:20:00 COT 1970, idTest=1, idAffiliate=1]]", responseGetAppoinmentByIdAppoinmenteIdExists.getBody().toString());		
		
	}
	
	@Test
	void testGetAppoinmentByIdAppoinmentIdNoExists() {
		appoinmentService.setAppoinmentRepository(appoinmentRepositoryMock);
		
		Optional<AppoinmentModel> appoinmentOptionalMock = java.util.Optional.empty();
		
		
		Mockito.when(appoinmentService.getByIdAppoinment(Long.parseLong("1"))).thenReturn(appoinmentOptionalMock);
		
		ResponseEntity<?> responseGetAppoinmentByIdAppoinmentIdNoExists = appoinmentController.getAppoinmentByIdAppoinment("1");
		Assertions.assertEquals(HttpStatus.NOT_FOUND, responseGetAppoinmentByIdAppoinmentIdNoExists.getStatusCode());
		Assertions.assertEquals(String.class, responseGetAppoinmentByIdAppoinmentIdNoExists.getBody().getClass());
		Assertions.assertEquals("El appoinment que se intenta obtener no existe.", responseGetAppoinmentByIdAppoinmentIdNoExists.getBody().toString());		
		
	}
	
	@Test
	void testSaveAppoinmentNull() {
		appoinmentService.setAppoinmentRepository(appoinmentRepositoryMock);
		
		AppoinmentModel appoinmentMock = new AppoinmentModel();
		AppoinmentModelInput appoinmentInputMock = new AppoinmentModelInput();
				
		Mockito.when(appoinmentService.saveAppoinment(appoinmentMock)).thenReturn(appoinmentMock);
		
		ResponseEntity<String> responseSaveAppoinmentNull = appoinmentController.saveAppoinment(appoinmentInputMock);
		Assertions.assertEquals(HttpStatus.NOT_FOUND, responseSaveAppoinmentNull.getStatusCode());
		Assertions.assertEquals("Los campos correspondientes a date, hour, idTest y idAffiliate no pueden ser nulos.", responseSaveAppoinmentNull.getBody().toString());		
	}
	
	@Test
	void testSaveAppoinmentEmpty() {
		appoinmentService.setAppoinmentRepository(appoinmentRepositoryMock);
		
		AppoinmentModelInput appoinmentInputMock = new AppoinmentModelInput();
		appoinmentInputMock.setDate("   ");
		appoinmentInputMock.setHour("10:20");
		appoinmentInputMock.setIdTest("1");
		appoinmentInputMock.setIdAffiliate("1");
		
		ResponseEntity<String> responseSaveAppoinmentEmpty = appoinmentController.saveAppoinment(appoinmentInputMock);
		Assertions.assertEquals(HttpStatus.NOT_FOUND, responseSaveAppoinmentEmpty.getStatusCode());
		Assertions.assertEquals("Los campos correspondientes a date, hour, idTest y idAffiliate no pueden estar vacíos.", responseSaveAppoinmentEmpty.getBody().toString());		
	}
	
	@Test
	void testSaveAppoinmentOk() throws ParseException {
		appoinmentService.setAppoinmentRepository(appoinmentRepositoryMock);
		
		AppoinmentModelInput appoinmentInputMock = new AppoinmentModelInput();
		appoinmentInputMock.setIdAppoinment("1");
		appoinmentInputMock.setDate("10-10-2022");
		appoinmentInputMock.setHour("10:20");
		appoinmentInputMock.setIdAffiliate("1");
		appoinmentInputMock.setIdTest("1");
		
		TestModel test = new TestModel();
		test.setIdTest(Long.parseLong("1"));
		test.setName("Test 2");
		test.setDescription("Descripción test 1");
		
		AffiliateModel affiliate = new AffiliateModel();
		affiliate.setIdAffiliate(1L);
		affiliate.setName("Affiliate 2");
		affiliate.setAge(20);
		affiliate.setMail("ejemplo@gmail.com");
		
		AppoinmentModel appoinment = new AppoinmentModel();
		appoinment.setIdAppoinment(Long.parseLong(appoinmentInputMock.getIdAppoinment()));
		appoinment.setDate(dateFormat.parse(appoinmentInputMock.getDate()));
		appoinment.setHour(timeFormat.parse(appoinmentInputMock.getHour()));
		appoinment.setTest(test);
		appoinment.setAffiliate(affiliate);
		
		Optional<TestModel> testOptionalMock = java.util.Optional.of(test);
		Optional<AffiliateModel> affiliateOptionalMock = java.util.Optional.of(affiliate);
				
		Mockito.when(appoinmentService.saveAppoinment(appoinment)).thenReturn(appoinment);
		Mockito.when(testService.getByIdTest(appoinment.getTest().getIdTest())).thenReturn(testOptionalMock);
		Mockito.when(affiliateService.getByIdAffiliate(appoinment.getAffiliate().getIdAffiliate())).thenReturn(affiliateOptionalMock);
		
		ResponseEntity<String> responseSaveAppoinmentOk = appoinmentController.saveAppoinment(appoinmentInputMock);
		Assertions.assertEquals(HttpStatus.CREATED, responseSaveAppoinmentOk.getStatusCode());
		Assertions.assertEquals("Appoinment creado exitosamente: AppoinmentModel [date=Mon Oct 10 00:00:00 COT 2022, hour=Thu Jan 01 10:20:00 COT 1970, idTest=1, idAffiliate=1]", responseSaveAppoinmentOk.getBody().toString());		
	}
	
	@Test
	void testSaveAppoinmentIdTestOrIdAffiliateNoExists() throws ParseException {
		appoinmentService.setAppoinmentRepository(appoinmentRepositoryMock);
		
		AppoinmentModelInput appoinmentInputMock = new AppoinmentModelInput();
		appoinmentInputMock.setIdAppoinment("1");
		appoinmentInputMock.setDate("10-10-2022");
		appoinmentInputMock.setHour("10:20");
		appoinmentInputMock.setIdAffiliate("1");
		appoinmentInputMock.setIdTest("1");
		
		TestModel test = new TestModel();
		test.setIdTest(Long.parseLong("1"));
		test.setName("Test 2");
		test.setDescription("Descripción test 1");
		
		AffiliateModel affiliate = new AffiliateModel();
		affiliate.setIdAffiliate(1L);
		affiliate.setName("Affiliate 2");
		affiliate.setAge(20);
		affiliate.setMail("ejemplo@gmail.com");
		
		AppoinmentModel appoinment = new AppoinmentModel();
		appoinment.setIdAppoinment(Long.parseLong(appoinmentInputMock.getIdAppoinment()));
		appoinment.setDate(dateFormat.parse(appoinmentInputMock.getDate()));
		appoinment.setHour(timeFormat.parse(appoinmentInputMock.getHour()));
		appoinment.setTest(test);
		appoinment.setAffiliate(affiliate);
		
		Optional<TestModel> testOptionalMock = java.util.Optional.empty();
		Optional<AffiliateModel> affiliateOptionalMock = java.util.Optional.empty();
				
		Mockito.when(testService.getByIdTest(appoinment.getTest().getIdTest())).thenReturn(testOptionalMock);
		Mockito.when(affiliateService.getByIdAffiliate(appoinment.getAffiliate().getIdAffiliate())).thenReturn(affiliateOptionalMock);
		
		ResponseEntity<String> responseSaveAppoinmentIdTestOrIdAffiliateNoExists = appoinmentController.saveAppoinment(appoinmentInputMock);
		Assertions.assertEquals(HttpStatus.NOT_FOUND, responseSaveAppoinmentIdTestOrIdAffiliateNoExists.getStatusCode());
		Assertions.assertEquals("El Appoinment no puede ser creado debido a que el idTest o el idAffiliate asociados no existen.", responseSaveAppoinmentIdTestOrIdAffiliateNoExists.getBody().toString());		
	}
	
	@Test
	void testSaveAppoinmentDateNoParse() {
		appoinmentService.setAppoinmentRepository(appoinmentRepositoryMock);
		
		AppoinmentModelInput appoinmentInputMock = new AppoinmentModelInput();
		appoinmentInputMock.setDate("jfksdj");
		appoinmentInputMock.setHour("10:20");
		appoinmentInputMock.setIdTest("1");
		appoinmentInputMock.setIdAffiliate("1");
		
		ResponseEntity<String> responseSaveAppoinmentDateNoParse = appoinmentController.saveAppoinment(appoinmentInputMock);
		Assertions.assertEquals(HttpStatus.NOT_FOUND, responseSaveAppoinmentDateNoParse.getStatusCode());
		Assertions.assertEquals("La(s) variable(s) date, hour, idTest y/o idAffiliate ingresada(s) no es(son) del tipo requerido.", responseSaveAppoinmentDateNoParse.getBody().toString());		
	}
	
	@Test
	void testSaveAppoinmentHourNoParse() {
		appoinmentService.setAppoinmentRepository(appoinmentRepositoryMock);
		
		AppoinmentModelInput appoinmentInputMock = new AppoinmentModelInput();
		appoinmentInputMock.setDate("10-10-2022");
		appoinmentInputMock.setHour("jhdskfhs");
		appoinmentInputMock.setIdTest("1");
		appoinmentInputMock.setIdAffiliate("1");
		
		ResponseEntity<String> responseSaveAppoinmentHourNoParse = appoinmentController.saveAppoinment(appoinmentInputMock);
		Assertions.assertEquals(HttpStatus.NOT_FOUND, responseSaveAppoinmentHourNoParse.getStatusCode());
		Assertions.assertEquals("La(s) variable(s) date, hour, idTest y/o idAffiliate ingresada(s) no es(son) del tipo requerido.", responseSaveAppoinmentHourNoParse.getBody().toString());		
	}
	
	@Test
	void testSaveAppoinmentIdTestNoParse() {
		appoinmentService.setAppoinmentRepository(appoinmentRepositoryMock);
		
		AppoinmentModelInput appoinmentInputMock = new AppoinmentModelInput();
		appoinmentInputMock.setDate("10-10-2022");
		appoinmentInputMock.setHour("10:20");
		appoinmentInputMock.setIdTest("jsnfksdn");
		appoinmentInputMock.setIdAffiliate("1");
		
		ResponseEntity<String> responseSaveAppoinmentIdTestNoParse = appoinmentController.saveAppoinment(appoinmentInputMock);
		Assertions.assertEquals(HttpStatus.NOT_FOUND, responseSaveAppoinmentIdTestNoParse.getStatusCode());
		Assertions.assertEquals("La(s) variable(s) date, hour, idTest y/o idAffiliate ingresada(s) no es(son) del tipo requerido.", responseSaveAppoinmentIdTestNoParse.getBody().toString());		
	}
	
	@Test
	void testSaveAppoinmentIdAffiliateNoParse() {
		appoinmentService.setAppoinmentRepository(appoinmentRepositoryMock);
		
		AppoinmentModelInput appoinmentInputMock = new AppoinmentModelInput();
		appoinmentInputMock.setDate("10-10-2022");
		appoinmentInputMock.setHour("10:20");
		appoinmentInputMock.setIdTest("1");
		appoinmentInputMock.setIdAffiliate("dldsnfs");
		
		ResponseEntity<String> responseSaveAppoinmentIdAffiliateNoParse = appoinmentController.saveAppoinment(appoinmentInputMock);
		Assertions.assertEquals(HttpStatus.NOT_FOUND, responseSaveAppoinmentIdAffiliateNoParse.getStatusCode());
		Assertions.assertEquals("La(s) variable(s) date, hour, idTest y/o idAffiliate ingresada(s) no es(son) del tipo requerido.", responseSaveAppoinmentIdAffiliateNoParse.getBody().toString());		
	}
	
	
	@Test
	void appoinmentUpdateAppoinmentNull() {
		appoinmentService.setAppoinmentRepository(appoinmentRepositoryMock);
		
		AppoinmentModelInput appoinmentInput = new AppoinmentModelInput();
		
		ResponseEntity<String> responseUpdateAppoinmentNull = appoinmentController.updateAppoinment(appoinmentInput);
		Assertions.assertEquals(HttpStatus.NOT_FOUND, responseUpdateAppoinmentNull.getStatusCode());
		Assertions.assertEquals("Los campos correspondientes a idAppoinment, date, hour, idTest y/o idAffiliate no pueden ser nulos.", responseUpdateAppoinmentNull.getBody().toString());		
	}
	
	@Test
	void testUpdateAffiliateEmpty() {
		appoinmentService.setAppoinmentRepository(appoinmentRepositoryMock);
		
		AppoinmentModelInput appoinmentInputMock = new AppoinmentModelInput();
		appoinmentInputMock.setIdAppoinment("1");
		appoinmentInputMock.setDate("   ");
		appoinmentInputMock.setHour("10:20");
		appoinmentInputMock.setIdTest("1");
		appoinmentInputMock.setIdAffiliate("1");
		
		ResponseEntity<String> responseUpdateAppoinmentEmpty = appoinmentController.updateAppoinment(appoinmentInputMock);
		Assertions.assertEquals(HttpStatus.NOT_FOUND, responseUpdateAppoinmentEmpty.getStatusCode());
		Assertions.assertEquals("Los campos correspondientes a idAppoinment, date, hour, idTest y idAffiliate no pueden estar vacíos.", responseUpdateAppoinmentEmpty.getBody().toString());		
	}
	
	@Test
	void testUpdateAppoinmentIdAppoinmentNoParse() {
		appoinmentService.setAppoinmentRepository(appoinmentRepositoryMock);
		
		AppoinmentModelInput affiliateInput = new AppoinmentModelInput();
		affiliateInput.setIdAppoinment("jnsfsd");
		affiliateInput.setDate("10-10-2022");
		affiliateInput.setHour("10:20");
		affiliateInput.setIdTest("1");
		affiliateInput.setIdAffiliate("1");
		
		ResponseEntity<String> responseUpdateAppoinmentIdAppoinmentNoPase = appoinmentController.updateAppoinment(affiliateInput);
		Assertions.assertEquals(HttpStatus.NOT_FOUND, responseUpdateAppoinmentIdAppoinmentNoPase.getStatusCode());
		Assertions.assertEquals("La(s) variable(s) idAppoinment, date, hour, idTest y/o idAffiliate ingresada(s) no es(son) del tipo requerido.", responseUpdateAppoinmentIdAppoinmentNoPase.getBody().toString());		
	}
	
	@Test
	void testUpdateAppoinmentDateNoParse() {
		appoinmentService.setAppoinmentRepository(appoinmentRepositoryMock);
		
		AppoinmentModelInput affiliateInput = new AppoinmentModelInput();
		affiliateInput.setIdAppoinment("1");
		affiliateInput.setDate("lfgndl");
		affiliateInput.setHour("10:20");
		affiliateInput.setIdTest("1");
		affiliateInput.setIdAffiliate("1");
		
		ResponseEntity<String> responseUpdateAppoinmentDateNoPase = appoinmentController.updateAppoinment(affiliateInput);
		Assertions.assertEquals(HttpStatus.NOT_FOUND, responseUpdateAppoinmentDateNoPase.getStatusCode());
		Assertions.assertEquals("La(s) variable(s) idAppoinment, date, hour, idTest y/o idAffiliate ingresada(s) no es(son) del tipo requerido.", responseUpdateAppoinmentDateNoPase.getBody().toString());		
	}
	
	@Test
	void testUpdateAppoinmentHourNoParse() {
		appoinmentService.setAppoinmentRepository(appoinmentRepositoryMock);
		
		AppoinmentModelInput affiliateInput = new AppoinmentModelInput();
		affiliateInput.setIdAppoinment("1");
		affiliateInput.setDate("10-10-2022");
		affiliateInput.setHour("fkdslf");
		affiliateInput.setIdTest("1");
		affiliateInput.setIdAffiliate("1");
		
		ResponseEntity<String> responseUpdateAppoinmentHourNoPase = appoinmentController.updateAppoinment(affiliateInput);
		Assertions.assertEquals(HttpStatus.NOT_FOUND, responseUpdateAppoinmentHourNoPase.getStatusCode());
		Assertions.assertEquals("La(s) variable(s) idAppoinment, date, hour, idTest y/o idAffiliate ingresada(s) no es(son) del tipo requerido.", responseUpdateAppoinmentHourNoPase.getBody().toString());		
	}
	
	@Test
	void testUpdateAppoinmentIdTestNoParse() {
		appoinmentService.setAppoinmentRepository(appoinmentRepositoryMock);
		
		AppoinmentModelInput affiliateInput = new AppoinmentModelInput();
		affiliateInput.setIdAppoinment("1");
		affiliateInput.setDate("10-10-2022");
		affiliateInput.setHour("10:20");
		affiliateInput.setIdTest("sdhfkjs");
		affiliateInput.setIdAffiliate("1");
		
		ResponseEntity<String> responseUpdateAppoinmentIdTestNoPase = appoinmentController.updateAppoinment(affiliateInput);
		Assertions.assertEquals(HttpStatus.NOT_FOUND, responseUpdateAppoinmentIdTestNoPase.getStatusCode());
		Assertions.assertEquals("La(s) variable(s) idAppoinment, date, hour, idTest y/o idAffiliate ingresada(s) no es(son) del tipo requerido.", responseUpdateAppoinmentIdTestNoPase.getBody().toString());		
	}
	
	@Test
	void testUpdateAppoinmentIdAffiliateNoParse() {
		appoinmentService.setAppoinmentRepository(appoinmentRepositoryMock);
		
		AppoinmentModelInput affiliateInput = new AppoinmentModelInput();
		affiliateInput.setIdAppoinment("1");
		affiliateInput.setDate("10-10-2022");
		affiliateInput.setHour("10:20");
		affiliateInput.setIdTest("1");
		affiliateInput.setIdAffiliate("jbfkjsd");
		
		ResponseEntity<String> responseUpdateAppoinmentIdAffiliateNoPase = appoinmentController.updateAppoinment(affiliateInput);
		Assertions.assertEquals(HttpStatus.NOT_FOUND, responseUpdateAppoinmentIdAffiliateNoPase.getStatusCode());
		Assertions.assertEquals("La(s) variable(s) idAppoinment, date, hour, idTest y/o idAffiliate ingresada(s) no es(son) del tipo requerido.", responseUpdateAppoinmentIdAffiliateNoPase.getBody().toString());		
	}
	
	@Test
	void testUpdateAppoinmentOk() throws ParseException {
		appoinmentService.setAppoinmentRepository(appoinmentRepositoryMock);
		
		AppoinmentModelInput appoinmentInputMock = new AppoinmentModelInput();
		appoinmentInputMock.setIdAppoinment("1");
		appoinmentInputMock.setDate("10-10-2022");
		appoinmentInputMock.setHour("10:20");
		appoinmentInputMock.setIdAffiliate("1");
		appoinmentInputMock.setIdTest("1");
		
		TestModel test = new TestModel();
		test.setIdTest(Long.parseLong("1"));
		test.setName("Test 2");
		test.setDescription("Descripción test 1");
		
		AffiliateModel affiliate = new AffiliateModel();
		affiliate.setIdAffiliate(1L);
		affiliate.setName("Affiliate 2");
		affiliate.setAge(20);
		affiliate.setMail("ejemplo@gmail.com");
		
		AppoinmentModel appoinment = new AppoinmentModel();
		appoinment.setIdAppoinment(Long.parseLong(appoinmentInputMock.getIdAppoinment()));
		appoinment.setDate(dateFormat.parse(appoinmentInputMock.getDate()));
		appoinment.setHour(timeFormat.parse(appoinmentInputMock.getHour()));
		appoinment.setTest(test);
		appoinment.setAffiliate(affiliate);
		
		Optional<AppoinmentModel> appoinmentOptionalMock = java.util.Optional.of(appoinment);
		Optional<TestModel> testOptionalMock = java.util.Optional.of(test);
		Optional<AffiliateModel> affiliateOptionalMock = java.util.Optional.of(affiliate);
		
		Mockito.when(appoinmentService.getByIdAppoinment(1L)).thenReturn(appoinmentOptionalMock);
		Mockito.when(appoinmentService.saveAppoinment(appoinment)).thenReturn(appoinment);
		Mockito.when(testService.getByIdTest(appoinment.getTest().getIdTest())).thenReturn(testOptionalMock);
		Mockito.when(affiliateService.getByIdAffiliate(appoinment.getAffiliate().getIdAffiliate())).thenReturn(affiliateOptionalMock);
		
		
		ResponseEntity<String> responseUpdateAppoinmentOk = appoinmentController.updateAppoinment(appoinmentInputMock);
		Assertions.assertEquals(HttpStatus.CREATED, responseUpdateAppoinmentOk.getStatusCode());
		Assertions.assertEquals("Appoinment con id 1 actualizado exitosamente: AppoinmentModel [date=Mon Oct 10 00:00:00 COT 2022, hour=Thu Jan 01 10:20:00 COT 1970, idTest=1, idAffiliate=1]", responseUpdateAppoinmentOk.getBody().toString());
	}
	
	@Test
	void testUpdateAppoinmentIdTestOrIdAffiliateNotExists() throws ParseException {
		appoinmentService.setAppoinmentRepository(appoinmentRepositoryMock);
		
		AppoinmentModelInput appoinmentInputMock = new AppoinmentModelInput();
		appoinmentInputMock.setIdAppoinment("1");
		appoinmentInputMock.setDate("10-10-2022");
		appoinmentInputMock.setHour("10:20");
		appoinmentInputMock.setIdAffiliate("1");
		appoinmentInputMock.setIdTest("1");
		
		TestModel test = new TestModel();
		test.setIdTest(Long.parseLong("1"));
		test.setName("Test 2");
		test.setDescription("Descripción test 1");
		
		AffiliateModel affiliate = new AffiliateModel();
		affiliate.setIdAffiliate(1L);
		affiliate.setName("Affiliate 2");
		affiliate.setAge(20);
		affiliate.setMail("ejemplo@gmail.com");
		
		AppoinmentModel appoinment = new AppoinmentModel();
		appoinment.setIdAppoinment(Long.parseLong(appoinmentInputMock.getIdAppoinment()));
		appoinment.setDate(dateFormat.parse(appoinmentInputMock.getDate()));
		appoinment.setHour(timeFormat.parse(appoinmentInputMock.getHour()));
		appoinment.setTest(test);
		appoinment.setAffiliate(affiliate);
		
		Optional<AppoinmentModel> appoinmentOptionalMock = java.util.Optional.of(appoinment);
		Optional<TestModel> testOptionalMock = java.util.Optional.empty();
		Optional<AffiliateModel> affiliateOptionalMock = java.util.Optional.empty();		
		Mockito.when(appoinmentService.getByIdAppoinment(1L)).thenReturn(appoinmentOptionalMock);
		Mockito.when(appoinmentService.saveAppoinment(appoinment)).thenReturn(appoinment);
		Mockito.when(testService.getByIdTest(appoinment.getTest().getIdTest())).thenReturn(testOptionalMock);
		Mockito.when(affiliateService.getByIdAffiliate(appoinment.getAffiliate().getIdAffiliate())).thenReturn(affiliateOptionalMock);
		
		
		ResponseEntity<String> responseUpdateAppoinmentOk = appoinmentController.updateAppoinment(appoinmentInputMock);
		Assertions.assertEquals(HttpStatus.NOT_FOUND, responseUpdateAppoinmentOk.getStatusCode());
		Assertions.assertEquals("El Appoinment no puede ser actualizado debido a que el idTest o el idAffiliate asociados no existen.", responseUpdateAppoinmentOk.getBody().toString());
	}

	@Test
	void testUpdateAppoinmentNotExists() {
		appoinmentService.setAppoinmentRepository(appoinmentRepositoryMock);
		AppoinmentModelInput appoinmentInput = new AppoinmentModelInput();
		appoinmentInput.setIdAppoinment("1");
		appoinmentInput.setDate("10-10-2022");
		appoinmentInput.setHour("10:20");
		appoinmentInput.setIdTest("1");
		appoinmentInput.setIdAffiliate("1");
		
		Optional<AppoinmentModel> appoinmentOptionalMock = java.util.Optional.empty();
		
		Mockito.when(appoinmentService.getByIdAppoinment(Long.parseLong("1"))).thenReturn(appoinmentOptionalMock);
		
		ResponseEntity<?> responseUdateAppoinmentNotIdExists = appoinmentController.updateAppoinment(appoinmentInput);
		Assertions.assertEquals(HttpStatus.NOT_FOUND, responseUdateAppoinmentNotIdExists.getStatusCode());
		Assertions.assertEquals(String.class, responseUdateAppoinmentNotIdExists.getBody().getClass());
		Assertions.assertEquals("El Appoinment que se intenta actualizar no existe.", responseUdateAppoinmentNotIdExists.getBody().toString());		
		
	}
	
	@Test
	void testDeleteAppoinmentByIdAppoinmentBlank() {
		ResponseEntity<?> responseDeleteAppoinmentByIdAppoinmentBlank = appoinmentController.deleteAppoinmentByIdAppoinment(" ");
		Assertions.assertEquals(HttpStatus.NO_CONTENT, responseDeleteAppoinmentByIdAppoinmentBlank.getStatusCode());
		Assertions.assertEquals("El idAppoinment no puede ser vacío.", responseDeleteAppoinmentByIdAppoinmentBlank.getBody().toString());		
	
	}
	
	@Test
	void testDeleteAppoinmentByIdAppoinmentNoParseLong() {
		ResponseEntity<?> responseDeleteAppoinmentByIdAppoinmentNoParseLong = appoinmentController.deleteAppoinmentByIdAppoinment("sffd");
		Assertions.assertEquals(HttpStatus.NO_CONTENT, responseDeleteAppoinmentByIdAppoinmentNoParseLong.getStatusCode());
		Assertions.assertEquals("El idAppoinment ingresado no es del tipo requerido.", responseDeleteAppoinmentByIdAppoinmentNoParseLong.getBody().toString());		
	
	}
	
	@Test
	void testDeleteAppoinmentNotExists() {
		appoinmentService.setAppoinmentRepository(appoinmentRepositoryMock);
		Optional<AppoinmentModel> appoinmentOptionalMock = java.util.Optional.empty();
		
		Mockito.when(appoinmentService.getByIdAppoinment(Long.parseLong("1"))).thenReturn(appoinmentOptionalMock);
		
		ResponseEntity<?> responseDeleteAppoinmentNotIdExists = appoinmentController.deleteAppoinmentByIdAppoinment("1");
		Assertions.assertEquals(HttpStatus.NO_CONTENT, responseDeleteAppoinmentNotIdExists.getStatusCode());
		Assertions.assertEquals(String.class, responseDeleteAppoinmentNotIdExists.getBody().getClass());
		Assertions.assertEquals("El appoinment que se intenta eliminar no existe.", responseDeleteAppoinmentNotIdExists.getBody().toString());		
		
	}
	
	@Test
	void testDeleteAppoinmentOk() throws ParseException {
		appoinmentService.setAppoinmentRepository(appoinmentRepositoryMock);
		
		AppoinmentModelInput appoinmentInputMock = new AppoinmentModelInput();
		appoinmentInputMock.setIdAppoinment("1");
		appoinmentInputMock.setDate("10-10-2022");
		appoinmentInputMock.setHour("10:20");
		appoinmentInputMock.setIdAffiliate("1");
		appoinmentInputMock.setIdTest("1");
		
		TestModel test = new TestModel();
		test.setIdTest(Long.parseLong("1"));
		test.setName("Test 2");
		test.setDescription("Descripción test 1");
		
		AffiliateModel affiliate = new AffiliateModel();
		affiliate.setIdAffiliate(1L);
		affiliate.setName("Affiliate 2");
		affiliate.setAge(20);
		affiliate.setMail("ejemplo@gmail.com");
		
		AppoinmentModel appoinment = new AppoinmentModel();
		appoinment.setIdAppoinment(Long.parseLong(appoinmentInputMock.getIdAppoinment()));
		appoinment.setDate(dateFormat.parse(appoinmentInputMock.getDate()));
		appoinment.setHour(timeFormat.parse(appoinmentInputMock.getHour()));
		appoinment.setTest(test);
		appoinment.setAffiliate(affiliate);
		
		Optional<AppoinmentModel> appoinmentOptionalMock = java.util.Optional.of(appoinment);	
		Mockito.when(appoinmentService.getByIdAppoinment(Long.parseLong("1"))).thenReturn(appoinmentOptionalMock);
		Mockito.doNothing().when(appoinmentRepositoryMock).deleteById(1L);;
		
		ResponseEntity<String> responseDeletedAppoinmentOk = appoinmentController.deleteAppoinmentByIdAppoinment("1");
		Assertions.assertEquals(HttpStatus.OK, responseDeletedAppoinmentOk.getStatusCode());
		Assertions.assertEquals("El appoinment con id 1 eliminado exitosamente: Optional[AppoinmentModel [date=Mon Oct 10 00:00:00 COT 2022, hour=Thu Jan 01 10:20:00 COT 1970, idTest=1, idAffiliate=1]]", responseDeletedAppoinmentOk.getBody().toString());
	}
	
	@Test
	void testGetAppoinmentByDateWithDateBlank() {
		ResponseEntity<?> responseGetAppoinmentByDateWithDateBlank = appoinmentController.getAppoinmentByDate("");
		Assertions.assertEquals(HttpStatus.NOT_FOUND, responseGetAppoinmentByDateWithDateBlank.getStatusCode());
		Assertions.assertEquals("El dateAppoinment no puede ser vacío.", responseGetAppoinmentByDateWithDateBlank.getBody().toString());
	}
	
	@Test
	void testGetAppoinmentByDateWithDateNotParseLong() {
		ResponseEntity<?> responseGetAppoinmentByDateWithDateNotParseLong = appoinmentController.getAppoinmentByDate("jdf");
		Assertions.assertEquals(HttpStatus.NOT_FOUND, responseGetAppoinmentByDateWithDateNotParseLong.getStatusCode());
		Assertions.assertEquals("El dateAppoinment ingresado no es del tipo requerido.", responseGetAppoinmentByDateWithDateNotParseLong.getBody().toString());
	}
	
	@Test
	void testGetAppoinmentByDateListEmpty() throws ParseException {
		appoinmentService.setAppoinmentRepository(appoinmentRepositoryMock);
		ArrayList<AppoinmentModel> appoinmentByDate = new ArrayList<AppoinmentModel>();
		
		Mockito.when(appoinmentService.getByDate(dateFormat.parse("10-10-2022"))).thenReturn(appoinmentByDate);
		
		ResponseEntity<?> responseGetAppoinmentByDateListEmpty = appoinmentController.getAppoinmentByDate("10-10-2022");
		Assertions.assertEquals(HttpStatus.NOT_FOUND, responseGetAppoinmentByDateListEmpty.getStatusCode());
		Assertions.assertEquals("No existen appoinments asignados para la fecha 10-10-2022", responseGetAppoinmentByDateListEmpty.getBody().toString());
	}
	
	@Test
	void testGetAppoinmentByDateListOk() throws ParseException {
		appoinmentService.setAppoinmentRepository(appoinmentRepositoryMock);
		ArrayList<AppoinmentModel> appoinmentByDate = new ArrayList<AppoinmentModel>();
		
		AppoinmentModelInput appoinmentInputMock = new AppoinmentModelInput();
		appoinmentInputMock.setIdAppoinment("1");
		appoinmentInputMock.setDate("10-10-2022");
		appoinmentInputMock.setHour("10:20");
		appoinmentInputMock.setIdAffiliate("1");
		appoinmentInputMock.setIdTest("1");
		
		TestModel test = new TestModel();
		test.setIdTest(Long.parseLong("1"));
		test.setName("Test 2");
		test.setDescription("Descripción test 1");
		
		AffiliateModel affiliate = new AffiliateModel();
		affiliate.setIdAffiliate(1L);
		affiliate.setName("Affiliate 2");
		affiliate.setAge(20);
		affiliate.setMail("ejemplo@gmail.com");
		
		AppoinmentModel appoinment = new AppoinmentModel();
		appoinment.setIdAppoinment(Long.parseLong(appoinmentInputMock.getIdAppoinment()));
		appoinment.setDate(dateFormat.parse(appoinmentInputMock.getDate()));
		appoinment.setHour(timeFormat.parse(appoinmentInputMock.getHour()));
		appoinment.setTest(test);
		appoinment.setAffiliate(affiliate);
		
		appoinmentByDate.add(appoinment);
		
		Mockito.when(appoinmentService.getByDate(dateFormat.parse("10-10-2022"))).thenReturn(appoinmentByDate);
		
		ResponseEntity<?> responseGetAppoinmentByDateListOk = appoinmentController.getAppoinmentByDate("10-10-2022");
		Assertions.assertEquals(HttpStatus.OK, responseGetAppoinmentByDateListOk.getStatusCode());
		Assertions.assertEquals("[AppoinmentModel [date=Mon Oct 10 00:00:00 COT 2022, hour=Thu Jan 01 10:20:00 COT 1970, idTest=1, idAffiliate=1]]", responseGetAppoinmentByDateListOk.getBody().toString());
	}
	
	@Test
	void testGetAppoinmentByAffiliateIdAffiliateBlank() {
		ResponseEntity<?> responseGetAppoinmentByAffiliateIdAffiliateIdBlank = appoinmentController.getByAffiliate("");
		Assertions.assertEquals(HttpStatus.NOT_FOUND, responseGetAppoinmentByAffiliateIdAffiliateIdBlank.getStatusCode());
		Assertions.assertEquals("El idAffiliate no puede ser vacío.", responseGetAppoinmentByAffiliateIdAffiliateIdBlank.getBody().toString());
	}
	
	@Test
	void testGetByAffiliateIdAffiliateNotParseLong() {
		ResponseEntity<?> responseGetAppoinmentByAffiliateIdAffiliateIdNotParseLong = appoinmentController.getByAffiliate("jdf");
		Assertions.assertEquals(HttpStatus.NOT_FOUND, responseGetAppoinmentByAffiliateIdAffiliateIdNotParseLong.getStatusCode());
		Assertions.assertEquals("El idAffiliate ingresado no es del tipo requerido.", responseGetAppoinmentByAffiliateIdAffiliateIdNotParseLong.getBody().toString());
	}
	
	@Test
	void testGetByAffiliateAffiliateNotExists() {
		appoinmentService.setAppoinmentRepository(appoinmentRepositoryMock);
		Optional<AffiliateModel> affiliateOptionalMock = java.util.Optional.empty();
		
		Mockito.when(affiliateService.getByIdAffiliate(Long.parseLong("1"))).thenReturn(affiliateOptionalMock);
		
		ResponseEntity<?> responseGetByAffiliateIdAppoinmentNotIdExists = appoinmentController.getByAffiliate("1");
		Assertions.assertEquals(HttpStatus.NOT_FOUND, responseGetByAffiliateIdAppoinmentNotIdExists.getStatusCode());
		Assertions.assertEquals(String.class, responseGetByAffiliateIdAppoinmentNotIdExists.getBody().getClass());
		Assertions.assertEquals("El Affiliate por el que se intenta filtrar no existe.", responseGetByAffiliateIdAppoinmentNotIdExists.getBody().toString());		
	}
	
	@Test
	void testGetByAffiliateExists() throws ParseException {
		appoinmentService.setAppoinmentRepository(appoinmentRepositoryMock);
		ArrayList<AppoinmentModel> appoinmentByAffiliate = new ArrayList<AppoinmentModel>();
		
		AppoinmentModelInput appoinmentInputMock = new AppoinmentModelInput();
		appoinmentInputMock.setIdAppoinment("1");
		appoinmentInputMock.setDate("10-10-2022");
		appoinmentInputMock.setHour("10:20");
		appoinmentInputMock.setIdAffiliate("1");
		appoinmentInputMock.setIdTest("1");
		
		TestModel test = new TestModel();
		test.setIdTest(Long.parseLong("1"));
		test.setName("Test 2");
		test.setDescription("Descripción test 1");
		
		AffiliateModel affiliate = new AffiliateModel();
		affiliate.setIdAffiliate(1L);
		affiliate.setName("Affiliate 2");
		affiliate.setAge(20);
		affiliate.setMail("ejemplo@gmail.com");
		
		AppoinmentModel appoinment = new AppoinmentModel();
		appoinment.setIdAppoinment(Long.parseLong(appoinmentInputMock.getIdAppoinment()));
		appoinment.setDate(dateFormat.parse(appoinmentInputMock.getDate()));
		appoinment.setHour(timeFormat.parse(appoinmentInputMock.getHour()));
		appoinment.setTest(test);
		appoinment.setAffiliate(affiliate);
		
		appoinmentByAffiliate.add(appoinment);
		
		Optional<AffiliateModel> affiliateOptionalMock = java.util.Optional.of(affiliate);
		
		Mockito.when(affiliateService.getByIdAffiliate(Long.parseLong("1"))).thenReturn(affiliateOptionalMock);
		Mockito.when(appoinmentService.getByAffiliate(affiliate)).thenReturn(appoinmentByAffiliate);
		
		ResponseEntity<?> responseGetByAffiliateExists = appoinmentController.getByAffiliate("1");
		Assertions.assertEquals(HttpStatus.OK, responseGetByAffiliateExists.getStatusCode());
		Assertions.assertEquals(ArrayList.class, responseGetByAffiliateExists.getBody().getClass());
	}
}
