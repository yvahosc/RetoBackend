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

import com.example.demo.models.AffiliateModel;
import com.example.demo.models.AffiliateModelInput;
import com.example.demo.repositories.AffiliateRepository;
import com.example.demo.services.AffiliateService;

@SpringBootTest
public class AffiliatesControllerTest {
	@Autowired
	AffiliatesController affiliateController = new AffiliatesController();
	@Autowired
	AffiliateService affiliateService = new AffiliateService();
	
	AffiliateRepository affiliateRepositoryMock = Mockito.mock(AffiliateRepository.class);
	
	
	@Test
	void testGetAffiliates() {
		affiliateService.setAffiliateRepository(affiliateRepositoryMock);
		
		ArrayList<AffiliateModel> affiliateListMock = new ArrayList<AffiliateModel>();
		AffiliateModel affiliate = new AffiliateModel();
		affiliate.setIdAffiliate(1L);
		affiliate.setName("Affiliate 2");
		affiliate.setAge(20);
		affiliate.setMail("ejemplo@gmail.com");
		affiliateListMock.add(affiliate);
		
		Mockito.when(affiliateService.getAffiliates()).thenReturn(affiliateListMock);
		
		ResponseEntity<ArrayList<AffiliateModel>> responseGetAffiliateNoEmpty = affiliateController.getAffiliate();
		Assertions.assertEquals("Affiliate 2", responseGetAffiliateNoEmpty.getBody().get(0).getName());
		Assertions.assertEquals(1, responseGetAffiliateNoEmpty.getBody().get(0).getIdAffiliate());
		Assertions.assertEquals("ejemplo@gmail.com", responseGetAffiliateNoEmpty.getBody().get(0).getMail());
		Assertions.assertEquals(20, responseGetAffiliateNoEmpty.getBody().get(0).getAge());
		Assertions.assertEquals(HttpStatus.OK, responseGetAffiliateNoEmpty.getStatusCode());
		Assertions.assertEquals(1, responseGetAffiliateNoEmpty.getBody().size());
	}

	@Test
	void testGetAffiliateEmpty() {
		affiliateService.setAffiliateRepository(affiliateRepositoryMock);
		
		ArrayList<AffiliateModel> affiliateListMock = new ArrayList<AffiliateModel>();
				
		Mockito.when(affiliateService.getAffiliates()).thenReturn(affiliateListMock);
		
		ResponseEntity<ArrayList<AffiliateModel>> responseGetAffiliateEmpty = affiliateController.getAffiliate();
		Assertions.assertEquals(HttpStatus.NO_CONTENT, responseGetAffiliateEmpty.getStatusCode());
		Assertions.assertEquals(true, responseGetAffiliateEmpty.getBody() == null || responseGetAffiliateEmpty.getBody().isEmpty());
	}
	
	@Test
	void testGetAffiliateByIdAffiliateIdBlank() {
		ResponseEntity<?> responseGetAffiliateByIdAffiliateIdBlank = affiliateController.getAffiliateByIdAffiliate("");
		Assertions.assertEquals(HttpStatus.NOT_FOUND, responseGetAffiliateByIdAffiliateIdBlank.getStatusCode());
		Assertions.assertEquals("El idAffiliate ingresado no puede ser vacío.", responseGetAffiliateByIdAffiliateIdBlank.getBody().toString());
	}
	
	@Test
	void testGetAffiliateByIdAffiliateIdNoParseLong() {
		ResponseEntity<?> responseGetAffiliateByIdAffiliateIdNoPArseLong = affiliateController.getAffiliateByIdAffiliate("fds");
		Assertions.assertEquals(HttpStatus.NOT_FOUND, responseGetAffiliateByIdAffiliateIdNoPArseLong.getStatusCode());
		Assertions.assertEquals("El idAffiliate ingresado no es del tipo requerido.", responseGetAffiliateByIdAffiliateIdNoPArseLong.getBody().toString());
	}
	
