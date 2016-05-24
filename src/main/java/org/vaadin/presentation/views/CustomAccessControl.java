package org.vaadin.presentation.views;

import javax.annotation.Priority;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;

import com.vaadin.cdi.access.AccessControl;

@Alternative
@Priority(1000)
public class CustomAccessControl extends AccessControl {

    @Inject
    private UserInfo userInfo;

    @Override
    public boolean isUserSignedIn() {
        return userInfo.getUser() != null;
    }

    @Override
    public boolean isUserInRole(String role) {
       System.err.println("checking role\n");
        if (isUserSignedIn()) {
          	System.err.println("Signed In OK!\n");
          	System.err.println("role=" + role);
           	System.err.println("getRoles=" + userInfo.getRoles().toString());

            for (String userRole : userInfo.getRoles()) {
            	System.err.println("UserRole= "+userRole);
                if (role.equals(userRole)) {
                	System.err.println("check OK");
                    return true;
                }
            }
        }
    	System.err.println("check False");
        return false;
    }

    public String String () {
        if (isUserSignedIn()) {
            return userInfo.getUser().getName();
        }
        return null;
    }

	@Override
	public String getPrincipalName() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}
