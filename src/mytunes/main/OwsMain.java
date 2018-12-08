package mytunes.main;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mytunes.GUI.Controller.Music.MyTunesController;

/**
 *
 * @author pgn
 */
public final class OwsMain extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mytunes/GUI/View/Music/MyTunes.FXML"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        MyTunesController mtCon = loader.getController();
        mtCon.setStage(stage);
        stage.setTitle("MyTunes");
        stage.show();
        
        
    }

    /**
     * @param args the command line argument
     */
    public static void main(String[] args) throws IOException {
        launch(args);

    }

}
