package edu.uco.avalon;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Project {

    private int projectID;
    private String name;
    private LocalDate startDate;
    private LocalDate estEndDate;
    private LocalDate actEndDate;
    private BigDecimal estCostOverall;
    private BigDecimal currentCost;
    private LocalDateTime lastUpdatedDate;
    private String lastUpdatedBy;
    private boolean isDeleted;

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEstEndDate() {
        return estEndDate;
    }

    public void setEstEndDate(LocalDate estEndDate) {
        this.estEndDate = estEndDate;
    }

    public LocalDate getActEndDate() {
        return actEndDate;
    }

    public void setActEndDate(LocalDate actEndDate) {
        this.actEndDate = actEndDate;
    }

    public BigDecimal getEstCostOverall() {
        return estCostOverall;
    }

    public void setEstCostOverall(BigDecimal estCostOverall) {
        this.estCostOverall = estCostOverall;
    }

    public BigDecimal getCurrentCost() {
        return currentCost;
    }

    public void setCurrentCost(BigDecimal currentCost) {
        this.currentCost = currentCost;
    }

    public LocalDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(LocalDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
