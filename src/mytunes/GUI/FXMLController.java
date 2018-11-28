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
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
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
    private TableView<Song> tbvSongs;
    @FXML
    private Slider sldProg;
    @FXML
    private Label lbltime;
    @FXML
    private Button btnEdit;

    //other
    private MediaPlayer mPlayer;
    private MyTunesModel mtModel;
    private int paused = 1;
    private List<Song> activePlaylist;
    private Song activeSong;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            mtModel = new MyTunesModel();

//            activePlaylist = mtModel.getAllSong();
//            tbvSongs.getItems().addAll(activePlaylist);
        } catch (SQLException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }

        TableColumn<Song, String> title = new TableColumn<>();
        title.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTitle()));
        title.setText("Title");
        title.setPrefWidth(350);
        tbvSongs.getColumns().add(title);

        TableColumn<Song, String> artist = new TableColumn<>();
        artist.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getArtist()));
        artist.setText("Artist");
        artist.setPrefWidth(200);
        tbvSongs.getColumns().add(artist);

        //Husk denne skal ændres fra getTitle til genrer i stedet for
        TableColumn<Song, String> genrer = new TableColumn<>();
        genrer.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTitle()));
        genrer.setText("Genre");
        genrer.setPrefWidth(150);
        tbvSongs.getColumns().add(genrer);

        TableColumn<Song, Double> durations = new TableColumn<>();
        durations.setCellValueFactory(c -> new SimpleObjectProperty(c.getValue().getDuration()));
        durations.setText("Duration");
        durations.setPrefWidth(80);
        tbvSongs.getColumns().add(durations);

    }

    @FXML
    private void volSlider(MouseEvent event) {
        double volume = sldVol.getBaselineOffset();
        double max = sldVol.getMax();
        mPlayer.setVolume((volume / max * 100) / 100);
    }

    @FXML
    private void newPlaylistBtn(ActionEvent event) {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("mytunes/GUI/AddPlaylist.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Create Playlist");
            stage.setScene(new Scene(root, 450, 176));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    private void menuAddSong(ActionEvent event) throws SQLException {

        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new ExtensionFilter("mp3 files", "*.mp3"));
        File addedFile = fc.showOpenDialog(null);
        try {

            createSongFromMetadata(addedFile);

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
    private void menuAddAlbum(ActionEvent event) throws SQLException {
        DirectoryChooser dc = new DirectoryChooser();

        File[] files = dc.showDialog(null).listFiles();
        for (File addedFile : files) {
            if (addedFile.getAbsolutePath().contains(".mp3")) {
                try {

                    createSongFromMetadata(addedFile);

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

    private void createSongFromMetadata(File addedFile) throws IOException, SAXException, SQLException, FileNotFoundException, TikaException, NumberFormatException {
        InputStream input = new FileInputStream(addedFile);
        ContentHandler handler = new DefaultHandler();
        Metadata metadata = new Metadata();
        Parser parser = new Mp3Parser();
        ParseContext parseCtx = new ParseContext();
        parser.parse(input, handler, metadata, parseCtx);
        input.close();

        String filePath = addedFile.getAbsolutePath();
        String title = metadata.get("title");
        String artist = metadata.get("xmpDM:artist");
        double duration = Double.parseDouble(metadata.get("xmpDM:duration"));
        String genre = metadata.get("xmpDM:genre");

        mtModel.createSong(filePath, title, artist, duration, genre);
    }

    private void playSong(Song song) {
        Media media = new Media(song.getFilePath());
        mPlayer = new MediaPlayer(media);
        mPlayer.play();
    }

    @FXML
    private void editPlaylist(ActionEvent event) {
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("mytunes/GUI/EditPlaylist.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Edit Playlist");
            stage.setScene(new Scene(root, 450, 176));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
