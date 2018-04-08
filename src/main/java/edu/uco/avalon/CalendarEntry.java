package edu.uco.avalon;

import net.bootsfaces.component.fullCalendar.FullCalendarEventBean;

import java.util.Date;

public class CalendarEntry extends FullCalendarEventBean {

    private int id;
    private Integer equipmentID;

    public CalendarEntry(int id, String title, Date start) {
        super(title, start);
        this.id = id;
    }

    @Override
    public void addExtendedFields(StringBuilder stringBuilder) {
        stringBuilder.append("id:'").append(this.id).append("',");

        if (equipmentID != null) {
            stringBuilder.append("equip_id:'").append(this.equipmentID).append("',");
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEquipmentID() {
        return equipmentID;
    }

    public void setEquipmentID(int equipmentID) {
        this.equipmentID = equipmentID;
    }
}
