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

import com.example.demo.models.AffiliateModel;
import com.example.demo.models.AffiliateModelInput;
import com.example.demo.services.AffiliateService;

@RestController
@RequestMapping("/api/controller/affiliate")
public class AffiliatesController {
	
	@Autowired
	AffiliateService affiliateService;
	
	@GetMapping()
	public ResponseEntity<ArrayList<AffiliateModel>> getAffiliate(){
		ArrayList<AffiliateModel> affiliateList = affiliateService.getAffiliates();
		if (affiliateList.isEmpty()) {
			return new ResponseEntity<ArrayList<AffiliateModel>>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<ArrayList<AffiliateModel>>(affiliateList, HttpStatus.OK);
		}
	}
	
	@GetMapping( path = "/{idAffiliate}")
	public ResponseEntity<?> getAffiliateByIdAffiliate(@PathVariable("idAffiliate") String idAffiliateString){
		Long idAffiliate;
		try {
			if(idAffiliateString.isBlank()) {
				throw new Exception("El idAffiliate ingresado no puede ser vacío.");
			} else {
				try {
					idAffiliate = Long.parseLong(idAffiliateString);
				}catch(Exception err) {
					throw new Exception("El idAffiliate ingresado no es del tipo requerido.");
				}
			}
			Optional<AffiliateModel> affiliate = this.affiliateService.getByIdAffiliate(idAffiliate);
			if (affiliate.isPresent()) {
				return new ResponseEntity<Optional<AffiliateModel>>(affiliate, HttpStatus.OK);
			}else {
				throw new Exception("El affiliate que se intenta obtener no existe.");
			}
		}catch(Exception err) {
			return new ResponseEntity<String>(err.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping()
	public ResponseEntity<String> saveAffiliate(@RequestBody AffiliateModelInput affiliateInput) {
		AffiliateModel affiliate = new AffiliateModel();
		try {
			if (affiliateInput.getName() == null || affiliateInput.getAge() == null || affiliateInput.getMail() == null) {
				throw new Exception("Los campos correspondientes a name, age y/o mail no pueden ser nulos.");
			}else if (affiliateInput.getName().isBlank() || affiliateInput.getAge().isBlank() || affiliateInput.getMail().isBlank()) {
				throw new Exception("Los campos correspondientes a name, age y/o mail no pueden estar vacíos.");
			}else {
				try {
					affiliate.setName(affiliateInput.getName());
					affiliate.setAge(Integer.parseInt(affiliateInput.getAge()));
					affiliate.setMail(affiliateInput.getMail());
				}catch(Exception err) {
					throw new Exception("La(s) variable(s) name, age y/o mail ingresada(s) no es(son) del tipo requerido.");
				}
				if (affiliate.getAge() < 0) {
					throw new Exception("La edad no puede ser un numero menor a cero.");
				}else {
					affiliateService.saveAffiliate(affiliate);
					return new ResponseEntity<String>("Affiliate creado exitosamente: " + affiliate.toString(), HttpStatus.CREATED);
				}
			}
		}catch(Exception err) {
			return new ResponseEntity<String>(err.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
	
	@PutMapping()
	public ResponseEntity<String> updateAffiliate(@RequestBody AffiliateModelInput affiliateInput) {
		AffiliateModel affiliate = new AffiliateModel();
		affiliate.setName(affiliateInput.getName());
		affiliate.setMail(affiliateInput.getMail());
		try {
			if (affiliate.getName() == null || affiliate.getMail() == null) {
				throw new Exception("Los campos correspondientes al nombre y/o el mail no pueden ser nulos.");
			}else if (affiliate.getName().isBlank() || affiliate.getMail().isBlank()) {
				throw new Exception("Los campos correspondientes al nombre y/o el mail no pueden estar vacíos.");
			}else {
				try {
					affiliate.setIdAffiliate(Long.parseLong(affiliateInput.getIdAffiliate()));
					affiliate.setAge(Integer.parseInt(affiliateInput.getAge()));
				}catch(Exception err) {
					throw new Exception("El idAffiliate o Age ingresado(s) no es(son) del tipo requerido.");
				}
				if (affiliate.getAge() < 0) {
					throw new Exception("La edad no puede ser un numero menor a cero.");
				}else {
					Optional<AffiliateModel> existsAffiliate = affiliateService.getByIdAffiliate(affiliate.getIdAffiliate());
					if (existsAffiliate.isPresent()) {
						affiliateService.saveAffiliate(affiliate);
						return new ResponseEntity<String>("Affiliate con id " + affiliate.getIdAffiliate() + " creado exitosamente: " + affiliate.toString(), HttpStatus.CREATED);
					}else{
						throw new Exception("El affiliate que se intenta actualizar no existe.");
					}
				}
			}
		}catch(Exception err) {
			return new ResponseEntity<String>(err.getMessage(), HttpStatus.NOT_FOUND);
		}		
	}
	
	@DeleteMapping( path = "/{idAffiliate}")
	public ResponseEntity<String> deleteAffiliateByIdAffiliate(@PathVariable("idAffiliate") String idAffiliateString){
		Long idAffiliate;
		try {
			if(idAffiliateString.isBlank()) {
				throw new Exception("El idAffiliate no puede ser vacío.");
			}else {
				try {
					idAffiliate = Long.parseLong(idAffiliateString);
				}catch(Exception err){
					throw new Exception("El idAffiliate ingresado no es del tipo requerido.");
				}
				Optional<AffiliateModel> existsAffiliate = this.affiliateService.getByIdAffiliate(idAffiliate);
				if (existsAffiliate.isPresent()) {
					affiliateService.deleteAffiliate(idAffiliate);
					return new ResponseEntity<String>("Affiliate con id " + idAffiliate + " eliminado exitosamente: " + existsAffiliate.toString(), HttpStatus.OK);
				}else{
					throw new Exception("El Affiliate que se intenta eliminar no existe.");
				}
			}
		}catch(Exception err) {
			return new ResponseEntity<String>(err.getMessage(), HttpStatus.NO_CONTENT);//El status podría ser bad request.
		}
	}
}
