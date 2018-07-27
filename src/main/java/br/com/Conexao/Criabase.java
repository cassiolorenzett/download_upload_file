/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.Conexao;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.scene.control.Alert;

/**
 *
 * @author cassio
 */
public class Criabase {

    public String Runbase() {
        String passa = "";
        
        String sql = "CREATE TABLE config (\n"
                + "	id      INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + "	clienteid TEXT,\n"
                + "	clientesecret TEXT \n"
                + ");";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:banco.db");
                Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
            stmt.execute("insert into config values (1,'','')");
            
            passa = "";
        } catch (SQLException e) {
            
            passa = "" + e;
        }

        return passa;
    }

}
