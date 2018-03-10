package edu.uco.avalon;

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

    private String viewId;

    /**
     * Any pages which a logged in user should not see
     */
    private List unauthenticatedPages = Arrays.asList("/index.xhtml", "/register.xhtml");

    /**
     * Any pages which can be viewed publicly
     */
    private List publicPages = Arrays.asList("/contact.xhtml", "/index.xhtml", "/register.xhtml");

    /**
     * If the current page is allowed by the current user
     */
    private boolean currentPageAllowed;

    @PostConstruct
    public void init() {
        viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
        currentPageAllowed = (publicPages.contains(viewId) || loginBean.isLoggedIn());
    }

    /**
     * If the user is not logged in and they should not be here, redirects to the login page.
     * Else if the user is logged in and at the login page, redirect them into the site.
     *
     * @throws IOException
     */
    public void redirectIfUnsafe() throws IOException {
        if (!currentPageAllowed) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/index.xhtml");
        } else if (loginBean.isLoggedIn() && unauthenticatedPages.contains(viewId)) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/dashboard.xhtml");
        }
    }

    public boolean isCurrentPageAllowed() {
        return currentPageAllowed;
    }

    public void setCurrentPageAllowed(boolean currentPageAllowed) {
        this.currentPageAllowed = currentPageAllowed;
    }
}
