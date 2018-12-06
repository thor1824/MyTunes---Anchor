/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.GUI.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.MediaView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Thorbjørn Schultz Damkjær
 */
public class YouTubePlayerController implements Initializable {

    @FXML
    private Label lblLibrary;
    @FXML
    private Label lblYoutube;
    @FXML
    private TableView<?> tbvPlayllist;
    @FXML
    private TableColumn<?, ?> tbvPlaylistName;
    @FXML
    private MediaView mview;
    @FXML
    private Label lblPlaying;
    @FXML
    private WebView webview;
    @FXML
    private TableColumn<?, ?> colTitle;
    @FXML
    private TableColumn<?, ?> colArtist;
    @FXML
    private TableColumn<?, ?> colDuration;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String content_Url = "<iframe width=\"900\" height=\"485\" src=\""
                + "http://www.youtube.com/embed/4z9TdDCWN7g"
                + "?&autoplay=1&state=enabled&cc_load_policy=1&playlist=mItWfoNnUag"
                + "\" frameborder=\"0\" allowfullscreen></iframe>";
        Label location = new Label();

        WebEngine webEngine = webview.getEngine();

        webEngine.loadContent(content_Url);

//        StackPane root = new StackPane();
//        root.getChildren().add(webView);
        location.textProperty().bind(webEngine.locationProperty());
        location.textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("AHHH");
        });

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("mytunes/GUI/View/YouTubePlayer.fxml"));

        Parent root;
        try {
            root = loader.load();
        
        Stage stage = new Stage();
        stage.setTitle("Create Playlist");
        stage.setScene(new Scene(root));
        stage.setOnCloseRequest(event ->
    {
        System.out.println("CLOSING");
        webEngine.load("");
    });
        } catch (IOException ex) {
            Logger.getLogger(YouTubePlayerController.class.getName()).log(Level.SEVERE, null, ex);
        }

//        Scene scene = new Scene(root, 580, 335);
////        Scene scene = new Scene(new VBox(10, location, webView));
//        primaryStage.setTitle("http://java-buddy.blogspot.com/");
//        primaryStage.setScene(scene);
//        primaryStage.show();
    }

    @FXML
    private void menuAddSong(ActionEvent event) {
    }

    @FXML
    private void menuAddAlbum(ActionEvent event) {
    }

    @FXML
    private void newPlaylistBtn(ActionEvent event) {
    }

    @FXML
    private void prevSongBtn(MouseEvent event) {
    }

    @FXML
    private void NextSongBtn(MouseEvent event) {
    }

}
