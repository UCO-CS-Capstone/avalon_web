package edu.uco.avalon;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Named
@RequestScoped
public class AuthBean implements Serializable {

    @Inject
    private LoginBean loginBean;

    private boolean currentPageAllowed;

    @PostConstruct
    public void init() {
        String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
        List publicList = Arrays.asList("/index.xhtml", "/register.xhtml");
        currentPageAllowed = (publicList.contains(viewId) || loginBean.isLoggedIn());
    }

    public boolean isCurrentPageAllowed() {
        return currentPageAllowed;
    }

    public void setCurrentPageAllowed(boolean currentPageAllowed) {
        this.currentPageAllowed = currentPageAllowed;
    }
}