	@Test
	void testGetAffiliateByIdAffiliateIdExists() {
		affiliateService.setAffiliateRepository(affiliateRepositoryMock);
		AffiliateModel affiliate = new AffiliateModel();
		affiliate.setIdAffiliate(1L);
		affiliate.setName("Affiliate 2");
		affiliate.setAge(20);
		affiliate.setMail("ejemplo@gmail.com");
		Optional<AffiliateModel> affiliateOptionalMock = java.util.Optional.of(affiliate);
		
		
		Mockito.when(affiliateService.getByIdAffiliate(1L)).thenReturn(affiliateOptionalMock);
		
		ResponseEntity<?> responseGetAffiliateByIdAffiliateIdExists = affiliateController.getAffiliateByIdAffiliate("1");
		Assertions.assertEquals(HttpStatus.OK, responseGetAffiliateByIdAffiliateIdExists.getStatusCode());
		Assertions.assertEquals(Optional.class, responseGetAffiliateByIdAffiliateIdExists.getBody().getClass());
		Assertions.assertEquals("Optional[AffiliateModel [name=Affiliate 2, age=20, mail=ejemplo@gmail.com]]", responseGetAffiliateByIdAffiliateIdExists.getBody().toString());		
		
	}
	
	@Test
	void testGetAffiliateByIdAffiliateIdNoExists() {
		affiliateService.setAffiliateRepository(affiliateRepositoryMock);
		
		Optional<AffiliateModel> affiliateOptionalMock = java.util.Optional.empty();
		
		
		Mockito.when(affiliateService.getByIdAffiliate(Long.parseLong("1"))).thenReturn(affiliateOptionalMock);
		
		ResponseEntity<?> responseGetAffiliateByIdAffiliateIdExists = affiliateController.getAffiliateByIdAffiliate("1");
		Assertions.assertEquals(HttpStatus.NOT_FOUND, responseGetAffiliateByIdAffiliateIdExists.getStatusCode());
		Assertions.assertEquals(String.class, responseGetAffiliateByIdAffiliateIdExists.getBody().getClass());
		Assertions.assertEquals("El affiliate que se intenta obtener no existe.", responseGetAffiliateByIdAffiliateIdExists.getBody().toString());		
		
	}
	
	@Test
	void testSaveAffiliateNull() {
		affiliateService.setAffiliateRepository(affiliateRepositoryMock);
		
		AffiliateModel affiliateMock = new AffiliateModel();
		AffiliateModelInput affiliateInputMock = new AffiliateModelInput();
				
		Mockito.when(affiliateService.saveAffiliate(affiliateMock)).thenReturn(affiliateMock);
		
		ResponseEntity<String> responseSaveAffiliateNull = affiliateController.saveAffiliate(affiliateInputMock);
		Assertions.assertEquals(HttpStatus.NOT_FOUND, responseSaveAffiliateNull.getStatusCode());
		Assertions.assertEquals("Los campos correspondientes a name, age y/o mail no pueden ser nulos.", responseSaveAffiliateNull.getBody().toString());		
	}
	
	@Test
	void testSaveAffiliateEmpty() {
		affiliateService.setAffiliateRepository(affiliateRepositoryMock);
		
		AffiliateModelInput affiliateInputMock = new AffiliateModelInput();
		affiliateInputMock.setName("  ");
		affiliateInputMock.setAge("10");
		affiliateInputMock.setMail("ejemplo@gmail.com");
		
		ResponseEntity<String> responseSaveAffiliateEmpty = affiliateController.saveAffiliate(affiliateInputMock);
		Assertions.assertEquals(HttpStatus.NOT_FOUND, responseSaveAffiliateEmpty.getStatusCode());
		Assertions.assertEquals("Los campos correspondientes a name, age y/o mail no pueden estar vacíos.", responseSaveAffiliateEmpty.getBody().toString());		
	}
	
	@Test
	void testSaveAffiliateOk() {
		affiliateService.setAffiliateRepository(affiliateRepositoryMock);
		
		AffiliateModelInput affiliateInputMock = new AffiliateModelInput();
		affiliateInputMock.setIdAffiliate("2");
		affiliateInputMock.setName("Affiliate 2");
		affiliateInputMock.setAge("10");
		affiliateInputMock.setMail("ejemplo@gmail.com");
		
		AffiliateModel affiliate1 = new AffiliateModel();
		AffiliateModel affiliate = new AffiliateModel();
		affiliate.setIdAffiliate(Long.parseLong("2"));
		affiliate.setName(affiliateInputMock.getName());
		affiliate.setAge(Integer.parseInt(affiliateInputMock.getAge()));
		affiliate.setMail(affiliateInputMock.getMail());
				
		Mockito.when(affiliateService.saveAffiliate(affiliate1)).thenReturn(affiliate);
		
		ResponseEntity<String> responseSaveAffiliateOk = affiliateController.saveAffiliate(affiliateInputMock);
		Assertions.assertEquals(HttpStatus.CREATED, responseSaveAffiliateOk.getStatusCode());
		Assertions.assertEquals("Affiliate creado exitosamente: AffiliateModel [name=Affiliate 2, age=10, mail=ejemplo@gmail.com]", responseSaveAffiliateOk.getBody().toString());		
	}
	
