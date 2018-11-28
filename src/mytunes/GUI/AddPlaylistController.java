/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.GUI;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mytunes.BE.Playlist;

/**
 * FXML Controller class
 *
 * @author Nijas Hansen
 */
public class AddPlaylistController implements Initializable
{

    @FXML
    private TextField txtPlaylistName;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnCancel;

    private Playlist play;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
    }    

    @FXML
    private void txtField(ActionEvent event)
    {
    }

    @FXML
    private void addPlaylistBTN(ActionEvent event)
    {
       // play.addToPlaylist(song);? 
    }

    /**
     * 
     * @param event closes the window
     */
    @FXML
    private void cancelPlaylist(ActionEvent event)
    {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        
        stage.close();
    }
    
}
