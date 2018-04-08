package edu.uco.avalon;


import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Named(value="costAnalysisTool")
@SessionScoped
public class CostAnalysisToolBean implements Serializable {

    @Inject
    private LoginBean loginBean;

    /**
     *
     * @return
     * @throws SQLException
     */
    public boolean isProductOwner() throws SQLException {
        try (Connection conn = ConnectionManager.getConnection()) {

            PreparedStatement prepStat = conn.prepareStatement("SELECT * FROM lu_roles r JOIN users_roles_xref u ON r.roleID = u.roleID WHERE u.userID = ? AND r.role = 'projectOwner'");
            prepStat.setInt(1, loginBean.getUserID());
            ResultSet rs = prepStat.executeQuery();
            return rs.next();
        }
    }

    public List<Cost> getCosts() throws SQLException {
        List<Cost> staticCosts = new ArrayList<>();

        try (Connection conn = ConnectionManager.getConnection()) {

            PreparedStatement prepStat = conn.prepareStatement("SELECT sum(currentCost) FROM projects");
            ResultSet rs = prepStat.executeQuery();
            if (!rs.next()) {
                throw new RuntimeException("wrong.");
            }

            staticCosts.add(new Cost("total project cost", rs.getBigDecimal(1).doubleValue()));
            staticCosts.add(new Cost("maintenances cost(per day)", 24.3));
            staticCosts.add(new Cost("milestone cost", 34.3));
            return staticCosts;
        }
    }
}
