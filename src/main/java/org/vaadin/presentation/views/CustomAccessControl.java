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
            for (String userRole : userInfo.getRoles()) {
                if (role.equals(userRole)) {
                    return true;
                }
            }
        }
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
