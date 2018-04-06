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
     */
    public boolean isProductOwner() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/avalon_db", "root", "2gWAyA5VgWowBC9PtZHpExeAPUtAHDDmcixyHGKW4ZYTckeu3dzdioFTBaQqELVv");
        if (conn == null) {
            throw new SQLException("conn is null.");
        }

        PreparedStatement prepStat = conn.prepareStatement("select * from lu_roles r join users_roles_xref u on r.roleID = u.roleID where u.userID = ? and r.role = 'projectOwner'");
        prepStat.setInt(1, loginBean.getUserID());
        ResultSet rs = prepStat.executeQuery();
        return rs.next();
    }

    public List<Cost> getCosts() throws SQLException {
        List<Cost> staticCosts = new ArrayList<>();

        Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306/avalon_db", "root", "2gWAyA5VgWowBC9PtZHpExeAPUtAHDDmcixyHGKW4ZYTckeu3dzdioFTBaQqELVv");
        if (conn == null) {
            throw new SQLException("conn is null.");
        }

        PreparedStatement prepStat = conn.prepareStatement("select sum(currentCost) from projects");
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
