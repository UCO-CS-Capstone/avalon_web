package edu.uco.avalon;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Maintenance {

    private int maintenanceID;
	private String description;
    private int equipmentID;
    private String displayForEquipmentID;
    private BigDecimal cost;
	private LocalDateTime nextMaintenanceDate;
    private LocalDateTime lastUpdatedDate;
    private String lastUpdatedBy;
    private boolean isDeleted;

    public int getMaintenanceID() {
        return maintenanceID;
    }

    public void setMaintenanceID(int maintenanceID) {
        this.maintenanceID = maintenanceID;

    }

	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
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

    public BigDecimal getCost() {
		return cost;
	}
	
	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public LocalDateTime getNextMaintenanceDate() {
        return nextMaintenanceDate;
    }

    public void setNextMaintenanceDate(LocalDateTime nextMaintenanceDate) {
        this.nextMaintenanceDate = nextMaintenanceDate;
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