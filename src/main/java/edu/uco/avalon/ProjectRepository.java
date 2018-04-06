package edu.uco.avalon;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectRepository {

    public static List<Project> readAllProject() throws SQLException {
        List<Project> projectList = new ArrayList<>();

        try (Connection conn = ConnectionManager.getConnection()) {
            String query = "SELECT * FROM projects where isDeleted = 0";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Project project = new Project();
                project.setProjectID(rs.getInt("projectID"));
                project.setName(rs.getString("name"));
                project.setStartDate(rs.getDate("startDate") != null ? rs.getDate("startDate").toLocalDate() : null);
                project.setEstEndDate(rs.getDate("estEndDate") != null ? rs.getDate("estEndDate").toLocalDate() : null);
                project.setActEndDate(rs.getDate("actEndDate") != null ? rs.getDate("actEndDate").toLocalDate() : null);
                project.setEstCostOverall(rs.getBigDecimal("estCostOverall"));
                project.setCurrentCost(rs.getBigDecimal("currentCost"));
                project.setLastUpdatedDate(rs.getTimestamp("lastUpdatedDate").toLocalDateTime());
                project.setLastUpdatedBy(rs.getString("lastUpdatedBy"));
                project.setDeleted(rs.getBoolean("isDeleted"));
                projectList.add(project);
            }
        }

        return projectList;

    }

    public static Project readOneProject(int projectID) throws SQLException {
        Project project = new Project();

        try (Connection conn = ConnectionManager.getConnection()) {
            String query = "SELECT * FROM projects WHERE projectID=?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, projectID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                project.setProjectID(rs.getInt("projectID"));
                project.setName(rs.getString("name"));
                project.setStartDate(rs.getDate("startDate") != null ? rs.getDate("startDate").toLocalDate() : null);
                project.setEstEndDate(rs.getDate("estEndDate") != null ? rs.getDate("estEndDate").toLocalDate() : null);
                project.setActEndDate(rs.getDate("actEndDate") != null ? rs.getDate("actEndDate").toLocalDate() : null);
                project.setEstCostOverall(rs.getBigDecimal("estCostOverall"));
                project.setCurrentCost(rs.getBigDecimal("currentCost"));
                project.setLastUpdatedDate(rs.getTimestamp("lastUpdatedDate").toLocalDateTime());
                project.setLastUpdatedBy(rs.getString("lastUpdatedBy"));
                project.setDeleted(rs.getBoolean("isDeleted"));
            }
        }

        return project;

    }

    public static void createProject(Project project) throws SQLException {
        try (Connection conn = ConnectionManager.getConnection()) {
            String query = "INSERT INTO projects(name, startDate, estEndDate, actEndDate," +
                    "estCostOverall, currentCost, lastUpdatedDate, lastUpdatedBy)" +
                    " values (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, project.getName());
            ps.setDate(2, project.getStartDate() != null ? Date.valueOf(project.getStartDate()) : null);
            ps.setDate(3, project.getEstEndDate() != null ? Date.valueOf(project.getEstEndDate()) : null);
            ps.setDate(4, project.getActEndDate() != null ? Date.valueOf(project.getActEndDate()) : null);
            ps.setBigDecimal(5, project.getEstCostOverall());
            ps.setBigDecimal(6, project.getCurrentCost());
            ps.setTimestamp(7, Timestamp.valueOf(project.getLastUpdatedDate()));
            ps.setString(8, project.getLastUpdatedBy());
            ps.executeUpdate();
        }

    }

    public static void updateProject(Project project) throws SQLException {
        try (Connection conn = ConnectionManager.getConnection()) {
            String query = "UPDATE projects SET name=?, startDate=?, estEndDate=?, actEndDate=?," +
                    "estCostOverall=?, currentCost=?, lastUpdatedDate=?, lastUpdatedBy=? WHERE  projectID=?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, project.getName());
            ps.setDate(2, project.getStartDate() != null ? Date.valueOf(project.getStartDate()) : null);
            ps.setDate(3, project.getEstEndDate() != null ? Date.valueOf(project.getEstEndDate()) : null);
            ps.setDate(4, project.getActEndDate() != null ? Date.valueOf(project.getActEndDate()) : null);
            ps.setBigDecimal(5, project.getEstCostOverall());
            ps.setBigDecimal(6, project.getCurrentCost());
            ps.setTimestamp(7, Timestamp.valueOf(project.getLastUpdatedDate()));
            ps.setString(8, project.getLastUpdatedBy());
            ps.setInt(9, project.getProjectID());
            ps.executeUpdate();
        }

    }

    public static void deleteProject(Project project) throws SQLException {
        try (Connection conn = ConnectionManager.getConnection()) {
            String query = "UPDATE projects SET isDeleted=?, lastUpdatedDate=?, lastUpdatedBy=? WHERE projectID=?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setBoolean(1, true);
            ps.setTimestamp(2, Timestamp.valueOf(project.getLastUpdatedDate()));
            ps.setString(3, project.getLastUpdatedBy());
            ps.setInt(4, project.getProjectID());
            ps.executeUpdate();
        }

    }

}
