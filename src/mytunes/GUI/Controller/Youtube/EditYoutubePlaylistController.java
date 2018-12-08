/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.GUI.Controller.Youtube;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mytunes.BE.YoutubePlaylist;
import mytunes.GUI.Model.YouTubePlayerModel;

/**
 * FXML Controller class
 *
 * @author Thorbjørn Schultz Damkjær
 */
public class EditYoutubePlaylistController implements Initializable
{

    @FXML
    private TextField txtfldEdit;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnCancel;

    private YoutubePlaylist yPlaylist;
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
    private void editBtn(ActionEvent event)
    {
        yPlaylist.setTitle(txtfldEdit.getText());
        Stage stage = (Stage) btnEdit.getScene().getWindow();
        stage.close();

    }

    @FXML
    private void cancelBtn(ActionEvent event)
    {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    public void setyPlaylist(YoutubePlaylist yPlaylist)
    {
        this.yPlaylist = yPlaylist;
        txtfldEdit.setText(yPlaylist.getTitle());
    }

    public void setYtModel(YouTubePlayerModel ytModel)
    {
        this.ytModel = ytModel;
    }

}
