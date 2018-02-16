package edu.uco.avalon;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Named(value = "allocationBean")
@RequestScoped
public class AllocationBean implements Serializable {

    private List<Allocation> allocationList;

    @PostConstruct
    public void init() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            allocationList = AllocationRepository.readAllAllocation().stream().filter(x -> !x.isDeleted()).collect(Collectors.toList());
        } catch (Exception ex) {
            Logger.getLogger(AllocationBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Allocation> getAllocationList() {
        return allocationList;
    }

    public void removeAllocation(int allocationID) throws Exception {
        Allocation allocation = new Allocation();
        allocation.setLastUpdatedDate(LocalDateTime.now());
        allocation.setLastUpdatedBy("user");
        int[] removedAllocation = {allocationID};
        AllocationRepository.removeAllocationByAllocationID(removedAllocation, allocation);
        allocationList = AllocationRepository.readAllAllocation().stream().filter(x -> !x.isDeleted()).collect(Collectors.toList());
    }

}
