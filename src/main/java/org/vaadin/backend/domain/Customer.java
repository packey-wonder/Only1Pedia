package org.vaadin.backend.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Version;

/**
 * A standard JPA entity, like in any other Java application.
 */
@NamedQueries({
        @NamedQuery(name="Customer.findAll",
                query="SELECT c FROM Customer c"),
        @NamedQuery(name="Customer.findByName",
                query="SELECT c FROM Customer c WHERE LOWER(c.firstName) LIKE :filter OR LOWER(c.lastName) LIKE :filter"),
})
@Entity
public class Customer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Version int version;

    private String firstName;

    private String lastName;

    private String password;

    private CustomerStatus status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Get the value of status
     *
     * @return the value of status
     */
    public CustomerStatus getStatus() {
        return status;
    }

    /**
     * Set the value of status
     *
     * @param status new value of status
     */
    public void setStatus(CustomerStatus status) {
        this.status = status;
    }

    /**
     * Get the value of lastName
     *
     * @return the value of lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Set the value of lastName
     *
     * @param lastName new value of lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Get the value of firstName
     *
     * @return the value of firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Set the value of firstName
     *
     * @param firstName new value of firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Get the value of firstName
     *
     * @return the value of firstName
     */
    public String getName() {
        return lastName + " " + firstName;
    }


    public boolean isPersisted() {
        return id > 0;
    }

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


}
