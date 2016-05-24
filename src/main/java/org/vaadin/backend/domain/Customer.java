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
        		query="SELECT c FROM Customer c WHERE LOWER(c.membershipNo) LIKE :filter "),
        @NamedQuery(name="Customer.findByMembershipNo",
        		query="SELECT c FROM Customer c WHERE LOWER(c.membershipNo) LIKE :filter "),
})
@Entity
public class Customer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Version int version;

    private String membershipNo;

    private String password;

    //    管理者か？
    private ManagerClass managerClass;

    //    学徒種別
    private MemberStatus status;

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
    public MemberStatus getStatus() {
        return status;
    }

    /**
     * Set the value of status
     *
     * @param status new value of status
     */
    public void setStatus(MemberStatus status) {
        this.status = status;
    }

    /**
     * Get the value of membershipNo
     *
     * @return the value of membershipNo
     */
    public String getMembershipNo() {
        return membershipNo;
    }

    /**
     * Set the value of membershipNo
     *
     * @param membershipNo new value of membershipNo
     */
    public void setMembershipNo(String firstName) {
        this.membershipNo = firstName;
    }

    /**
     * Get the value of membershipNo
     *
     * @return the value of membershipNo
     */
    public String getName() {
        return membershipNo;
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

	/**
	 * @return managerClass
	 */
	public ManagerClass getManagerClass() {
		return managerClass;
	}

	/**
	 * @param managerClass セットする managerClass
	 */
	public void setManagerClass(ManagerClass managerClass) {
		this.managerClass = managerClass;
	}


}
