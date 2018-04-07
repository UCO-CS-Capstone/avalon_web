package edu.uco.avalon;

import net.bootsfaces.component.fullCalendar.FullCalendarEventBean;

import java.util.Date;

public class CalendarEntry extends FullCalendarEventBean {

    private int id;

    public CalendarEntry(int id, String title, Date start) {
        super(title, start);
        this.id = id;
    }

    @Override
    public void addExtendedFields(StringBuilder stringBuilder) {
        stringBuilder.append("id:'").append(this.id).append("',");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
