package org.vaadin.presentation.views;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.backend.CustomerService;
import org.vaadin.backend.domain.Customer;
import org.vaadin.cdiviewmenu.ViewMenuItem;
import org.vaadin.cdiviewmenu.ViewMenuUI;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.ejt.vaadin.loginform.DefaultVerticalLoginForm;
import com.ejt.vaadin.loginform.LoginForm.LoginEvent;
import com.ejt.vaadin.loginform.LoginForm.LoginListener;
import com.vaadin.cdi.CDIView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.themes.ValoTheme;

/*
 * A very simple view that just displays an "about text". The view also has
 * a button to reset the demo date in the database.
 */
@CDIView("")
@ViewMenuItem(icon = FontAwesome.SIGN_IN, title = "Login-Logout",  order = ViewMenuItem.END)
public class LoginView extends MVerticalLayout implements View {

    @Inject
    private CustomerService service;

    @Inject
	private UserInfo user;


    @PostConstruct
    void init() {
	    ViewMenuUI.getMenu().setActive("customers");
    	final DefaultVerticalLoginForm loginForm = new DefaultVerticalLoginForm();
    	loginForm.addLoginListener(new LoginListener() {
    	    @Override
    	    public void onLogin(LoginEvent event) {


    	        System.err.println(
    	                "Logged in with user name " + event.getUserName() +
    	                        " and password of length " + event.getPassword().length());
    	        Customer cust = service.findByMembershipNo(event.getUserName());


    	        if(cust != null) {
    	        	System.err.println("user password is " + cust.getPassword() );
    	        	if (cust.getPassword().equals(event.getPassword()))  {

    	        			user.setUser(cust);
    	        			System.err.println("Password OK");

    	        			ViewMenuUI.getMenu().navigateTo(AboutView.class);
    	        	} else {
	        			System.err.println("Password NG!");
    	        		loginForm.clear();
    	        	}

    	        } else {
    	        	System.err.println("ユーザーが見つかりません");
    	        	loginForm.clear();
    	        }


    	    }
    	});

        add(loginForm);


        setMargin(new MarginInfo(false, true, true, true));
        setStyleName(ValoTheme.LAYOUT_CARD);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
    	user.setUser(null);

    }
}
