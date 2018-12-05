/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.GUI.Controller;

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
import java.util.Random;
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
import javafx.collections.ObservableList;
import javafx.event.EventType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import mytunes.BE.Playlist;
import mytunes.GUI.Model.MyTunesModel;

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
public class MyTunesController implements Initializable {

    //FXML
//    @FXML
//    private ListView<Playlist> LstPlaylist;
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
    private TableView<Song> tbvSongs;
    @FXML
    private Slider sldProg;
    @FXML
    private Label lbltime;
    @FXML
    private ProgressBar songProg;
    @FXML
    private MediaView mview;
    @FXML
    private Label mLibrary;

    //other
    private MediaPlayer mPlayer;
    private MyTunesModel mtModel;
    private final int PAUSED = 0;
    private final int PLAYING = 1;
    private int state = PAUSED;
    private boolean onPlaylist = false;
    private ObservableList<Playlist> allPlaylist;
    private ObservableList<Song> allSongs;
    private ObservableList<Song> activeObvPlaylist;
    private Playlist activePlaylist;
    private Song activeSong;
    private Duration duration;
    private final int SHUFFLE_ON = 1;
    private final int SHUFFLE_OFF = 0;
    private int shuffleState = SHUFFLE_OFF;
    private final int repeat_ON = 1;
    private final int repeat_OFF = 0;
    private int repeatState = repeat_OFF;
    private ContextMenu cmSong;
    private MenuItem deleteSong;
    private MenuItem deleteSongFromPlist;
    @FXML
    private Label mLibrary1;
    @FXML
    private Label mLibrary2;
    @FXML
    private ImageView btnShuffle;
    @FXML
    private ImageView btnRepeat;
    @FXML
    private TableView<Playlist> tbvPlayllist;
    @FXML
    private TableColumn<Playlist, String> tbvPlaylistName;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //Collum Init
        TableColumn<Song, String> title = new TableColumn<>();
        title.setCellValueFactory(c -> c.getValue().getTitleProperty());
        title.setText("Title");
        title.setPrefWidth(350);
        tbvSongs.getColumns().add(title);

        TableColumn<Song, String> artist = new TableColumn<>();
        artist.setCellValueFactory(c -> c.getValue().getArtistProperty());
        artist.setText("Artist");
        artist.setPrefWidth(200);
        tbvSongs.getColumns().add(artist);

        TableColumn<Song, String> genrer = new TableColumn<>();
        genrer.setCellValueFactory(c -> c.getValue().getGenreProperty());
        genrer.setText("Genre");
        genrer.setPrefWidth(150);
        tbvSongs.getColumns().add(genrer);

        TableColumn<Song, Double> durations = new TableColumn<>();
        durations.setCellValueFactory(c -> new SimpleObjectProperty(c.getValue().getDuration()));
        durations.setText("Duration");
        durations.setPrefWidth(80);
        tbvSongs.getColumns().add(durations);

        tbvSongs.setPlaceholder(new Label("Playlist is Empty"));
        tbvPlaylistName.setCellValueFactory(c -> c.getValue().getTitleProperty());

        // slider
        sldVol.setValue(50);

