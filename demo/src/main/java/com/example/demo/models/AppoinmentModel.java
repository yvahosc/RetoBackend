package com.example.demo.models;


import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name  = "appoinment")
public class AppoinmentModel {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long idAppoinment;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date date;
    
    @Column(nullable = false)
    @Temporal(TemporalType.TIME)
    private Date hour;
    
    @ManyToOne
    @JoinColumn(name = "idTest", referencedColumnName= "idTest", nullable = false)
    private TestModel test;
    
    @ManyToOne
    @JoinColumn(name = "idAffiliate", referencedColumnName= "idAffiliate", nullable = false)
    private AffiliateModel affiliate;

	public Long getIdAppoinment() {
		return idAppoinment;
	}

	public void setIdAppoinment(Long idAppoinment) {
		this.idAppoinment = idAppoinment;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getHour() {
		return hour;
	}

	public void setHour(Date hour) {
		this.hour = hour;
	}

	public TestModel getTest() {
		return test;
	}

	public void setTest(TestModel test) {
		this.test = test;
	}

	public AffiliateModel getAffiliate() {
		return affiliate;
	}

	public void setAffiliate(AffiliateModel affiliate) {
		this.affiliate = affiliate;
	}

	@Override
	public String toString() {
		return "AppoinmentModel [date=" + getDate() + ", hour=" + getHour() + ", idTest=" + test.getIdTest()
				+ ", idAffiliate=" + affiliate.getIdAffiliate() + "]";
	}  
	
	
}