	@Test
	void testSaveAffiliateAgeNoParseInt() {
		affiliateService.setAffiliateRepository(affiliateRepositoryMock);
		
		AffiliateModelInput affiliateInputMock = new AffiliateModelInput();
		affiliateInputMock.setIdAffiliate("1");
		affiliateInputMock.setName("Affiliate 1");
		affiliateInputMock.setAge("fdsfs");
		affiliateInputMock.setMail("ejemplo@gmail.com");
		
		ResponseEntity<String> responseSaveAffiliateAgeNoParseInt = affiliateController.saveAffiliate(affiliateInputMock);
		Assertions.assertEquals(HttpStatus.NOT_FOUND, responseSaveAffiliateAgeNoParseInt.getStatusCode());
		Assertions.assertEquals("La(s) variable(s) name, age y/o mail ingresada(s) no es(son) del tipo requerido.", responseSaveAffiliateAgeNoParseInt.getBody().toString());		
	}
	
	@Test
	void testSaveAffiliateAgeLessZero() {
		affiliateService.setAffiliateRepository(affiliateRepositoryMock);
		
		AffiliateModelInput affiliateInputMock = new AffiliateModelInput();
		affiliateInputMock.setIdAffiliate("1");
		affiliateInputMock.setName("Affiliate 1");
		affiliateInputMock.setAge("-1");
		affiliateInputMock.setMail("ejemplo@gmail.com");
		
		ResponseEntity<String> responseSaveAffiliateAgeLessZero = affiliateController.saveAffiliate(affiliateInputMock);
		Assertions.assertEquals(HttpStatus.NOT_FOUND, responseSaveAffiliateAgeLessZero.getStatusCode());
		Assertions.assertEquals("La edad no puede ser un numero menor a cero.", responseSaveAffiliateAgeLessZero.getBody().toString());		
	}
	
	@Test
	void affiliateUpdateAffiliateNull() {
		affiliateService.setAffiliateRepository(affiliateRepositoryMock);
		
		AffiliateModelInput affiliateInput = new AffiliateModelInput();
		AffiliateModel affiliateMock = new AffiliateModel();
				
		Mockito.when(affiliateService.saveAffiliate(affiliateMock)).thenReturn(affiliateMock);
		
		ResponseEntity<String> responseUpdateAffiliateNull = affiliateController.updateAffiliate(affiliateInput);
		Assertions.assertEquals(HttpStatus.NOT_FOUND, responseUpdateAffiliateNull.getStatusCode());
		Assertions.assertEquals("Los campos correspondientes al nombre y/o el mail no pueden ser nulos.", responseUpdateAffiliateNull.getBody().toString());		
	}
	
	@Test
	void testUpdateAffiliateEmpty() {
		affiliateService.setAffiliateRepository(affiliateRepositoryMock);
		
		AffiliateModelInput affiliateInput = new AffiliateModelInput();
		affiliateInput.setName("  ");
		affiliateInput.setAge("1");
		affiliateInput.setMail("ejemplo@gmail.com");
		
		AffiliateModel affiliateMock = new AffiliateModel();
		affiliateMock.setName(affiliateInput.getName());
		affiliateMock.setAge(Integer.parseInt(affiliateInput.getAge()));
		affiliateMock.setMail(affiliateInput.getMail());
				
		Mockito.when(affiliateService.saveAffiliate(affiliateMock)).thenReturn(affiliateMock);
		
		ResponseEntity<String> responseUpdateAffiliateEmpty = affiliateController.updateAffiliate(affiliateInput);
		Assertions.assertEquals(HttpStatus.NOT_FOUND, responseUpdateAffiliateEmpty.getStatusCode());
		Assertions.assertEquals("Los campos correspondientes al nombre y/o el mail no pueden estar vacíos.", responseUpdateAffiliateEmpty.getBody().toString());		
	}
	
