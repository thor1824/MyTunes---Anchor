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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private ImageView btnPlay;
    @FXML
    private ImageView btnPrev;
    @FXML
    private ImageView btnNext;
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
    @FXML
    private ImageView btnpause;
    @FXML
    private Label lbltime;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
    }    



    @FXML
    private void volSlider(MouseEvent event)
    {
    }

    @FXML
    private void newPlaylistBtn(ActionEvent event)
    {
    }

    @FXML
    private void playSongBtn(MouseEvent event)
    {
       btnPlay.setOpacity(0);
        
       if(btnPlay.getOpacity() == 0)
       {
           btnPlay.setOpacity(1); 
       }
    }

    @FXML
    private void NextSongBtn(MouseEvent event)
    {
    }

    @FXML
    private void prevSongBtn(MouseEvent event)
    {
    }
    
}
