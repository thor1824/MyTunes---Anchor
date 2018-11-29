/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.GUI;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mytunes.DAL.DB.PlaylistDAO;

/**
 * FXML Controller class
 *
 * @author Nijas Hansen
 */
public class AddPlaylistController implements Initializable
{

    @FXML
    private TextField txtField;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnCancel;

    private PlaylistDAO play;
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
    private void addPlaylistBTN(ActionEvent event) throws SQLException
    {
           play.createPlaylist(txtField.getText());
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
