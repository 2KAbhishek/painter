package com.iam2kabhishek.painter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserModel {
    Connection conn;

    public UserModel() {
        conn = DBConnect.getConnection();
        if (conn == null) {
            System.out.println("Connection failed");
        }
    }

    public boolean canLogin(String username, String password) {
        PreparedStatement stmt;
        ResultSet resultSet;
        String sql = "SELECT * FROM user WHERE name = ? AND password = ?";
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return true;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    public void addUser(String username, String password) {
        PreparedStatement stmt;
        String sql = "INSERT INTO user (name, password) VALUES (?, ?)";
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
