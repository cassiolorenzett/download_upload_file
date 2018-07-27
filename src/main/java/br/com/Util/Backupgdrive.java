/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.Util;

import com.google.api.client.googleapis.media.MediaHttpDownloader;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.Drive.Files.Get;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;

/**
 * Utilizada para fazer o backup das informações com o google drive.
 *
 * @author cassio
 */
public class Backupgdrive {

    private static String msgerr = "";
    private static String client_id = "";
    private static String cliente_secret = "";
    //busca as credenciais cadastradas na base de dados, caso estejam cadastradas.

    //executa o processo de backup
    public String execCopia(String cam) throws IOException {
        Getcredencialgdrive credenc = new Getcredencialgdrive();
        String msgexec = "";

        if (credenc.getCredencial().size() > 0) {

            Googledrive gdrive = new Googledrive();

            gdrive.setClientsecrets(credenc.getCredencial().get(0).toString(), credenc.getCredencial().get(1).toString());

            // Build a new authorized API client service.
            Drive service = gdrive.getDrive();
            String delet = deleteFile(service);

            if (delet.isEmpty()) {
                msgexec = uploadFile(service, cam);
            } else {
                msgexec = delet;
            }

        }

        return msgexec;

    }

    //deleta todos os arquivos coinwalletoff.xml de backup para fazer o upload do atual que se esta enviando
    private static String deleteFile(Drive service) throws IOException {
        String pageToken = null;
        String retdel = "";

        do {
            FileList result = service.files().list()
                    .setQ("title contains 'Dadoscharmaq' and trashed = false")
                    .setSpaces("drive")
                    .setFields("nextPageToken, items(id, title)")
                    .setPageToken(pageToken)
                    .execute();

            for (File file : result.getItems()) {

                try {
                    service.files().delete(file.getId()).execute();
                    retdel = "";
                } catch (IOException e) {
                    retdel = "" + e;
                }

            }
            pageToken = result.getNextPageToken();
        } while (pageToken != null);

        return retdel;

    }


    //efetua o upload das informações cadastradas no software para o google drive para backup.
    private static String uploadFile(Drive service, String cam) {
        
        //printFile(service);
        File body = new File();
        body.setTitle("Dadoscharmaq");
        body.setDescription("Dadoscharmaq");
        body.setMimeType("application/unknown");

      
        // File's content.
        
        java.io.File fileContent = new java.io.File(cam);

        FileContent mediaContent = new FileContent("application/unknown", fileContent);
        try {
            File file = service.files().insert(body, mediaContent).execute();

            // Uncomment the following line to print the File ID.
            //System.out.println("File ID: " + file.getId());
            return "";
        } catch (IOException e) {

            return "erro: "+e;
        }
       // return ""; 
   }

    /**
     * Baixa o arquivo de backup de carteira do google drive. Presenta na função
     * baixa_gdrive()
     *
     * @return void
     * @see IOException
     */
    public String downloadFile(String cam) throws IOException {
        String ret = "";
        
        Getcredencialgdrive credenc = new Getcredencialgdrive();

        if (credenc.getCredencial().size() > 0) {

            Googledrive gdrive = new Googledrive();
            gdrive.setClientsecrets(credenc.getCredencial().get(0).toString(), credenc.getCredencial().get(1).toString());
            Drive service = gdrive.getDrive();
            String pageToken = null;
            ret = "Nenhum arquivo foi identificado !";
            do {
                FileList result = service.files().list()
                        .setQ("title contains 'Dadoscharmaq' and trashed = false")
                        .setSpaces("drive")
                        .setFields("nextPageToken, items(id, title)")
                        .setPageToken(pageToken)
                        .execute();

                for (File file : result.getItems()) {
                     
                    try {
                        File fileToDownload = service.files().get(file.getId()).execute();
                        GenericUrl u = new GenericUrl(fileToDownload.getDownloadUrl());
                        Get request = service.files().get(fileToDownload.getId());
                        //String name = fileToDownload.getTitle(); 
                        OutputStream bos = new FileOutputStream(cam + "/Dados.mdb");
                        MediaHttpDownloader mhd = request.getMediaHttpDownloader();

                        mhd.setDirectDownloadEnabled(true);
                        mhd.download(u, bos);
                        bos.close();
                       
                        ret = "";
                        
                    } catch (IOException e) {
                       
                        System.err.println("test2");
                        ret  =  "erro ao efetuar o download do arquivo: " + e;
                        
                    }

                }
                pageToken = result.getNextPageToken();
            } while (pageToken != null);
        }
        
        return ret;
    }

}
