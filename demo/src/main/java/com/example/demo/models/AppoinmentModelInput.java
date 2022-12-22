package com.example.demo.models;

public class AppoinmentModelInput {
	
    private String idAppoinment;
    private String date;
    private String hour;
    private String idTest;
    private String idAffiliate;
    
	public String getIdAppoinment() {
		return idAppoinment;
	}
	public void setIdAppoinment(String idAppoinment) {
		this.idAppoinment = idAppoinment;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getHour() {
		return hour;
	}
	public void setHour(String hour) {
		this.hour = hour;
	}
	public String getIdTest() {
		return idTest;
	}
	public void setIdTest(String idTest) {
		this.idTest = idTest;
	}
	public String getIdAffiliate() {
		return idAffiliate;
	}
	public void setIdAffiliate(String idAffiliate) {
		this.idAffiliate = idAffiliate;
	}   
}
