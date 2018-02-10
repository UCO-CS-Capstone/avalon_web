package edu.uco.avalon;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
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
    private double estCostOverall;
    private double currentCost;

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
        this.estCostOverall = 0.0;
        this.currentCost = 0.0;
        return "/project/create";
    }

    public String createProject() throws Exception {
        Project newProject = new Project();
        newProject.setName(this.name);
        newProject.setStartDate(new java.sql.Date(this.startDate.getTime()).toLocalDate());
        newProject.setEstEndDate(new java.sql.Date(this.estEndDate.getTime()).toLocalDate());
        if (this.actEndDate != null)
            newProject.setActEndDate(new java.sql.Date(this.actEndDate.getTime()).toLocalDate());
        newProject.setEstCostOverall(this.estCostOverall);
        newProject.setCurrentCost(this.currentCost);
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
        this.startDate = java.sql.Date.valueOf(project.getStartDate());
        this.estEndDate = java.sql.Date.valueOf(project.getEstEndDate());
        if (project.getActEndDate() != null)
           this.actEndDate = java.sql.Date.valueOf(project.getActEndDate());
        else
            this.actEndDate = null;
        this.estCostOverall = project.getEstCostOverall();
        this.currentCost = project.getCurrentCost();
        return "/project/edit";
    }

    public String editProject() throws Exception {
        Project oldProject = new Project();
        oldProject.setProjectID(this.projectID);
        oldProject.setName(this.name);
        oldProject.setStartDate(new java.sql.Date(this.startDate.getTime()).toLocalDate());
        oldProject.setEstEndDate(new java.sql.Date(this.estEndDate.getTime()).toLocalDate());
        if (this.actEndDate != null)
            oldProject.setActEndDate(new java.sql.Date(this.actEndDate.getTime()).toLocalDate());
        oldProject.setEstCostOverall(this.estCostOverall);
        oldProject.setCurrentCost(this.currentCost);
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

    public double getEstCostOverall() {
        return estCostOverall;
    }

    public void setEstCostOverall(double estCostOverall) {
        this.estCostOverall = estCostOverall;
    }

    public double getCurrentCost() {
        return currentCost;
    }

    public void setCurrentCost(double currentCost) {
        this.currentCost = currentCost;
    }
}
