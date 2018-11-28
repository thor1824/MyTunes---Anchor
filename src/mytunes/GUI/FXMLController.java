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
import static java.lang.Math.floor;
import static java.lang.String.format;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.input.MouseButton;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Duration;
import mytunes.BE.Song;
import javafx.beans.Observable;

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
    @FXML
    private ProgressBar songProg;
    @FXML
    private MediaView mview;

    //other
    private MediaPlayer mPlayer;
    private MyTunesModel mtModel;
    private int paused = 1;
    private List<Song> activePlaylist;
    private Song activeSong;
    private Duration duration;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //Collum Init
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

        TableColumn<Song, String> genrer = new TableColumn<>();
        genrer.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getGenre()));
        genrer.setText("Genre");
        genrer.setPrefWidth(150);
        tbvSongs.getColumns().add(genrer);

        TableColumn<Song, Double> durations = new TableColumn<>();
        durations.setCellValueFactory(c -> new SimpleObjectProperty(c.getValue().getDuration()));
        durations.setText("Duration");
        durations.setPrefWidth(80);
        tbvSongs.getColumns().add(durations);

        //other init
        try {
            mtModel = new MyTunesModel();
            String path = new File("src/Music/Dee_Yan-Key_-_01_-_That_aint_Chopin.mp3").getAbsolutePath();
            path.replace("\\", "/").replaceAll(" ", "%20");
            Media media = new Media(new File(path).toURI().toString());
            mPlayer = new MediaPlayer(media);
//            activeSong = activePlaylist.get(0);
//            activePlaylist = mtModel.getAllSong();
//            tbvSongs.getItems().addAll(activePlaylist);
        } catch (SQLException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }

        //timer
        updateTimer();

        //slider
        updateSlide();

        //Event init
        tbvSongs.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2) {
                        Song song = tbvSongs.getSelectionModel().getSelectedItem();
                        playSong(song);
                    }
                }
            }
        });
    }

    @FXML
    private void volSlider(MouseEvent event) {
        if (mPlayer != null) {
            double volume = sldVol.getValue();
            double max = sldVol.getMax();
            mPlayer.setVolume((volume / max * 100) / 100);
        }

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
                mPlayer.pause();
                paused = 1;
                break;
            case 1:

                btnPlay.setImage(pause);
                playSong(null);
                paused = 0;
                break;

        }
    }

    @FXML
    private void NextSongBtn(MouseEvent event) {
        int nextIndex = activePlaylist.indexOf(activeSong) + 1;
        if ((activePlaylist.size() - 1) < nextIndex) {
            nextIndex = 0;
        }
        activeSong = activePlaylist.get(nextIndex);
        playSong(activeSong);
    }

    @FXML
    private void prevSongBtn(MouseEvent event) {
        int previousIndex = activePlaylist.indexOf(activeSong) - 1;
        if (previousIndex > 0) {
            previousIndex = activePlaylist.size() - 1;
        }
        activeSong = activePlaylist.get(previousIndex);
        playSong(activeSong);
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

    private void updateSlide() {
        sldProg.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                if (sldProg.isValueChanging()) {
                    // multiply duration by percentage calculated by slider position
                    if (duration != null) {
                        mPlayer.seek(duration.multiply(sldProg.getValue() / 100.0));
                    }
                    updateValues();

                }
            }
        });

        mPlayer.currentTimeProperty().addListener(new ChangeListener() {

            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                updateValues();
            }
        });
    }

    private void updateTimer() {
        mPlayer.currentTimeProperty().addListener((Observable ov) -> {
            updateValues();
        });

        mPlayer.setOnReady(() -> {
            duration = mPlayer.getMedia().getDuration();
            updateValues();
        });
    }

    private void updateValues() {
        if (lbltime != null && sldProg != null && duration != null) {
            Platform.runLater(new Runnable() {
                public void run() {
                    Duration currentTime = mPlayer.getCurrentTime();
                    lbltime.setText(formatTime(currentTime, duration));
                    sldProg.setDisable(duration.isUnknown());
                    if (!sldProg.isDisabled() && duration.greaterThan(Duration.ZERO) && !sldProg.isValueChanging()) {
                        sldProg.setValue(currentTime.divide(duration).toMillis() * 100.0);
                    }

                }
            });
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
        mPlayer.stop();
//        String path = new File(song.getFilePath()).getAbsolutePath();
        String path = new File("src/Music/Dee_Yan-Key_-_01_-_That_aint_Chopin.mp3").getAbsolutePath();
        path.replace("\\", "/").replaceAll(" ", "%20");
        Media media = new Media(new File(path).toURI().toString());
        mPlayer = new MediaPlayer(media);
        updateValues();
        mview = new MediaView(mPlayer);

        mPlayer.setOnReady(new Runnable() {
            @Override
            public void run() {
                updateSlide();
                updateTimer();
                mview.setMediaPlayer(mPlayer);
                mPlayer.play();
            }
        });

        //lblPlaying.setText(song.getTitle());
//        volSlider(null);
//        System.out.println(mPlayer.getTotalDuration());
    }

    private static String formatTime(Duration elapsed, Duration duration) {
        int intElapsed = (int) floor(elapsed.toSeconds());
        int elapsedHours = intElapsed / (60 * 60);
        if (elapsedHours > 0) {
            intElapsed -= elapsedHours * 60 * 60;
        }
        int elapsedMinutes = intElapsed / 60;
        int elapsedSeconds = intElapsed - elapsedHours * 60 * 60
                - elapsedMinutes * 60;

        if (duration.greaterThan(Duration.ZERO)) {
            int intDuration = (int) floor(duration.toSeconds());
            int durationHours = intDuration / (60 * 60);
            if (durationHours > 0) {
                intDuration -= durationHours * 60 * 60;
            }
            int durationMinutes = intDuration / 60;
            int durationSeconds = intDuration - durationHours * 60 * 60
                    - durationMinutes * 60;
            if (durationHours > 0) {
                return format("%d:%02d:%02d/%d:%02d:%02d",
                        elapsedHours, elapsedMinutes, elapsedSeconds,
                        durationHours, durationMinutes, durationSeconds);
            } else {
                return format("%02d:%02d/%02d:%02d",
                        elapsedMinutes, elapsedSeconds, durationMinutes,
                        durationSeconds);
            }
        } else {
            if (elapsedHours > 0) {
                return format("%d:%02d:%02d", elapsedHours,
                        elapsedMinutes, elapsedSeconds);
            } else {
                return format("%02d:%02d", elapsedMinutes,
                        elapsedSeconds);
            }
        }
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
