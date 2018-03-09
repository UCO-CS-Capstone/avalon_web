package edu.uco.avalon;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Named(value="sechdule")
@SessionScoped
public class SechduleBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String projectName;
    private String startDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
    private String endDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
    private double currentCost = 0;
    private String milestone;
    private boolean edit;

    private List<Sechdule> schedules = new ArrayList<>();

    {

        LocalDate startDate = LocalDate.of(2018, 2, 9);
        LocalDate endDate = LocalDate.of(2018, 6, 1);
        schedules.add(new Sechdule("Project1", startDate, endDate, currentCost, "N/A"));
    }

    public List<Sechdule> getSchedules() {
        return schedules;
    }

    public void delete(Sechdule schedule) {
        schedules = schedules.stream().filter((sche -> !sche.getProjectName().equals(schedule.getProjectName()))).collect(Collectors.toList());
    }

    public void beforeCreate() {
        this.edit = false;
        this.projectName = "";
        this.startDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        this.endDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        this.currentCost = 0;
        this.milestone = "";
    }

    public void save() {
        if (this.edit) {
            schedules.forEach(schedule -> {
                if (schedule.getProjectName().equals(this.getProjectName())) {
                    schedule.setStartDate(LocalDate.parse(this.getStartDate()));
                    schedule.setEndDate(LocalDate.parse(this.getEndDate()));
                    schedule.setCurrentCost(this.getCurrentCost());
                    schedule.setMilestone(this.getMilestone());
                }
            });
        } else {
            schedules.add(new Sechdule(
                    this.projectName,
                    LocalDate.parse(this.startDate),
                    LocalDate.parse(this.endDate),
                    currentCost, milestone));
        }
    }


    public void beforeEdit(Sechdule schedule) {
        this.edit = true;
        this.projectName = schedule.getProjectName();
        this.startDate = schedule.getStartDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
        this.endDate = schedule.getEndDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
        this.currentCost = schedule.getCurrentCost();
        this.milestone = schedule.getMilestone();
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public double getCurrentCost() {
        return currentCost;
    }

    public void setCurrentCost(double currentCost) {
        this.currentCost = currentCost;
    }

    public String getMilestone() {
        return milestone;
    }

    public void setMilestone(String milestone) {
        this.milestone = milestone;
    }

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }
}
