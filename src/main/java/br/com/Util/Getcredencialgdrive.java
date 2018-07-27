/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.Util;

import br.com.Conexao.conn_db;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.scene.control.Alert;

/**
 *
 * @author cassio
 */
public class Getcredencialgdrive {

    public ArrayList getCredencial() {
        conn_db conn = new conn_db();
        conn.conectar();
        ResultSet rs = null;
        ArrayList<String> ret = new ArrayList<String>();
        try {

            rs = conn_db.conn.createStatement().executeQuery("select * from config");
            while (rs.next()) {

                ret.add(rs.getString("clienteid"));
                ret.add(rs.getString("clientesecret"));

            }

        } catch (SQLException ex) {

            Alert erro = new Alert(Alert.AlertType.ERROR);
            erro.setContentText("erro ao buscar credenciais: " + ex);
            erro.showAndWait();
            ret.add(null);
        } finally {
            try {
                rs.close();
            } catch (SQLException ex) {
                Alert erro = new Alert(Alert.AlertType.ERROR);
                erro.setContentText("erro ao buscar credenciais: " + ex);
                erro.showAndWait();
                ret.add(null);
            }
        }
        return ret;
    }
}
