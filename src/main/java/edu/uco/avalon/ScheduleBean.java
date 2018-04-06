package edu.uco.avalon;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Named(value="schedule")
@SessionScoped
public class ScheduleBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private int equipmentID;
    private String equipmentName;
    private String equipmentType;
    private String allocateDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
    private boolean allocated;

    private List<Schedule> schedules = new ArrayList<>();

    {

        LocalDate date = LocalDate.of(2018, 2, 9);
        schedules.add(new Schedule(1, "Equipment01", "type01", date, false));
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void delete(Schedule schedule) {
        schedules = schedules.stream().filter((sche -> sche.getEquipmentID() != schedule.getEquipmentID())).collect(Collectors.toList());
    }

    public void create() {
        schedules.add(new Schedule(this.equipmentID, this.equipmentName, this.equipmentType, LocalDate.parse(this.allocateDate), this.allocated));
    }

    public void edit() {

    }

    public void beforeEdit(Schedule schedule) {
        this.equipmentID = schedule.getEquipmentID();
        this.equipmentName = schedule.getEquipmentName();
        this.equipmentType = schedule.getEquipmentType();
        this.allocateDate = schedule.getAllocateDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
        this.allocated = schedule.isAllocated();
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

    public String getAllocateDate() {
        return allocateDate;
    }

    public void setAllocateDate(String allocateDate) {
        this.allocateDate = allocateDate;
    }

    public boolean isAllocated() {
        return allocated;
    }

    public void setAllocated(boolean allocated) {
        this.allocated = allocated;
    }
}
