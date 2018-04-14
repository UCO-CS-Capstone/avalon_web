package edu.uco.avalon;

import org.omnifaces.util.Faces;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Makes sure that the logged in user has the
 * access for the current page they are tyring
 * to view.
 */
@Named
@RequestScoped
public class AuthBean implements Serializable {

    @Inject
    private LoginBean loginBean;

    private String viewId = Faces.getViewId();

    /**
     * Any pages which a logged in user should not see
     */
    private static final List unauthenticatedPages = Arrays.asList("/index.xhtml", "/register.xhtml");

    /**
     * Any pages which can be viewed publicly
     */
    private static final List publicPages = Arrays.asList("/contact.xhtml");

    /**
     * If the current page is allowed by the current user
     */
    private boolean currentPageAllowed = false;

    @PostConstruct
    public void init() {
        checkAuthentication();
    }

    /**
     * Check the user's login status and make sure they are where they are supossed to be.
     */
    private void checkAuthentication() {
        if (loginBean.isLoggedIn() || loginBean.checkPersistentSession()) {
            // Logged in
            if (inUnauthenticatedPages()) {
                // At login or register pages, redirect into site
                Faces.redirect("dashboard");
            } else {
                // Allow content to display
                currentPageAllowed = true;
            }
        } else {
            // Not logged in
            if (inUnauthenticatedPages() || inPublicPages()) {
                // At publicly viewable page
                currentPageAllowed = true;
            }
        }
    }

    public boolean inPublicPages() {
        return publicPages.contains(viewId);
    }

    public boolean inUnauthenticatedPages() {
        return unauthenticatedPages.contains(viewId);
    }

    public boolean isCurrentPageAllowed() {
        return currentPageAllowed;
    }

    public void setCurrentPageAllowed(boolean currentPageAllowed) {
        this.currentPageAllowed = currentPageAllowed;
    }
}
