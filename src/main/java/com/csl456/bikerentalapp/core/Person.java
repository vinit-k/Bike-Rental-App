package com.csl456.bikerentalapp.core;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.validator.constraints.Email;

import java.util.Objects;

import javax.persistence.*;

@Entity
@Table(name = "person")
@NamedQueries({
                      @NamedQuery(name = "Person.findAll", query = "SELECT P FROM Person P"),
                      @NamedQuery(name = "Person.findById", query = "SELECT P FROM Person P WHERE P.id = :id")
              })
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope = Person.class)
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private long contactNumber;

    @Email
    @Column(nullable = false)
    private String email;


    public Person() {

    }

    public Person(String name, Long contactNumber, String email) {
        this.name = name;
        this.contactNumber = contactNumber;
        this.email = email;
    }

  

    @Override
	public int hashCode() {
		return Objects.hash(contactNumber, email, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Person other = (Person) obj;
		return contactNumber == other.contactNumber && Objects.equals(email, other.email)
				&& Objects.equals(name, other.name);
	}

	public long getContactNumber() { return contactNumber; }

    public void setContactNumber(long contactNumber) { this.contactNumber = contactNumber; }


   

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

}
