package br.com.BackupBD;

import br.com.Conexao.conn_db;
import br.com.Util.Backupgdrive;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class FXMLController implements Initializable {

    @FXML
    private TextField path_up_bd;

    @FXML
    private Button bt_up;

    @FXML
    private Button run_up;

    @FXML
    private TextField path_dw_bd;

    @FXML
    private Button bt_dw;

    @FXML
    private Button run_dw;

    @FXML
    private ProgressIndicator progress;

    @FXML
    private MenuItem config;

    @FXML
    void bt_dw(ActionEvent event) {
        progress.setProgress(0);
        Stage stage = new Stage();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory
                = directoryChooser.showDialog(stage);

        if (selectedDirectory == null) {
            path_dw_bd.setText("");
        } else {
            path_dw_bd.setText(selectedDirectory.getAbsolutePath());
        }

    }

    @FXML
    void bt_up(ActionEvent event) {
        progress.setProgress(0);
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        File selectedDirectory
                = fileChooser.showOpenDialog(stage);

        if (selectedDirectory == null) {
            path_up_bd.setText("");
        } else {
            path_up_bd.setText(selectedDirectory.getAbsolutePath());
        }

    }

    @FXML
    void config(ActionEvent event) {
        progress.setProgress(0);
        abreconfig();

    }

    @FXML
    void run_dw(ActionEvent event) {
        progress.setProgress(0);
        if (!path_dw_bd.getText().trim().isEmpty()) {

            if (validadadosbd()) {
                
                if (fileexist(path_dw_bd.getText().trim()))
                {
                    progress.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
                    run_up.setDisable(true);
                    bt_up.setDisable(true);
                    run_dw.setDisable(true);
                    bt_dw.setDisable(true);

                    Task getfile = new Task() {
                        @Override
                        protected Object call() throws Exception {
                            String passa = "";

                            Backupgdrive google = new Backupgdrive();
                            try {
                                passa = google.downloadFile(path_dw_bd.getText().trim());
                            } catch (IOException ex) {

                                passa = "Erro ao efetuar o dowload do arquivo. Erro: " + ex;

                            }

                            return passa;
                        }

                        @Override
                        protected void succeeded() {

                            if (getValue().toString().trim().isEmpty()) {

                                progress.setProgress(100);

                            } else {
                                progress.setProgress(0);
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setHeaderText("Erro processo Download.");
                                alert.setContentText(getValue().toString().trim());
                                alert.showAndWait();
                            }

                            run_up.setDisable(false);
                            bt_up.setDisable(false);
                            run_dw.setDisable(false);
                            bt_dw.setDisable(false);

                        }

                    };
                    Thread thrback = new Thread(getfile);
                    thrback.setDaemon(true);
                    thrback.start();
                }

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Erro conta Google Drive");
                alert.setContentText("Necessário configurar sua conta do Google Drive para efetuar o Download !. "
                        + "A seguir será aberto a tela de configuração para configura-lá.");
                alert.showAndWait();
                abreconfig();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Erro ao selecionar pasta");
            alert.setContentText("Necessário selecionar algum diretório para efetuar o Download !");
            alert.showAndWait();
        }

    }

    @FXML
    void run_up(ActionEvent event) {
        progress.setProgress(0);
        if (!path_up_bd.getText().trim().isEmpty()) {
            if (validadadosbd()) {
                progress.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
                run_up.setDisable(true);
                bt_up.setDisable(true);
                run_dw.setDisable(true);
                bt_dw.setDisable(true);

                Task getfile = new Task() {
                    @Override
                    protected Object call() throws Exception {
                        String passa = "";

                        Backupgdrive google = new Backupgdrive();
                        try {
                            passa = google.execCopia(path_up_bd.getText().trim());
                        } catch (IOException ex) {

                            passa = "Erro ao efetuar o upload do arquivo. Erro: " + ex;

                        }

                        return passa;
                    }

                    @Override
                    protected void succeeded() {

                        if (getValue().toString().trim().isEmpty()) {

                            progress.setProgress(100);

                        } else {
                            progress.setProgress(0);
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setHeaderText("Erro processo Upload.");
                            alert.setContentText(getValue().toString().trim());
                            alert.showAndWait();
                        }

                        run_up.setDisable(false);
                        bt_up.setDisable(false);
                        run_dw.setDisable(false);
                        bt_dw.setDisable(false);

                    }

                };
                Thread thrback = new Thread(getfile);
                thrback.setDaemon(true);
                thrback.start();

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Erro conta Google Drive");
                alert.setContentText("Necessário configurar sua conta do Google Drive para efetuar o Upload !. "
                        + "A seguir será aberto a tela de configuração para configura-lá.");
                alert.showAndWait();
                abreconfig();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Erro ao selecionar arquivo");
            alert.setContentText("Necessário selecionar algum arquivo para Upload !");
            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    private boolean validadadosbd() {
        boolean ret = false;

        conn_db conn = new conn_db();
        conn.conectar();
        ResultSet rs = null;

        try {

            rs = conn_db.conn.createStatement().executeQuery("select * from config");
            while (rs.next()) {

                if (!rs.getString("clienteid").trim().isEmpty()
                        && !rs.getString("clientesecret").trim().isEmpty()) {
                    ret = true;
                } else {
                    ret = false;
                }

            }

        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Seleção");
            alert.setContentText("Erro ao selecionar, Erro_1: " + ex);
            alert.showAndWait();
        } finally {
            try {
                rs.close();
            } catch (SQLException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText("Seleção");
                alert.setContentText("Erro ao selecionar, Erro_2: " + ex);
                alert.showAndWait();
            }
        }
        return ret;
    }

    private void abreconfig() {
        Parent root;
        try {
            Stage stagesinc = new Stage();
            root = FXMLLoader.load(getClass().getResource("/fxml/Sinc.fxml"));

            Scene scene = new Scene(root);

            stagesinc.setTitle("Configurações");
            stagesinc.setScene(scene);
            stagesinc.setResizable(false);
            stagesinc.show();

        } catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro ao abrir tela de Configurações");
            alert.setContentText("" + ex);
            alert.showAndWait();
        }
    }

    private boolean fileexist(String pathname) {
        boolean lret = false;

        File file = new File(pathname + "\\Dados.mdb");
        if (file.exists()) {
            
            Alert alert
                    = new Alert(AlertType.WARNING,
                            "Arquivo já existe no caminho especificado. Deseja proseguir ?",
                            ButtonType.YES,
                            ButtonType.NO);
            alert.setTitle("Atenção");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.YES) {
                
                DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss"); 
                Date date = new Date(); 
                
                File fileaux = new File(pathname + "\\Dados_"+dateFormat.format(date)+".mdb");
                file.renameTo(fileaux);
                
                
                lret = true;
            }else
            {
                lret = false;
            }
            
            
        } else {
            lret = true;
        }

        return lret;
    }
}
