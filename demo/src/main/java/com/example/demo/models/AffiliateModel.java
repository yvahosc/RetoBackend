package com.example.demo.models;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name  = "affiliate")
public class AffiliateModel {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long idAffiliate;

	@Column(nullable = false)
    private String name;
	
	@Column(nullable = false)
    private int age;
	
	@Column(nullable = false)
    private String mail;
	
	@OneToMany(mappedBy = "affiliate", cascade = (CascadeType.ALL))
    private Set<AppoinmentModel> appoinments;
    
	public Long getIdAffiliate() {
		return idAffiliate;
	}
	public void setIdAffiliate(Long idAffiliate) {
		this.idAffiliate = idAffiliate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	@Override
	public String toString() {
		return "AffiliateModel [name=" + name + ", age=" + age + ", mail=" + mail + "]";
	}
	
	
}