	@Test
	void testUpdateAffiliateIdNoParseLong() {
		affiliateService.setAffiliateRepository(affiliateRepositoryMock);
		
		AffiliateModelInput affiliateInput = new AffiliateModelInput();
		affiliateInput.setIdAffiliate("  ");
		affiliateInput.setName("Affiliate 1");
		affiliateInput.setAge("1");
		affiliateInput.setMail("ejemplo@gmail.com");
		
		ResponseEntity<String> responseUpdateAffiliateEmpty = affiliateController.updateAffiliate(affiliateInput);
		Assertions.assertEquals(HttpStatus.NOT_FOUND, responseUpdateAffiliateEmpty.getStatusCode());
		Assertions.assertEquals("El idAffiliate o Age ingresado(s) no es(son) del tipo requerido.", responseUpdateAffiliateEmpty.getBody().toString());		
	}
	
	@Test
	void testUpdateAffiliateAgeNoParseInt() {
		affiliateService.setAffiliateRepository(affiliateRepositoryMock);
		
		AffiliateModelInput affiliateInput = new AffiliateModelInput();
		affiliateInput.setIdAffiliate("1");
		affiliateInput.setName("Affiliate 1");
		affiliateInput.setAge("dfsf");
		affiliateInput.setMail("ejemplo@gmail.com");
		
		ResponseEntity<String> responseUpdateAffiliateEmpty = affiliateController.updateAffiliate(affiliateInput);
		Assertions.assertEquals(HttpStatus.NOT_FOUND, responseUpdateAffiliateEmpty.getStatusCode());
		Assertions.assertEquals("El idAffiliate o Age ingresado(s) no es(son) del tipo requerido.", responseUpdateAffiliateEmpty.getBody().toString());		
	}
	
	@Test
	void testUpdateAffiliateAgeLessZero() {
		affiliateService.setAffiliateRepository(affiliateRepositoryMock);
		
		AffiliateModelInput affiliateInput = new AffiliateModelInput();
		affiliateInput.setIdAffiliate("1");
		affiliateInput.setName("Affiliate 1");
		affiliateInput.setAge("-10");
		affiliateInput.setMail("ejemplo@gmail.com");
		
		ResponseEntity<String> responseUpdateAffiliateEmpty = affiliateController.updateAffiliate(affiliateInput);
		Assertions.assertEquals(HttpStatus.NOT_FOUND, responseUpdateAffiliateEmpty.getStatusCode());
		Assertions.assertEquals("La edad no puede ser un numero menor a cero.", responseUpdateAffiliateEmpty.getBody().toString());		
	}
	
	@Test
	void testUpdateAffiliateOk() {
		affiliateService.setAffiliateRepository(affiliateRepositoryMock);
		
		AffiliateModelInput affiliateInput = new AffiliateModelInput();
		affiliateInput.setIdAffiliate("1");
		affiliateInput.setName("Affiliate 1");
		affiliateInput.setAge("20");
		affiliateInput.setMail("ejemplo@gmail.com");
		
		AffiliateModel affiliateMock = new AffiliateModel();
		affiliateMock.setIdAffiliate(Long.parseLong(affiliateInput.getIdAffiliate()));
		affiliateMock.setName(affiliateInput.getName());
		affiliateMock.setAge(Integer.parseInt(affiliateInput.getAge()));
		affiliateMock.setMail(affiliateInput.getMail());
		
		Optional<AffiliateModel> affiliateOptionalMock = java.util.Optional.of(affiliateMock);		
		Mockito.when(affiliateService.getByIdAffiliate(1L)).thenReturn(affiliateOptionalMock);
		
		Mockito.when(affiliateService.saveAffiliate(affiliateMock)).thenReturn(affiliateMock);
		
		ResponseEntity<String> responseUpdateAffiliateOk = affiliateController.updateAffiliate(affiliateInput);
		Assertions.assertEquals(HttpStatus.CREATED, responseUpdateAffiliateOk.getStatusCode());
		Assertions.assertEquals("Affiliate con id 1 creado exitosamente: AffiliateModel [name=Affiliate 1, age=20, mail=ejemplo@gmail.com]", responseUpdateAffiliateOk.getBody().toString());
	}
	
