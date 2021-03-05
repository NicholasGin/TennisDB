/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TennisBallGames;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Abdelkader
 */
public class TeamsAdapter {

    Connection connection;

    public TeamsAdapter(Connection conn, Boolean reset) throws SQLException {
        connection = conn;
        if (reset) {
            Statement stmt = connection.createStatement();
            try {
                // Remove tables if database tables have been created.
                // This will throw an exception if the tables do not exist
                // We drop Matches first because it refrences the table Teams
                stmt.execute("DROP TABLE Matches");
                stmt.execute("DROP TABLE Teams");
                // then do finally
            } catch (SQLException ex) {
                // No need to report an error.
                // The table simply did not exist.
                // do finally to create it
            } finally {
                // Create the table of teams
                stmt.execute("CREATE TABLE Teams ("
                        + "TeamName CHAR(15) NOT NULL PRIMARY KEY, "
                        + "Wins INT, " + "Losses INT, "
                        + "Ties INT" + ")");
                populateSampls();
            }
        }
    }

    private void populateSampls() throws SQLException {
        // Add some teams
        this.insertTeam("Astros");
        this.insertTeam("Marlins");
        this.insertTeam("Brewers");
        this.insertTeam("Cubs");
    }

    public void insertTeam(String name) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("INSERT INTO Teams (TeamName, Wins, Losses, Ties) VALUES ('" + name + "', 0, 0, 0)");
    }

    // Get all teams Data
    public ObservableList<Teams> getTeamsList() throws SQLException {
        ObservableList<Teams> list = FXCollections.observableArrayList();
        ResultSet rs;

        // Create a Statement object
        Statement stmt = connection.createStatement();

        // Create a string with a SELECT statement
        String sqlStatement = "SELECT * FROM Teams";

        // Execute the statement and return the result
        rs = stmt.executeQuery(sqlStatement);

        while (rs.next()) {
            list.add(new Teams(rs.getString("TeamName"),
                    rs.getInt("Wins"),
                    rs.getInt("Losses"),
                    rs.getInt("Ties")));
        }
        return list;
    }

    // Get all teams names to populate the ComboBoxs used in Task #3.
    public ObservableList<String> getTeamsNames() throws SQLException {
        ObservableList<String> list = FXCollections.observableArrayList();
        ResultSet rs;


        // Create a Statement object
        Statement stmt = connection.createStatement();

        // Create a string with a SELECT statement
        String sql = "SELECT TeamName FROM Teams";

        // Execute the statement and return the result
        rs = stmt.executeQuery(sql);

        // loop for the all rs rows and update list

        while(rs.next()) {
            list.add(rs.getString("TeamName"));
        }

        return list;
    }

    public void setStatus(String hTeam, String vTeam, int hScore, int vScore) throws SQLException {
        // Create a Statement object
        Statement stmt = connection.createStatement();
        ResultSet rs;
        
        // Write your code here for Task #4

        String sql = "SELECT * FROM Teams";
        rs = stmt.executeQuery(sql);

        int hTies = 0, vTies = 0, hWins = 0, vWins = 0, hLosses = 0, vLosses = 0;

        while(rs.next()) {
            System.out.println(rs.getString("TeamName"));
            if (rs.getString("TeamName").split(" ")[0].equals(hTeam)) {
                hWins = rs.getInt("Wins");
                hTies = rs.getInt("Ties");
                hLosses = rs.getInt("Losses");
            }
            if (rs.getString("TeamName").split(" ")[0].equals(vTeam)) {
                vWins = rs.getInt("Wins");
                vTies = rs.getInt("Ties");
                vLosses = rs.getInt("Losses");
            }
        }

        if(hScore == vScore) {
            hTies++;
            vTies++;

            stmt.execute("UPDATE Teams SET Ties = " + hTies + " WHERE TeamName = '" + hTeam + "'");
            stmt.execute("UPDATE Teams SET Ties = " + vTies + " WHERE TeamName = '" + vTeam + "'");
        }

        else if (hScore > vScore) {
            hWins++;
            vLosses++;
            stmt.execute("UPDATE Teams SET Wins = " + hWins + " WHERE TeamName = '" + hTeam + "'");
            stmt.execute("UPDATE Teams SET Losses = " + vLosses + " WHERE TeamName = '" + vTeam + "'");
        }

        else {
            hLosses++;
            vWins++;
            stmt.execute("UPDATE Teams SET Losses = " + hLosses + " WHERE TeamName = '" + hTeam + "'");
            stmt.execute("UPDATE Teams SET Wins = " + vWins + " WHERE TeamName = '" + vTeam + "'");
        }
    }
}
