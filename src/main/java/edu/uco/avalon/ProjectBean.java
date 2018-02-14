package edu.uco.avalon;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Named(value = "projectBean")
@SessionScoped
public class ProjectBean implements Serializable {

//    private DataSource ds;
    private List<Project> projectList;
    private int projectID;
    private String name;
    private Date startDate;
    private Date estEndDate;
    private Date actEndDate;
    private String estCostOverall;
    private String currentCost;

    @PostConstruct
    public void init() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
//            Context initContext = new InitialContext();
//            Context envContext = (Context) initContext.lookup("java:comp/env");
//            ds = (DataSource) envContext.lookup("jdbc/avalon_db");
            projectList = ProjectRepository.readAllProject().stream().filter(x -> !x.isDeleted()).collect(Collectors.toList());
        } catch (Exception ex) {
            Logger.getLogger(ProjectBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Project> getProjectList() {
        return projectList;
    }

    public String beforeCreate() {
        this.projectID = 0;
        this.name = null;
        this.startDate = null;
        this.estEndDate = null;
        this.actEndDate = null;
        this.estCostOverall = null;
        this.currentCost = null;
        return "/project/create";
    }

    public String createProject() throws Exception {
        Project newProject = new Project();
        newProject.setName(this.name);
        if (this.startDate != null)
            newProject.setStartDate(new java.sql.Date(this.startDate.getTime()).toLocalDate());
        if (this.estEndDate != null)
            newProject.setEstEndDate(new java.sql.Date(this.estEndDate.getTime()).toLocalDate());
        if (this.actEndDate != null)
            newProject.setActEndDate(new java.sql.Date(this.actEndDate.getTime()).toLocalDate());
        newProject.setCurrentCost(Helpers.parse(this.currentCost, Locale.US));
        newProject.setEstCostOverall(Helpers.parse(this.estCostOverall, Locale.US));
        newProject.setLastUpdatedDate(LocalDateTime.now());
        newProject.setLastUpdatedBy("user");
        ProjectRepository.createProject(newProject);
//        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        projectList = ProjectRepository.readAllProject().stream().filter(x -> !x.isDeleted()).collect(Collectors.toList());
        return "/project/index";
    }

    public String beforeEditing(int projectID) throws Exception {
        Project project = ProjectRepository.readOneProject(projectID);
        this.projectID = project.getProjectID();
        this.name = project.getName();
        if (project.getStartDate() != null)
            this.startDate = java.sql.Date.valueOf(project.getStartDate());
        else
            this.startDate = null;
        if (project.getEstEndDate() != null)
            this.estEndDate = java.sql.Date.valueOf(project.getEstEndDate());
        else
            this.estEndDate = null;
        if (project.getActEndDate() != null)
           this.actEndDate = java.sql.Date.valueOf(project.getActEndDate());
        else
            this.actEndDate = null;
        this.estCostOverall = String.valueOf(project.getEstCostOverall());
        this.currentCost = String.valueOf(project.getCurrentCost());
        return "/project/edit";
    }

    public String editProject() throws Exception {
        Project oldProject = new Project();
        oldProject.setProjectID(this.projectID);
        oldProject.setName(this.name);
        if (this.startDate != null)
            oldProject.setStartDate(new java.sql.Date(this.startDate.getTime()).toLocalDate());
        if (this.estEndDate != null)
            oldProject.setEstEndDate(new java.sql.Date(this.estEndDate.getTime()).toLocalDate());
        if (this.actEndDate != null)
            oldProject.setActEndDate(new java.sql.Date(this.actEndDate.getTime()).toLocalDate());
        oldProject.setEstCostOverall(Helpers.parse(this.estCostOverall, Locale.US));
        oldProject.setCurrentCost(Helpers.parse(this.currentCost, Locale.US));
        oldProject.setLastUpdatedDate(LocalDateTime.now());
        oldProject.setLastUpdatedBy("user");
        ProjectRepository.updateProject(oldProject);
        projectList = ProjectRepository.readAllProject().stream().filter(x -> !x.isDeleted()).collect(Collectors.toList());
        return "/project/index";
    }

    public void deleteProject(int projectID) throws Exception {
        ProjectRepository.deleteProject(projectID);
        projectList = ProjectRepository.readAllProject().stream().filter(x -> !x.isDeleted()).collect(Collectors.toList());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEstEndDate() {
        return estEndDate;
    }

    public void setEstEndDate(Date estEndDate) {
        this.estEndDate = estEndDate;
    }

    public Date getActEndDate() {
        return actEndDate;
    }

    public void setActEndDate(Date actEndDate) {
        this.actEndDate = actEndDate;
    }

    public String getEstCostOverall() {
        return estCostOverall;
    }

    public void setEstCostOverall(String estCostOverall) {
        this.estCostOverall = estCostOverall;
    }

    public String getCurrentCost() {
        return currentCost;
    }

    public void setCurrentCost(String currentCost) {
        this.currentCost = currentCost;
    }
}
