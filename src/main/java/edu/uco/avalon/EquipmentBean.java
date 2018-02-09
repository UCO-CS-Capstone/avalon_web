package edu.uco.avalon;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Named(value = "equipmentBean")
@SessionScoped

public class EquipmentBean implements Serializable {

    private List<Equipment> equipmentList;
    private List<String> equipmentTypes;
    private int equipmentID;
    private String name;
    private String type;

    @PostConstruct
    public void init() {
        try {

            equipmentTypes = new ArrayList<>();
            equipmentTypes.add("(Please Select");
            equipmentTypes.add("Type1");
            equipmentTypes.add("Type2");
            equipmentTypes.add("Type3");
            equipmentTypes.add("Type4");

            Class.forName("org.mariadb.jdbc.Driver");
            equipmentList = EquipmentRepository.readAllEquipment().stream().filter(x -> !x.isDeleted()).collect(Collectors.toList());
        } catch (Exception ex) {
            Logger.getLogger(EquipmentBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Equipment> getEquipmentList() {
        return equipmentList;
    }

    public String beforeCreate() {
        this.equipmentID = 0;
        this.name = null;
        this.type = null;
        return "/Equipment/create";
    }

    public String createEquipment() throws Exception{
        Equipment newEquipment = new Equipment();
        newEquipment.setName(this.name);
        newEquipment.setType(this.type);
        newEquipment.setLastUpdatedBy("user");
        newEquipment.setLastUpdatedDate(LocalDateTime.now());
        EquipmentRepository.createEquipment(newEquipment);
        equipmentList = EquipmentRepository.readAllEquipment().stream().filter(x -> !x.isDeleted()).collect(Collectors.toList());
        return "/Equipment/index";
    }

    public String beforeEdit(int equipmentID) throws Exception{
        Equipment equipment = EquipmentRepository.readOneEquipment(equipmentID);
        this.equipmentID = equipment.getEquipmentID();
        this.name = equipment.getName();
        this.type = equipment.getType();
        return "/Equipment/edit";
    }

    public String editEquipment () throws Exception{
        Equipment oldEquipment = new Equipment();
        oldEquipment.setEquipmentID(this.equipmentID);
        oldEquipment.setName(this.name);
        oldEquipment.setType(this.type);
        oldEquipment.setLastUpdatedDate(LocalDateTime.now());
        oldEquipment.setLastUpdatedBy("user");
        EquipmentRepository.updateEquipment(oldEquipment);
        equipmentList = EquipmentRepository.readAllEquipment().stream().filter(x -> !x.isDeleted()).collect(Collectors.toList());
        return "/Equipment/index";
    }

    public void deleteEquipment(int equipmentID) throws Exception{
        EquipmentRepository.deleteEquipment(equipmentID);
        equipmentList = EquipmentRepository.readAllEquipment().stream().filter(x -> !x.isDeleted()).collect(Collectors.toList());

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getEquipmentTypes() {
        return equipmentTypes;
    }

}
