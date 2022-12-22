package com.example.demo.services;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.models.AffiliateModel;
import com.example.demo.repositories.AffiliateRepository;

@Service
public class AffiliateService {
	@Autowired
	AffiliateRepository affiliateRepository;

	public void setAffiliateRepository(AffiliateRepository affiliateRepository) {
		this.affiliateRepository = affiliateRepository;
	}

	public ArrayList<AffiliateModel> getAffiliates(){
		return (ArrayList<AffiliateModel>) affiliateRepository.findAll();
	}
	
	public AffiliateModel saveAffiliate(AffiliateModel affiliate) {
		affiliateRepository.save(affiliate);
		return affiliate;
	}
	
	public Optional<AffiliateModel> getByIdAffiliate(long idAfiliate){
		return affiliateRepository.findById(idAfiliate);
	}
	
	public void deleteAffiliate(Long idAfiliate) {
		affiliateRepository.deleteById(idAfiliate);
	}
		
}
