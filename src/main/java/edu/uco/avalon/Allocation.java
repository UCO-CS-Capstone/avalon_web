package edu.uco.avalon;

import java.time.LocalDateTime;

public class Allocation {

    private int allocationID;
    private int equipmentID;
    private String displayForEquipmentID;
    private int projectID;
    private String displayForProjectID;
    private LocalDateTime lastUpdatedDate;
    private String lastUpdatedBy;
    private boolean isDeleted;

    public int getAllocationID() {
        return allocationID;
    }

    public void setAllocationID(int allocationID) {
        this.allocationID = allocationID;

    }

    public int getEquipmentID() {
        return equipmentID;
    }

    public void setEquipmentID(int equipmentID) {
        this.equipmentID = equipmentID;
    }

    public String getDisplayForEquipmentID() {
        return displayForEquipmentID;
    }

    public void setDisplayForEquipmentID(String displayForEquipmentID) {
        this.displayForEquipmentID = displayForEquipmentID;
    }

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public String getDisplayForProjectID() {
        return displayForProjectID;
    }

    public void setDisplayForProjectID(String displayForProjectID) {
        this.displayForProjectID = displayForProjectID;
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
