/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.BackupBD;

import br.com.Conexao.Criabase;
import br.com.Conexao.conn_db;
import br.com.Util.Getcredencialgdrive;
import br.com.Util.Googledrive;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author cassio
 */
public class SincController implements Initializable{
    
    @FXML
    private TextField cliente_id;

    @FXML
    private PasswordField cliente_secret;

    @FXML
    private Button sinc;

    @FXML
    private Button sair;

    @FXML
    private TextArea status_sinc;

    @FXML
    void sair(ActionEvent event) {
        Stage stage = (Stage) sair.getScene().getWindow(); //Obtendo a janela atual
        stage.close();
    }

    @FXML
    void sinc(ActionEvent event) {

           try {
                Googledrive gdrive = new Googledrive();
                gdrive.setClientsecrets(cliente_id.getText().trim(), cliente_secret.getText().trim());
                String retorno = gdrive.ativaconta();
                status_sinc.setText("Conta ativada com sucesso !" + retorno);
                gravacredencial();
                
            } catch (IOException ex) {
               
                status_sinc.setText("" + ex);
            }
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
       
       Getcredencialgdrive credencial = new Getcredencialgdrive();
       cliente_id.setText(credencial.getCredencial().get(0).toString());
       cliente_secret.setText(credencial.getCredencial().get(1).toString());
    }
    
    
    private void gravacredencial()
    {
        conn_db connect = new conn_db();
        connect.conectar();
        PreparedStatement ps_ = null;
        try {
            ps_ = connect.conn.prepareStatement("update config set clienteid = ?,clientesecret = ? where id = 1");
            ps_.setString(1, cliente_id.getText().trim());
            ps_.setString(2, cliente_secret.getText().trim());
            ps_.executeUpdate();            

            ps_.close();
        } catch (Exception ex) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Atualização");
            alert.setContentText("Erro ao Atualizar, Erro_1: " + ex);
            alert.showAndWait();
        }
        try {
            ps_.close();
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Atualização");
            alert.setContentText("Erro ao Atualizar, Erro_2: " + ex);
            alert.showAndWait();
        }
    }
}
