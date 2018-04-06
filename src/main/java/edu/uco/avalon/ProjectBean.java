package edu.uco.avalon;

import org.omnifaces.util.Faces;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.constraint.UniqueHashCode;
import org.supercsv.cellprocessor.ift.CellProcessor;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
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

    private List<Allocation> allocatedList;
    private List<Allocation> unallocatedList;
    private int[] selectedAllocation;

    private String myStyle;

    public String getMyStyle() {
        return myStyle;
    }

    public void setMyStyle(String myStyle) {
        this.myStyle = myStyle;
    }
    private void filterListByNextMaintenanceDate(List<Allocation> unallocatedList) throws SQLException {
        for (int i = unallocatedList.size()-1; i >= 0; i--) {
            LocalDateTime dateToCompare = MaintenanceRepository.getLatestNextMaintenanceDateForEquipment(unallocatedList.get(i).getEquipmentID());
            if (dateToCompare == null) {}
            else if (dateToCompare.isAfter(LocalDateTime.now())) {}
            else {
                unallocatedList.remove(i);
            }
        }
    }

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

    public List<Allocation> getAllocatedList() {
        return allocatedList;
    }

    public List<Allocation> getUnallocatedList() {
        return unallocatedList;
    }

    public int[] getSelectedAllocation() {
        return selectedAllocation;
    }

    public void setSelectedAllocation(int[] selectedAllocation) {
        this.selectedAllocation = selectedAllocation;
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

    public String validateProject() throws Exception {
        boolean isError = false;

        if (this.startDate != null && this.actEndDate != null) {
            if (this.startDate.after(this.actEndDate)) {
                isError = true;
                FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Start Date cannot be after Actual End Date.", null);
                FacesContext.getCurrentInstance().addMessage("project", facesMessage);
            }
        }
        if (this.startDate != null && this.estEndDate != null) {
            if (this.startDate.after(this.estEndDate)) {
                isError = true;
                FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Start Date cannot be after Estimated End Date.", null);
                FacesContext.getCurrentInstance().addMessage("project", facesMessage);
            }
        }

        if (!isError) {
            if (this.projectID == 0) {
                return this.createProject();
            }
            else {
                return this.editProject();
            }
        }
        return null;
    }

    public String createProject() throws Exception {
        Project newProject = new Project();
        newProject.setName(this.name);
        if (this.startDate != null) newProject.setStartDate(new java.sql.Date(this.startDate.getTime()).toLocalDate());
        if (this.estEndDate != null) newProject.setEstEndDate(new java.sql.Date(this.estEndDate.getTime()).toLocalDate());
        if (this.actEndDate != null) newProject.setActEndDate(new java.sql.Date(this.actEndDate.getTime()).toLocalDate());
        newProject.setCurrentCost(Helpers.parse(this.currentCost, Locale.US));
        newProject.setEstCostOverall(Helpers.parse(this.estCostOverall, Locale.US));
        newProject.setLastUpdatedDate(LocalDateTime.now());
        newProject.setLastUpdatedBy("user");
        ProjectRepository.createProject(newProject);
//        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        projectList = ProjectRepository.readAllProject().stream().filter(x -> !x.isDeleted()).collect(Collectors.toList());
        return "/project/index";
    }

    public String projectDetail(int projectID) throws Exception {
        Project project = ProjectRepository.readOneProject(projectID);
        this.projectID = project.getProjectID();
        this.name = project.getName();
        this.startDate = (project.getStartDate() != null) ? java.sql.Date.valueOf(project.getStartDate()) : null;
        this.estEndDate = (project.getEstEndDate() != null) ? java.sql.Date.valueOf(project.getEstEndDate()) : null;
        this.actEndDate = (project.getActEndDate() != null) ? java.sql.Date.valueOf(project.getActEndDate()) : null;
        this.estCostOverall = (project.getEstCostOverall() != null) ? "$" + String.format("%,.2f", project.getEstCostOverall()) : null;
        this.currentCost = (project.getCurrentCost() != null) ? "$" + String.format("%,.2f", project.getCurrentCost()) : null;
        allocatedList = AllocationRepository.readAllAllocation().stream().filter(x -> !x.isDeleted() && x.getProjectID() == projectID).collect(Collectors.toList());
        unallocatedList = AllocationRepository.readAllAllocation().stream().filter(x -> !x.isDeleted() && x.getProjectID() == 0).collect(Collectors.toList());
        filterListByNextMaintenanceDate(unallocatedList);
        unallocatedList.sort(Comparator.comparing(Allocation::getDisplayForEquipmentID));

        return "/project/detail";
    }

    public void addAllocation() throws Exception {
        Allocation allocation = new Allocation();
        allocation.setProjectID(this.projectID);
        allocation.setLastUpdatedDate(LocalDateTime.now());
        allocation.setLastUpdatedBy("user");
        AllocationRepository.addAllocation(selectedAllocation, allocation);
        allocatedList = AllocationRepository.readAllAllocation().stream().filter(x -> !x.isDeleted() && x.getProjectID() == projectID).collect(Collectors.toList());
        unallocatedList = AllocationRepository.readAllAllocation().stream().filter(x -> !x.isDeleted() && x.getProjectID() == 0).collect(Collectors.toList());
        filterListByNextMaintenanceDate(unallocatedList);
        unallocatedList.sort(Comparator.comparing(Allocation::getDisplayForEquipmentID));
    }

    public void removeAllocation(int allocationID) throws Exception {
        int[] removedAllocation = { allocationID };
        Allocation allocation = new Allocation();
        allocation.setLastUpdatedDate(LocalDateTime.now());
        allocation.setLastUpdatedBy("user");
        AllocationRepository.removeAllocationByAllocationID(removedAllocation, allocation);
        allocatedList = AllocationRepository.readAllAllocation().stream().filter(x -> !x.isDeleted() && x.getProjectID() == projectID).collect(Collectors.toList());
        unallocatedList = AllocationRepository.readAllAllocation().stream().filter(x -> !x.isDeleted() && x.getProjectID() == 0).collect(Collectors.toList());
        filterListByNextMaintenanceDate(unallocatedList);
        unallocatedList.sort(Comparator.comparing(Allocation::getDisplayForEquipmentID));
    }

    public String beforeEditing(int projectID) throws Exception {
        Project project = ProjectRepository.readOneProject(projectID);
        this.projectID = project.getProjectID();
        this.name = project.getName();
        this.startDate = (project.getStartDate() != null) ? java.sql.Date.valueOf(project.getStartDate()) : null;
        this.estEndDate = (project.getEstEndDate() != null) ? java.sql.Date.valueOf(project.getEstEndDate()) : null;
        this.actEndDate = (project.getActEndDate() != null) ? java.sql.Date.valueOf(project.getActEndDate()) : null;
        this.estCostOverall = String.valueOf(project.getEstCostOverall());
        this.currentCost = String.valueOf(project.getCurrentCost());
        return "/project/edit";
    }

    public String editProject() throws Exception {
        Project oldProject = new Project();
        oldProject.setProjectID(this.projectID);
        oldProject.setName(this.name);
        if (this.startDate != null) oldProject.setStartDate(new java.sql.Date(this.startDate.getTime()).toLocalDate());
        if (this.estEndDate != null) oldProject.setEstEndDate(new java.sql.Date(this.estEndDate.getTime()).toLocalDate());
        if (this.actEndDate != null) oldProject.setActEndDate(new java.sql.Date(this.actEndDate.getTime()).toLocalDate());
        oldProject.setEstCostOverall(Helpers.parse(this.estCostOverall, Locale.US));
        oldProject.setCurrentCost(Helpers.parse(this.currentCost, Locale.US));
        oldProject.setLastUpdatedDate(LocalDateTime.now());
        oldProject.setLastUpdatedBy("user");
        ProjectRepository.updateProject(oldProject);
        projectList = ProjectRepository.readAllProject().stream().filter(x -> !x.isDeleted()).collect(Collectors.toList());
        return "/project/index";
    }

    public void deleteProject(int projectID) throws Exception {
        Project project = new Project();
        project.setProjectID(projectID);
        project.setLastUpdatedDate(LocalDateTime.now());
        project.setLastUpdatedBy("user");
        AllocationRepository.removeAllocationByProject(project);
        ProjectRepository.deleteProject(project);
        projectList = ProjectRepository.readAllProject().stream().filter(x -> !x.isDeleted()).collect(Collectors.toList());
    }

    public String calculateStatus(Project project) {
        StringBuilder status = new StringBuilder();
        boolean isProblem = false;
        myStyle = "";

        if (project.getActEndDate() != null) {
            status.append("Finished");
            myStyle = "text-success";
            if (project.getEstEndDate() != null) {
                if (project.getActEndDate().isAfter(project.getEstEndDate())) {
                    status.append(" Late");
                    myStyle = "text-primary";
                    isProblem = true;
                }
            }
            status.append(", ");
        }
        else if (project.getEstEndDate() != null) {
            if (LocalDate.now().isAfter(project.getEstEndDate())) {
                status.append("Overdue, ");
                myStyle = "text-danger";
                isProblem = true;
            }
        }

        if (project.getCurrentCost().compareTo(project.getEstCostOverall()) > 0) {
            status.append("Overbudget, ");
            if (!myStyle.equals("text-danger"))
                myStyle = "text-warning";
            isProblem = true;
        }

        if (!isProblem) {
            status.append("No Issue, ");
            if (!myStyle.equals("text-success"))
                myStyle = "text-info";
        }

        status.setLength(status.length() - 2);
        return status.toString();
    }

    public static void toCSV() throws IOException, SQLException {
        CellProcessor[] processors = new CellProcessor[] {
            new UniqueHashCode(), // projectID (must be unique)
            new NotNull(), // name
            new Optional(), // startDate
            new Optional(), // estEndDate
            new Optional(), // actEndDate
            new Optional(), // estCostOverall
            new Optional(), // currentCost
            new Optional(), // lastUpdatedDate
            new Optional(), // lastUpdatedBy
        };
        // the header elements are used to map the bean values to each column (names must match)
        final String[] headers = new String[] {
                "projectID",
                "name",
                "startDate",
                "estEndDate",
                "actEndDate",
                "estCostOverall",
                "currentCost",
                "lastUpdatedDate",
                "lastUpdatedBy"//,
                //"isDeleted"
        };
        List<Project> projects = ProjectRepository.readAllProject();
        InputStream data = CSVBuilder.create(processors, headers, projects);
        String filename = CSVBuilder.filename("projects");
        Faces.sendFile(data, filename, true);
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
