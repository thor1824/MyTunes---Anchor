/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.GUI;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import mytunes.BE.Song;

/**
 * FXML Controller class
 *
 * @author Bruger
 */
public class EditSongInfoController implements Initializable
{

    @FXML
    private Button btnOkEditSong;
    @FXML
    private Button btnCancelEditSong;
    @FXML
    private Button btnOpenFile;
    @FXML
    private TextField txtSongEditTitle;
    @FXML
    private TextField txtSongEditArtist;
    @FXML
    private TextField txtSongEditGenre;
    @FXML
    private TextField txtFilePath;

    private static Song songInfo;
    private static MyTunesModel mtModel;

    public static void setMtModel(MyTunesModel mtModel)
    {
        EditSongInfoController.mtModel = mtModel;
    }
    
    
    public static void setSongInfo(Song songInfo)
    {
        EditSongInfoController.songInfo = songInfo;
    }
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        txtSongEditTitle.setText(songInfo.getTitle());
        txtSongEditArtist.setText(songInfo.getArtist());
        txtSongEditGenre.setText(songInfo.getGenre());
    }    

    @FXML
    private void btnOkEdit(ActionEvent event) throws SQLException
    {
        songInfo.setTitle(txtSongEditTitle.getText());
        songInfo.setArtist(txtSongEditArtist.getText());
        songInfo.setGenre(txtSongEditGenre.getText());
        
        mtModel.updateSong(songInfo);
        
        Stage stage = (Stage) btnOkEditSong.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void btnCancelEdit(ActionEvent event)
    {
        Stage stage = (Stage) btnCancelEditSong.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void btnOpenFile(ActionEvent event)
    {
        FileChooser efc = new FileChooser();
        efc.getExtensionFilters().addAll(new ExtensionFilter("mp3 files", "*.mp3"));
        File addedFile = efc.showOpenDialog(null);
        
        txtFilePath.setText(addedFile.getAbsolutePath());
    }

    @FXML
    private void txtSongTitleEdit(ActionEvent event)
    {
    }

    @FXML
    private void txtSongArtistEdit(ActionEvent event)
    {
    }

    @FXML
    private void txtSongGenreEdit(ActionEvent event)
    {
    }

    @FXML
    private void txtFilePathEdit(ActionEvent event)
    {
    }
    
}
