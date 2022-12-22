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
@Table(name  = "test")
public class TestModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long idTest;

    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String description;
    
    @OneToMany(mappedBy = "test", cascade = (CascadeType.ALL))
    private Set<AppoinmentModel> appoinments;
    
    public Long getIdTest() {
        return idTest;
    }
    
    public void setIdTest(Long idTest) {
        this.idTest = idTest;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

	@Override
	public String toString() {
		return "TestModel [idTest=" + idTest + ", name=" + name + ", description=" + description + "]";
	}
}

