package edu.uco.avalon;

import net.bootsfaces.component.fullCalendar.FullCalendarEventList;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Named
@RequestScoped
public class DashboardBean {

    private int equipmentID;

    public FullCalendarEventList getCalendarEventList() throws SQLException {
        FullCalendarEventList list = new FullCalendarEventList();
        List<Maintenance> maintenanceList = MaintenanceRepository.readAllMaintenance().stream().
                filter(x -> !x.isDeleted()).collect(Collectors.toList());

        for (Maintenance maintenance : maintenanceList) {
            java.util.Date date = Date.from(maintenance.getNextMaintenanceDate().atZone(ZoneId.systemDefault()).toInstant());
            CalendarEntry entry = new CalendarEntry(maintenance.getMaintenanceID(), maintenance.getDescription(), date);
            entry.setEquipmentID(maintenance.getEquipmentID());
            list.getList().add(entry);
        }

        return list;
    }

    public String getCalendarEventListJson() throws SQLException {
        return this.getCalendarEventList().toJson();
    }

    public int getEquipmentID() {
        return equipmentID;
    }

    public void setEquipmentID(int equipmentID) {
        this.equipmentID = equipmentID;
    }
}
