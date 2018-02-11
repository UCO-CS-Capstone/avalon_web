package edu.uco.avalon;


import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;


@Named(value = "ScheduleBean")
@SessionScoped
public class ScheduleBean implements Serializable {


    private List<Schedule> ScheduleList;
    private int equipmentID;



    private String equipmentName;
    private String equipmentType;
    private LocalDate allocateDate;
    private boolean allocated;


    public List<Schedule> getScheduleList() {
        return ScheduleList;
    }

    public boolean isAllocated() {
        return allocated;
    }

    public void setAllocated(boolean allocated) {
        this.allocated = allocated;
    }

    public LocalDate getAllocateDate() {

        return allocateDate;
    }

    public void setAllocateDate(LocalDate allocateDate) {
        this.allocateDate = allocateDate;
    }

    public String getEquipmentType() {

        return equipmentType;
    }

    public void setEquipmentType(String equipmentType) {
        this.equipmentType = equipmentType;
    }

    public String getEquipmentName() {
        return equipmentName;

    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public int getEquipmentID() {

        return equipmentID;
    }

    public void setEquipmentID(int equipmentID) {
        this.equipmentID = equipmentID;
    }
}
