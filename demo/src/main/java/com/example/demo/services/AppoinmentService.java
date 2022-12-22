package com.example.demo.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.models.AffiliateModel;
import com.example.demo.models.AppoinmentModel;
import com.example.demo.repositories.AppoinmentRepository;

@Service
public class AppoinmentService {
	@Autowired
	AppoinmentRepository appoinmentRepository;

	public void setAppoinmentRepository(AppoinmentRepository appoinmentRepository) {
		this.appoinmentRepository = appoinmentRepository;
	}

	public ArrayList<AppoinmentModel> getAppoinments(){
		return (ArrayList<AppoinmentModel>) appoinmentRepository.findAll();
	}
	
	public Optional<AppoinmentModel> getByIdAppoinment(long idAppoinment){
		return appoinmentRepository.findById(idAppoinment);
	}
	
	public AppoinmentModel saveAppoinment(AppoinmentModel appoinment) {
		appoinmentRepository.save(appoinment);
		return appoinment;
	}

	public void deleteAppoinment(Long idAppoinment) {
		appoinmentRepository.deleteById(idAppoinment);
	}
	
	public ArrayList<AppoinmentModel> getByDate(Date date){
		return appoinmentRepository.findByDateOrderByAffiliateAsc(date);
	}
	
	public ArrayList<AppoinmentModel> getByAffiliate(AffiliateModel affiliateModel){
		return appoinmentRepository.findByAffiliateOrderByDateAsc(affiliateModel);
	}
}
