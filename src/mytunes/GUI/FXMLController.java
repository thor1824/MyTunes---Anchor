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
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import mytunes.BE.Song;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * FXML Controller class
 *
 * @author Nijas Hansen
 */
public class FXMLController implements Initializable {
    //FXML
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
    private Label lbltime;
    
    //other
    
    private int paused = 1;
    private List<Song> activePlaylist;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void volSlider(MouseEvent event) {
    }

    @FXML
    private void newPlaylistBtn(ActionEvent event) {
    }

    @FXML
    private void playSongBtn(MouseEvent event) {
        Image pause = new Image("Pics/PauseBtn.png");
        Image play = new Image("Pics/Playbtn.png");
        switch (paused) {
            case 0:
                btnPlay.setImage(play);
                paused = 1;
                break;
            case 1:
                btnPlay.setImage(pause);
                paused = 0;
                break;
        }
    }

    @FXML
    private void NextSongBtn(MouseEvent event) {
    }

    @FXML
    private void prevSongBtn(MouseEvent event) {
    }

    @FXML
    private void menuAddSong(ActionEvent event) {

        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new ExtensionFilter("mp3 files", "*.mp3"));
        File addedFile = fc.showOpenDialog(null);
        try {

            InputStream input = new FileInputStream(addedFile);
            ContentHandler handler = new DefaultHandler();
            Metadata metadata = new Metadata();
            Parser parser = new Mp3Parser();
            ParseContext parseCtx = new ParseContext();
            parser.parse(input, handler, metadata, parseCtx);
            input.close();

            String[] metadataNames = metadata.names();

            for (String name : metadataNames) {
                System.out.println(name + ": " + metadata.get(name));
            }

            System.out.println("----------------------------------------------");
            System.out.println("Title: " + metadata.get("title"));
            System.out.println("Artists: " + metadata.get("xmpDM:artist"));
            System.out.println("Composer : " + metadata.get("xmpDM:composer"));
            System.out.println("Genre : " + metadata.get("xmpDM:genre"));
            System.out.println("Album : " + metadata.get("xmpDM:album"));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (TikaException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void menuAddAlbum(ActionEvent event) {
        DirectoryChooser dc = new DirectoryChooser();
        
        File[] files = dc.showDialog(null).listFiles();
        for (File addedFile : files) {
            if (addedFile.getAbsolutePath().contains(".mp3")) {
                try {

                    InputStream input = new FileInputStream(addedFile);
                    ContentHandler handler = new DefaultHandler();
                    Metadata metadata = new Metadata();
                    Parser parser = new Mp3Parser();
                    ParseContext parseCtx = new ParseContext();
                    parser.parse(input, handler, metadata, parseCtx);
                    input.close();

                    String[] metadataNames = metadata.names();

                    for (String name : metadataNames) {
                        System.out.println(name + ": " + metadata.get(name));
                    }

                    System.out.println("----------------------------------------------");
                    System.out.println("Title: " + metadata.get("title"));
                    System.out.println("Artists: " + metadata.get("xmpDM:artist"));
                    System.out.println("Composer : " + metadata.get("xmpDM:composer"));
                    System.out.println("Genre : " + metadata.get("xmpDM:genre"));
                    System.out.println("Album : " + metadata.get("xmpDM:album"));

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (TikaException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
