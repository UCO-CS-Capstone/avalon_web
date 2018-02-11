package edu.uco.avalon;

import java.time.LocalDate;

public class Schedule{

    private int equipmentID;
    private String equipmentName;
    private String equipmentType;
    private LocalDate allocateDate;
    private boolean allocated;

    public Schedule() { }

    public Schedule(int equipmentID, String equipmentName, String equipmentType, LocalDate allocateDate, boolean allocated) {
        this.equipmentID = equipmentID;
        this.equipmentName = equipmentName;
        this.equipmentType = equipmentType;
        this.allocateDate = allocateDate;
        this.allocated = allocated;
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


    public LocalDate getAllocateDate() {
        return allocateDate;
    }

    public void setAllocateDate(LocalDate allocateDate) {
        this.allocateDate = allocateDate;
    }


    public boolean isAllocated() {
        return allocated;
    }

    public void setAllocated(boolean allocated) {
        this.allocated = allocated;
    }
}

