package org.vaadin.presentation.views;

import java.io.Serializable;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.vaadin.backend.domain.Customer;

import com.vaadin.cdi.UIScoped;

@UIScoped
public class UserInfo implements Serializable {
    private Customer user;

	private List<String> roles = new LinkedList<String>();

	public List<String> getRoles() {
        return roles;
    }	
    
    
    public UserInfo() {
        this.user = null;
    }

    public Customer getUser() {
        return user;
    }

    public String getName() {
        if (user == null) {
            return "anonymous user";
        } else {
            return user.getEmail();
        }
    }

    public void setUser(Customer user) {
        this.user = user;
        roles.clear();
        if (user != null) {
            roles.add(user.getRole());
          
        }
        
        
    }
}