package edu.uco.avalon;

import java.time.LocalDate;

public class Schedule{

    private int equipmentID;
    private String equipmentName;
    private String equipmentType;
    private LocalDate allocateDate;
    private boolean allocated;
}

    public int getEquipmentID() {
        return equipmentID;
    }

    public void setEquipmentID(int equipmentID) {
        this.equipmentID = equipmentID;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }


    public String getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(String equipmentType) {
        this.equipmentType = equipmentType;
    }



    public LocalDateTime getAllocateDate() {
        return allocateDate;
    }

    public void setAllocateDate(LocalDateTime allocateDate) {
        this.allocateDate = allocateDate;
    }


    public boolean isAllocated() {
        return allocated;
    }

    public void setAllocated(boolean allocated) {
        this.allocated = allocated;
    }

