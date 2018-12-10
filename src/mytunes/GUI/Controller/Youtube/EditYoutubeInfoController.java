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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import mytunes.BE.YoutubeVideo;
import mytunes.GUI.Model.YouTubePlayerModel;

/**
 * FXML Controller class
 *
 * @author Thorbjørn Schultz Damkjær
 */
public class EditYoutubeInfoController implements Initializable
{

    @FXML
    private Button btnOkEditSong;
    @FXML
    private Button btnCancelEditSong;
    @FXML
    private TextField txtSongEditTitle;
    @FXML
    private TextField txtSongEditArtist;
    @FXML
    private Text edittxt;

    private YoutubeVideo yVideo;
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
    private void btnOkEdit(ActionEvent event) throws SQLException
    {
        yVideo.setTitle(txtSongEditTitle.getText());
        yVideo.setArtist(txtSongEditArtist.getText());
        ytModel.updateYoutubeVideo(yVideo);
        Stage stage = (Stage) btnOkEditSong.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void btnCancelEdit(ActionEvent event)
    {
        Stage stage = (Stage) btnCancelEditSong.getScene().getWindow();
        stage.close();
    }

    public void setyVideo(YoutubeVideo yVideo)
    {
        this.yVideo = yVideo;
        txtSongEditTitle.setText(yVideo.getTitle());
        txtSongEditArtist.setText(yVideo.getArtist());
    }

    public void setYtModel(YouTubePlayerModel ytModel)
    {
        this.ytModel = ytModel;
    }

}
