/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.Conexao;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import javafx.scene.control.Alert;

/**
 *
 * @author cassio
 */
public class conn_db {
    public static Connection conn = null;
   
    /**
    * Função que executa a conexão com a base de dados
    * Utilizada em toda a aplicação
    */
    public void conectar() {
       
        String cam = new File("banco.db").getAbsolutePath();
        File arq = new File(cam);
        boolean gera = false;
        
        if (!arq.exists()){
            gera = true;
        }else
        {
            gera = false;
        }
        
        try {
         Class.forName("org.sqlite.JDBC");
         conn = DriverManager.getConnection("jdbc:sqlite:banco.db");
         conn.setAutoCommit(true);
         
         if (gera == true)
         {
            Criabase runbase = new Criabase();
            runbase.Runbase();
         }
            
      } catch ( Exception e ) {
            Alert msgalert = new Alert(Alert.AlertType.ERROR);
            msgalert.setHeaderText("Erros encontrados:");
            msgalert.setContentText(""+e);
            msgalert.showAndWait();
          
      }
      
   }
}