	@Test
	void testUpdateAffiliateNotExists() {
		affiliateService.setAffiliateRepository(affiliateRepositoryMock);
		AffiliateModelInput affiliateInput = new AffiliateModelInput();
		affiliateInput.setIdAffiliate("1");
		affiliateInput.setName("Affiliate 1");
		affiliateInput.setAge("20");
		affiliateInput.setMail("ejemplo@gmail.com");
		Optional<AffiliateModel> affiliateOptionalMock = java.util.Optional.empty();
		
		Mockito.when(affiliateService.getByIdAffiliate(Long.parseLong("1"))).thenReturn(affiliateOptionalMock);
		
		ResponseEntity<?> responseUdateAffiliateNotIdExists = affiliateController.updateAffiliate(affiliateInput);
		Assertions.assertEquals(HttpStatus.NOT_FOUND, responseUdateAffiliateNotIdExists.getStatusCode());
		Assertions.assertEquals(String.class, responseUdateAffiliateNotIdExists.getBody().getClass());
		Assertions.assertEquals("El affiliate que se intenta actualizar no existe.", responseUdateAffiliateNotIdExists.getBody().toString());		
		
	}
	
	@Test
	void testDeleteAffiliateByIdAffiliateBlank() {
		ResponseEntity<?> responseDeleteAffiliateByIdAffiliateBlank = affiliateController.deleteAffiliateByIdAffiliate(" ");
		Assertions.assertEquals(HttpStatus.NO_CONTENT, responseDeleteAffiliateByIdAffiliateBlank.getStatusCode());
		Assertions.assertEquals("El idAffiliate no puede ser vacío.", responseDeleteAffiliateByIdAffiliateBlank.getBody().toString());		
	
	}
	
	@Test
	void testDeleteAffiliateByIdAffiliateNoParseLong() {
		ResponseEntity<?> responseDeleteAffiliateByIdAffiliateNoParseLong = affiliateController.deleteAffiliateByIdAffiliate("sffd");
		Assertions.assertEquals(HttpStatus.NO_CONTENT, responseDeleteAffiliateByIdAffiliateNoParseLong.getStatusCode());
		Assertions.assertEquals("El idAffiliate ingresado no es del tipo requerido.", responseDeleteAffiliateByIdAffiliateNoParseLong.getBody().toString());		
	
	}
	
	@Test
	void testDeleteAffiliateNotExists() {
		affiliateService.setAffiliateRepository(affiliateRepositoryMock);
		Optional<AffiliateModel> affiliateOptionalMock = java.util.Optional.empty();
		
		Mockito.when(affiliateService.getByIdAffiliate(Long.parseLong("1"))).thenReturn(affiliateOptionalMock);
		
		ResponseEntity<?> responseDeleteAffiliateNotIdExists = affiliateController.deleteAffiliateByIdAffiliate("1");
		Assertions.assertEquals(HttpStatus.NO_CONTENT, responseDeleteAffiliateNotIdExists.getStatusCode());
		Assertions.assertEquals(String.class, responseDeleteAffiliateNotIdExists.getBody().getClass());
		Assertions.assertEquals("El Affiliate que se intenta eliminar no existe.", responseDeleteAffiliateNotIdExists.getBody().toString());		
		
	}
	
	@Test
	void testDeleteAffiliateOk() {
		affiliateService.setAffiliateRepository(affiliateRepositoryMock);
		
		AffiliateModelInput affiliateInput = new AffiliateModelInput();
		affiliateInput.setIdAffiliate("100");
		affiliateInput.setName("Affiliate 1");
		affiliateInput.setAge("20");
		affiliateInput.setMail("ejemplo@gmail.com");
		
		AffiliateModel affiliateMock = new AffiliateModel();
		affiliateMock.setIdAffiliate(Long.parseLong(affiliateInput.getIdAffiliate()));
		affiliateMock.setName(affiliateInput.getName());
		affiliateMock.setAge(Integer.parseInt(affiliateInput.getAge()));
		affiliateMock.setMail(affiliateInput.getMail());
		
		Optional<AffiliateModel> affiliateOptionalMock = java.util.Optional.of(affiliateMock);	
		Mockito.when(affiliateService.getByIdAffiliate(Long.parseLong("100"))).thenReturn(affiliateOptionalMock);
		Mockito.doNothing().when(affiliateRepositoryMock).deleteById(100L);;
		
		ResponseEntity<String> responseDeletedAffiliateOk = affiliateController.deleteAffiliateByIdAffiliate("100");
		Assertions.assertEquals(HttpStatus.OK, responseDeletedAffiliateOk.getStatusCode());
		Assertions.assertEquals("Affiliate con id 100 eliminado exitosamente: Optional[AffiliateModel [name=Affiliate 1, age=20, mail=ejemplo@gmail.com]]", responseDeletedAffiliateOk.getBody().toString());
	}
	
}
