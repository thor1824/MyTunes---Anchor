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
public class EditPlaylistController implements Initializable
{

    @FXML
    private TextField txtfldEdit;
    @FXML
    private Button btnEdit;
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

    /**
     * 
     * @param event changes the name of the highlighted playlist
     */
    @FXML
    private void editPlaylist(ActionEvent event)
    {
        // TODO
    }

    /**
     * 
     * @param event sends the information to the arraylist with 
     * playlists 
     */
    @FXML
    private void editBtn(ActionEvent event)
    {
       // play.setTitle(Title);?
    }

    /**
     * 
     * @param event closes the window 
     */
    @FXML
    private void cancelBtn(ActionEvent event)
    {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        
        stage.close();
    }
    
}
