/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytune.GUI.Controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author Nijas Hansen
 */
public class FXMLController implements Initializable
{

    @FXML
    private ListView<?> LstPlaylist;
    @FXML
    private Button btnPlay;
    @FXML
    private Button btnPrev;
    @FXML
    private Button btnNext;
    @FXML
    private Slider sldVol;
    @FXML
    private Label lblPlaying;
    @FXML
    private Button btnNewPlaylist;
    @FXML
    private TableView<?> tbvSongs;
    @FXML
    private Slider sldProg;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
    }    

    @FXML
    private void playSongBtn(ActionEvent event)
    {
    }

    @FXML
    private void prevSongBtn(ActionEvent event)
    {
    }

    @FXML
    private void NextSongBtn(ActionEvent event)
    {
    }

    @FXML
    private void volSlider(MouseEvent event)
    {
    }

    @FXML
    private void newPlaylistBtn(ActionEvent event)
    {
    }
    
}
