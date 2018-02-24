package edu.uco.avalon;

import java.time.LocalDate;

public class Sechdule {

    private String projectName;
    private LocalDate startDate;
    private LocalDate endDate;
    private double currentCost;
    private String milestone;

    public Sechdule() {
    }

    public Sechdule(String projectName, LocalDate startDate, LocalDate endDate, double currentCost, String milestone) {
        this.projectName = projectName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.currentCost = currentCost;
        this.milestone = milestone;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
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
}

