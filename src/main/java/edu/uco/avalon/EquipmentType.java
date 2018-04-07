package edu.uco.avalon;

import java.time.LocalDateTime;

public class EquipmentType {

    private int equipmentTypeID;
    private String description;
    private LocalDateTime lastUpdatedDate;
    private String lastUpdatedBy;
    private boolean isDeleted;


    public int getEquipmentTypeID() { return equipmentTypeID; }

    public void setEquipmentTypeID(int equipmentTypeID) { this.equipmentTypeID = equipmentTypeID; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getLastUpdatedDate() { return lastUpdatedDate; }

    public void setLastUpdatedDate(LocalDateTime lastUpdatedDate) { this.lastUpdatedDate = lastUpdatedDate; }

    public String getLastUpdatedBy() { return lastUpdatedBy; }

    public void setLastUpdatedBy(String lastUpdatedBy) { this.lastUpdatedBy = lastUpdatedBy; }

    public boolean isDeleted() { return isDeleted; }
}