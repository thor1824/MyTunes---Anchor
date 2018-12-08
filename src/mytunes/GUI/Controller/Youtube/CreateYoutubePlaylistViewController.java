/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.GUI.Controller.Youtube;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mytunes.GUI.Model.YouTubePlayerModel;

/**
 * FXML Controller class
 *
 * @author Thorbjørn Schultz Damkjær
 */
public class CreateYoutubePlaylistViewController implements Initializable
{

    @FXML
    private TextField txtPlaylistName;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnCancel;

    private YouTubePlayerModel ytModel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
    }

    @FXML
    private void addPlaylistBTN(ActionEvent event)
    {
        try
        {
            ytModel.createYoutubePlaylist(txtPlaylistName.getText());
            Stage stage = (Stage) btnAdd.getScene().getWindow();
            stage.close();
        } catch (SQLException ex)
        {
            Logger.getLogger(CreateYoutubePlaylistViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void cancelPlaylist(ActionEvent event)
    {
        Stage stage = (Stage) btnCancel.getScene().getWindow();

        stage.close();
    }

    public void setYoutubeModel(YouTubePlayerModel ytm)
    {
        ytModel = ytm;
    }

}