        //other init
        try {
            mtModel = new MyTunesModel();

            allSongs = mtModel.getAllSong();
            activeObvPlaylist = allSongs;
            tbvSongs.setItems(activeObvPlaylist);

            allPlaylist = mtModel.getAllPlaylists();
            tbvPlayllist.setItems(allPlaylist);

            if (activeObvPlaylist.size() > 0) {
                activeSong = activeObvPlaylist.get(0);
                setSongElements(activeSong);
            }

        } catch (SQLException ex) {
            Logger.getLogger(MyTunesController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex)
        {
            Logger.getLogger(MyTunesController.class.getName()).log(Level.SEVERE, null, ex);
        }

        // timer
        // ContextMenu
        // Create cmSong
        cmSong = new ContextMenu();

        MenuItem playSong = new MenuItem("Play Song");
        playSong.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                Song song = tbvSongs.getSelectionModel().getSelectedItem();
                playSong(song);
            }
        });
        
         deleteSongFromPlist = new MenuItem("Delete song from playlist");
        deleteSongFromPlist.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                Song song = tbvSongs.getSelectionModel().getSelectedItem();
                try
                {
                    mtModel.deleteFromPlayist(song, activePlaylist);
                } catch (SQLException ex)
                {
                    Logger.getLogger(MyTunesController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        MenuItem editSong = new MenuItem("Edit Song");
        editSong.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    editSong();
                } catch (IOException ex) {
                    Logger.getLogger(MyTunesController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        deleteSong = new MenuItem("Delete Song From Library");
        deleteSong.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                Song song = tbvSongs.getSelectionModel().getSelectedItem();
                System.out.println("he");
                try {
                    mtModel.deleteSong(song);
                } catch (SQLException ex) {
                    Logger.getLogger(MyTunesController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });

        Menu addtoPlaylist = new Menu("Add to: ");
        genratePlaylistMenuItems(addtoPlaylist);

        // Create cmPlaylist
        ContextMenu cmPlaylist = new ContextMenu();

        MenuItem plChoose = new MenuItem("Choose Playlist");
        plChoose.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                Playlist playlist = tbvPlayllist.getSelectionModel().getSelectedItem();
                changeMusicList(playlist.getSongs());
            }
        });

        MenuItem plDelete = new MenuItem("Delete Playlist");
        plDelete.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                Playlist playlist = tbvPlayllist.getSelectionModel().getSelectedItem();
                try {
                    mtModel.deletePlayliste(playlist);
                } catch (SQLException ex) {
                    Logger.getLogger(MyTunesController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        MenuItem plEdit = new MenuItem("Edit Playlist");
        plEdit.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                Playlist playlist = tbvPlayllist.getSelectionModel().getSelectedItem();
                editPlaylist(null);
            }
        });

        // Add MenuItem to ContextMenus
        cmSong.getItems().addAll(playSong, editSong, deleteSong, addtoPlaylist);
        cmPlaylist.getItems().addAll(plChoose, plDelete, plEdit);

        // Right Click
        // When user right-click on a Song
        tbvSongs.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {

            @Override
            public void handle(ContextMenuEvent event) {
                cmPlaylist.hide();
                cmSong.setMinWidth(100);
                if (onPlaylist) {

                } else {
                }

                cmSong.show(tbvSongs, event.getScreenX(), event.getScreenY());
            }
        });

        // When user right-click on a Playlist
        tbvPlayllist.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {

            @Override
            public void handle(ContextMenuEvent event) {
                cmSong.hide();
                cmPlaylist.show(tbvSongs, event.getScreenX(), event.getScreenY());
            }
        });

        // Double-click
        // Double-click on Song to play
        tbvSongs.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2 && tbvSongs.getSelectionModel().getSelectedItem() != null) {
                        Song song = tbvSongs.getSelectionModel().getSelectedItem();
                        int focusIndex = tbvSongs.getSelectionModel().getSelectedIndex();
                        tbvSongs.getFocusModel().focus(focusIndex);
                        tbvSongs.requestFocus();
                        playSong(song);
                        tbvSongs.getSelectionModel().clearSelection();
                        

                    }
                }
            }
        });

        // Double-click on Playlist to set Active
        tbvPlayllist.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2) {
                        Playlist playlist = tbvPlayllist.getSelectionModel().getSelectedItem();
                        activePlaylist = playlist;
                        onPlaylist = true;
                        changeMusicList(playlist.getSongs());
                        

                    }
                }
            }
        });

        // Double-click on Music Library to see all songs
        mLibrary.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2) {
                        onPlaylist = false;
                        changeMusicList(allSongs);
                        
                    }
                }
            }
        });

    }

    private void editSong() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("mytunes/GUI/View/EditSongInfo.fxml"));

        EditSongInfoController editCon = loader.getController();
        editCon.setMtModel(mtModel);
        editCon.setSongInfo(tbvSongs.getSelectionModel().getSelectedItem());

        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Edit song info");
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void changeMusicList(ObservableList list) {
        activeObvPlaylist = list;
        tbvSongs.setItems(activeObvPlaylist);
        if (onPlaylist)
        {
            cmSong.getItems().add(deleteSongFromPlist);
            cmSong.getItems().remove(deleteSong);
        }
        else
        {
            cmSong.getItems().add(deleteSong);
            cmSong.getItems().remove(deleteSongFromPlist);
        }
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
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("mytunes/GUI/View/AddPlaylist.fxml"));

            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Create Playlist");
            stage.setScene(new Scene(root, 450, 176));
            stage.show();

            AddPlaylistController addCon = loader.getController();
            addCon.setMtModel(mtModel);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void genratePlaylistMenuItems(Menu menuAdd) {
        menuAdd.getItems().clear();
        for (Playlist playlist : allPlaylist) {
            String title = playlist.getTitle();
            MenuItem playlistAdd = new MenuItem(title);

            playlistAdd.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    Song song = tbvSongs.getSelectionModel().getSelectedItem();
                    try {
                        mtModel.addSongToPlaylist(song, playlist);
                    } catch (SQLException ex) {
                        Logger.getLogger(MyTunesController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            menuAdd.getItems().add(playlistAdd);

            
        }
    }

    @FXML
    private void playSongBtn(MouseEvent event) {

        switch (state) {
            case PAUSED:
                mediaPlay();
                break;
            case PLAYING:
                mediaPause();
                break;
        }
    }

    private void mediaPlay() {
        Image pause = new Image("mytunes/GUI/View/Resouces/icons/icons8-pause-30.png");
        btnPlay.setImage(pause);
        mPlayer.play();
        state = PLAYING;

    }

    private void mediaPause() {
        Image play = new Image("mytunes/GUI/View/Resouces/icons/icons8-play-30.png");
        btnPlay.setImage(play);
        mPlayer.pause();
        state = PAUSED;

    }

    @FXML
    private void NextSongBtn(MouseEvent event) {
        int nextIndex = activeObvPlaylist.indexOf(activeSong) + 1;
        if ((activeObvPlaylist.size() - 1) < nextIndex) {
            nextIndex = 0;
        }
        activeSong = activeObvPlaylist.get(nextIndex);
        playSong(activeSong);
    }

    @FXML
    private void prevSongBtn(MouseEvent event) {
        int previousIndex = activeObvPlaylist.indexOf(activeSong) - 1;
        if (previousIndex < 0) {
            previousIndex = activeObvPlaylist.size() - 1;
        }
        activeSong = activeObvPlaylist.get(previousIndex);
        playSong(activeSong);
    }

    @FXML
    private void menuAddSong(ActionEvent event) throws SQLException {

        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new ExtensionFilter("mp3 files", "*.mp3"));
        fc.setInitialDirectory(new File("src"));
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
        sldProg.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                    Number old_val, Number new_val) {
                songProg.setProgress(new_val.doubleValue() / 100);
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

        String filePath = addedFile.getPath();
        System.out.println(filePath);
        String title = metadata.get("title");

        String artist = metadata.get("xmpDM:artist");
        double duration = Double.parseDouble(metadata.get("xmpDM:duration"));
        String genre = metadata.get("xmpDM:genre");
        boolean added = mtModel.createSong(filePath, title, artist, duration, genre);
        if (added) {
            System.out.println("Song added to Library");
        } else {
            System.out.println(title + ": already exsist");
        }
    }

    private void playSong(Song song) {
        mPlayer.stop();
        activeSong = song;
        setSongElements(song);
        mediaPlay();

    }

    private void setSongElements(Song song) {
        String path = new File(song.getFilePath()).getAbsolutePath();
        path.replace("\\", "/").replaceAll(" ", "%20");
        Media media = new Media(new File(path).toURI().toString());

        mPlayer = new MediaPlayer(media);

        mview = new MediaView(mPlayer);

        mPlayer.setOnReady(new Runnable() {
            @Override
            public void run() {

                mview.setMediaPlayer(mPlayer);

            }
        });

        lblPlaying.setText("now Playing...  " + song.getTitle());
        volSlider(null);
        updateValues();
        updateSlide();
        updateTimer();
        checkState();
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

    private void editPlaylist(ActionEvent event) {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(("mytunes/GUI/View/EditPlaylist.fxml")));

            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Edit Playlist");
            stage.setScene(new Scene(root));
            stage.show();

            EditPlaylistController editCon = loader.getController();
            editCon.setMtModel(mtModel);
            editCon.setPlaylist(tbvPlayllist.getSelectionModel().getSelectedItem());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void jumpTo(MouseEvent event) {

        Duration jumpToTime = new Duration(duration.toMillis() / 100 * sldProg.getValue());
        mPlayer.seek(jumpToTime);

    }

    private void checkShuffleState() {
        switch (shuffleState) {
            case SHUFFLE_OFF:
                mPlayer.setOnEndOfMedia(new Runnable() {
                    @Override
                    public void run() {
                        NextSongBtn(null);
                    }
                });
                break;

            case SHUFFLE_ON:
                mPlayer.setOnEndOfMedia(new Runnable() {
                    @Override
                    public void run() {
                        Random random = new Random(System.currentTimeMillis());
                        int nextIndex = random.nextInt(activeObvPlaylist.size());
                        playSong(activeObvPlaylist.get(nextIndex));
                    }
                });
                break;
        }
    }

    private void checkState() {
        switch (repeatState) {
            case repeat_OFF:
                checkShuffleState();
                break;

            case repeat_ON:
                mPlayer.setOnEndOfMedia(new Runnable() {
                    @Override
                    public void run() {
                        playSong(activeSong);
                    }
                });
                break;
        }
    }

    @FXML
    private void btnShuffle(MouseEvent event) {
        switch (shuffleState) {
            case SHUFFLE_OFF:

                Image shuffleOn = new Image("mytunes/GUI/View/Resouces/icons/icons8-Ashuffle-26.png");
                btnShuffle.setImage(shuffleOn);

                shuffleState = SHUFFLE_ON;
                checkShuffleState();

                break;
            case SHUFFLE_ON:
                Image shuffleOff = new Image("mytunes/GUI/View/Resouces/icons/icons8-shuffle-26.png");
                btnShuffle.setImage(shuffleOff);

                shuffleState = SHUFFLE_OFF;
                checkShuffleState();
                break;
        }
    }

    @FXML
    private void btnRepeat(MouseEvent event) {
        switch (repeatState) {
            case repeat_OFF:

                Image repeatOn = new Image("mytunes/GUI/View/Resouces/icons/icons8-Arefresh-96.png");
                btnRepeat.setImage(repeatOn);

                repeatState = repeat_ON;
                checkState();

                break;

            case repeat_ON:
                Image repeatOFF = new Image("mytunes/GUI/View/Resouces/icons/icons8-refresh-96.png");
                btnRepeat.setImage(repeatOFF);

                repeatState = repeat_OFF;
                checkState();

                break;

        }
    }

}
