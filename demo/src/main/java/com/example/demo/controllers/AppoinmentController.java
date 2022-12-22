package com.example.demo.controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.AffiliateModel;
import com.example.demo.models.AppoinmentModel;
import com.example.demo.models.AppoinmentModelInput;
import com.example.demo.models.TestModel;
import com.example.demo.services.AffiliateService;
import com.example.demo.services.AppoinmentService;
import com.example.demo.services.TestService;

@RestController
@RequestMapping("/api/controller/appoinment")
public class AppoinmentController {

	@Autowired
	AppoinmentService appoinmentService;
	@Autowired
	TestService testService;
	@Autowired
	AffiliateService affiliateService;
	
	@GetMapping()
	public ResponseEntity<ArrayList<AppoinmentModel>> getAppoinment(){
		ArrayList<AppoinmentModel> appoinmentsList = appoinmentService.getAppoinments();
		if (appoinmentsList.isEmpty()) {
			return new ResponseEntity<ArrayList<AppoinmentModel>>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<ArrayList<AppoinmentModel>>(appoinmentsList, HttpStatus.OK);
		}
	}
	
	@GetMapping(path = "/{idAppoinment}")
	public ResponseEntity<?> getAppoinmentByIdAppoinment(@PathVariable("idAppoinment") String idAppoinmentString){
		Long idAppoinment;
		try {
			if (idAppoinmentString.isBlank()) {
				throw new Exception("El idAppoinment ingresado no puede ser vacío.");
			}else {
				try {
					idAppoinment = Long.parseLong(idAppoinmentString);
				}catch(Exception err) {
					throw new Exception("El idAppoinment ingresado no es del tipo requerido.");
				}
				Optional<AppoinmentModel> appoinment = this.appoinmentService.getByIdAppoinment(idAppoinment);
				if (appoinment.isPresent()) {
					return new ResponseEntity<Optional<AppoinmentModel>>(appoinment, HttpStatus.OK);
				}else {
					throw new Exception("El appoinment que se intenta obtener no existe.");
				}
			}
		} catch(Exception err) {
			return new ResponseEntity<String>(err.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping()
	public ResponseEntity<String> saveAppoinment(@RequestBody AppoinmentModelInput appoinmentInput) { 
		AppoinmentModel appoinment = new AppoinmentModel();
		TestModel testModel = new TestModel();
		AffiliateModel affiliateModel = new AffiliateModel();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		try {
			if (appoinmentInput.getDate() == null || appoinmentInput.getHour() == null || appoinmentInput.getIdTest() == null || appoinmentInput.getIdAffiliate() == null) {
				throw new Exception("Los campos correspondientes a date, hour, idTest y idAffiliate no pueden ser nulos.");
			}else if (appoinmentInput.getDate().isBlank() || appoinmentInput.getHour().isBlank() || appoinmentInput.getIdTest().isBlank() || appoinmentInput.getIdAffiliate().isBlank()) {
				throw new Exception("Los campos correspondientes a date, hour, idTest y idAffiliate no pueden estar vacíos.");
			}else {
				try {
					appoinment.setDate(dateFormat.parse(appoinmentInput.getDate()));
					appoinment.setHour(timeFormat.parse(appoinmentInput.getHour()));
					testModel.setIdTest(Long.parseLong(appoinmentInput.getIdTest()));
					affiliateModel.setIdAffiliate(Long.parseLong(appoinmentInput.getIdAffiliate()));
				}catch(Exception err) {
					throw new Exception("La(s) variable(s) date, hour, idTest y/o idAffiliate ingresada(s) no es(son) del tipo requerido.");
				}
				appoinment.setTest(testModel);
				appoinment.setAffiliate(affiliateModel);
				Optional<TestModel> existsTest = testService.getByIdTest(appoinment.getTest().getIdTest());
				Optional<AffiliateModel> existsAffiliate = affiliateService.getByIdAffiliate(appoinment.getAffiliate().getIdAffiliate());
				if (existsTest.isPresent() && existsAffiliate.isPresent()) {
					appoinmentService.saveAppoinment(appoinment);
					return new ResponseEntity<String>("Appoinment creado exitosamente: " + appoinment.toString(), HttpStatus.CREATED);
				}else{
					throw new Exception("El Appoinment no puede ser creado debido a que el idTest o el idAffiliate asociados no existen.");
				}
			}
		}catch(Exception err) {
			return new ResponseEntity<String>(err.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
	
	@PutMapping()
	public ResponseEntity<String> updateAppoinment(@RequestBody AppoinmentModelInput appoinmentInput) {
		AppoinmentModel appoinment = new AppoinmentModel();
		TestModel testModel = new TestModel();
		AffiliateModel affiliateModel = new AffiliateModel();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		try {			
			if (appoinmentInput.getIdAppoinment() == null || appoinmentInput.getDate() == null || appoinmentInput.getHour() == null || appoinmentInput.getIdTest() == null || appoinmentInput.getIdAffiliate() == null) {
				throw new Exception("Los campos correspondientes a idAppoinment, date, hour, idTest y/o idAffiliate no pueden ser nulos.");
			}else if (appoinmentInput.getIdAppoinment().isBlank() || appoinmentInput.getDate().isBlank() || appoinmentInput.getHour().isBlank() || appoinmentInput.getIdTest().isBlank() || appoinmentInput.getIdAffiliate().isBlank()) {
				throw new Exception("Los campos correspondientes a idAppoinment, date, hour, idTest y idAffiliate no pueden estar vacíos.");
			}else {
				try {
					appoinment.setIdAppoinment(Long.parseLong(appoinmentInput.getIdAppoinment()));
					appoinment.setDate(dateFormat.parse(appoinmentInput.getDate()));
					appoinment.setHour(timeFormat.parse(appoinmentInput.getHour()));
					testModel.setIdTest(Long.parseLong(appoinmentInput.getIdTest()));
					affiliateModel.setIdAffiliate(Long.parseLong(appoinmentInput.getIdAffiliate()));
				}catch(Exception err) {
					throw new Exception("La(s) variable(s) idAppoinment, date, hour, idTest y/o idAffiliate ingresada(s) no es(son) del tipo requerido.");
				}
				
				appoinment.setTest(testModel);
				appoinment.setAffiliate(affiliateModel);
				Optional<AppoinmentModel> existsAppoinment = appoinmentService.getByIdAppoinment(appoinment.getIdAppoinment());
				if (existsAppoinment.isPresent()) {
					Optional<TestModel> existsTest = testService.getByIdTest(appoinment.getTest().getIdTest());
					Optional<AffiliateModel> existsAffiliate = affiliateService.getByIdAffiliate(appoinment.getAffiliate().getIdAffiliate());
					if (existsTest.isPresent() && existsAffiliate.isPresent()) {
						appoinmentService.saveAppoinment(appoinment);
						return new ResponseEntity<String>("Appoinment con id " + appoinment.getIdAppoinment() + " actualizado exitosamente: " + appoinment.toString(), HttpStatus.CREATED);
					}else{
						throw new Exception("El Appoinment no puede ser actualizado debido a que el idTest o el idAffiliate asociados no existen.");
					}
				}else{
					throw new Exception("El Appoinment que se intenta actualizar no existe.");
				}
			}
		}catch(Exception err) {
			return new ResponseEntity<String>(err.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping( path = "/{idAppoinment}")
	public ResponseEntity<String> deleteAppoinmentByIdAppoinment(@PathVariable("idAppoinment") String idAppoinmentString){
		Long idAppoinment;
		try {
			if (idAppoinmentString.isBlank()) {
				throw new Exception("El idAppoinment no puede ser vacío.");
			}else {
				try {
					idAppoinment = Long.parseLong(idAppoinmentString);
				}catch(Exception err) {
					throw new Exception("El idAppoinment ingresado no es del tipo requerido.");
				}
				Optional<AppoinmentModel> existsAppoinment = this.appoinmentService.getByIdAppoinment(idAppoinment);
				if (existsAppoinment.isPresent()) {
					appoinmentService.deleteAppoinment(idAppoinment);
					return new ResponseEntity<String>("El appoinment con id " + idAppoinment + " eliminado exitosamente: " + existsAppoinment.toString(), HttpStatus.OK);
				}else{
					throw new Exception("El appoinment que se intenta eliminar no existe.");
				}
			}
		}catch(Exception err) {
			return new ResponseEntity<String>(err.getMessage(), HttpStatus.NO_CONTENT);//El status podría ser bad request.
		}
	}
	
	@GetMapping("/searchByDate")
	public ResponseEntity<?> getAppoinmentByDate(@RequestParam("dateAppoinment") String dateAppoinmentString) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date dateAppoinment;
		try {
			if (dateAppoinmentString.isBlank()) {
				throw new Exception("El dateAppoinment no puede ser vacío.");
			}else {
				try {
					dateAppoinment = dateFormat.parse(dateAppoinmentString);
				}catch(Exception err) {
					throw new Exception("El dateAppoinment ingresado no es del tipo requerido.");
				}
				
				ArrayList<AppoinmentModel> appoinmentByDate = this.appoinmentService.getByDate(dateAppoinment);
				if (appoinmentByDate.isEmpty()) {
					throw new Exception("No existen appoinments asignados para la fecha " + dateAppoinmentString);
				}else{
					return new ResponseEntity<ArrayList<AppoinmentModel>>(appoinmentByDate, HttpStatus.OK);
				}
			}
		}catch(Exception err) {
			return new ResponseEntity<String>(err.getMessage(), HttpStatus.NOT_FOUND);
		} 
	}
	
	@GetMapping("/searchByIdAffiliate")
	public ResponseEntity<?> getByAffiliate(@RequestParam("idAffiliate") String idAffiliateString){
		Long idAffiliate;
		try {
			if (idAffiliateString.isBlank()) {
				throw new Exception("El idAffiliate no puede ser vacío.");
			}else {
				try {
					idAffiliate = Long.parseLong(idAffiliateString);
				}catch(Exception err) {
					throw new Exception("El idAffiliate ingresado no es del tipo requerido.");
				}
				Optional<AffiliateModel> affiliate = this.affiliateService.getByIdAffiliate(idAffiliate);
				if (affiliate.isPresent()) {
					AffiliateModel affiliateModel = new AffiliateModel();
					affiliateModel.setIdAffiliate(affiliate.get().getIdAffiliate());
					affiliateModel.setName(affiliate.get().getName());
					affiliateModel.setAge(affiliate.get().getAge());
					affiliateModel.setMail(affiliate.get().getMail());
					ArrayList<AppoinmentModel> appoinmentByAffiliate = this.appoinmentService.getByAffiliate(affiliateModel);
					return new ResponseEntity<ArrayList<AppoinmentModel>>(appoinmentByAffiliate, HttpStatus.OK);
				}else{
					throw new Exception("El Affiliate por el que se intenta filtrar no existe.");
				}
			}
		}catch(Exception err) {
			return new ResponseEntity<String>(err.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
}
