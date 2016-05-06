package org.vaadin.presentation.views;

import javax.enterprise.inject.Alternative;
import javax.annotation.Priority;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Specializes;
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

    @Override
    public String getPrincipalName() {
        if (isUserSignedIn()) {
            return userInfo.getUser().getEmail();
        }
        return null;
    }

}
