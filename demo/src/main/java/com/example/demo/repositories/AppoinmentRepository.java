package com.example.demo.repositories;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.AffiliateModel;
import com.example.demo.models.AppoinmentModel;

@Repository
public interface AppoinmentRepository extends CrudRepository<AppoinmentModel, Long>{
	public abstract ArrayList<AppoinmentModel> findByDateOrderByAffiliateAsc(Date date);
	public abstract ArrayList<AppoinmentModel> findByAffiliateOrderByDateAsc(AffiliateModel affiliateModel);
}

