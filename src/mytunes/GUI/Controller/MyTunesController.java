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
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.input.MouseButton;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import mytunes.BE.Song;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.ContextMenuEvent;
import mytunes.BE.MediaPlayerWithElements;
import mytunes.BE.MetadataExtractor;
import mytunes.BE.Playlist;
import mytunes.GUI.Model.MyTunesModel;

import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

/**
 * FXML Controller class
 *
 * @author Nijas Hansen
 */
public class MyTunesController implements Initializable {

    //FXML
    @FXML
    private ImageView btnPlay;
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
    private ImageView btnShuffle;
    @FXML
    private ImageView btnRepeat;
    @FXML
    private TableView<Playlist> tbvPlayllist;
    @FXML
    private TableColumn<Playlist, String> tbvPlaylistName;
    @FXML
    private TextField txtSearch;

    //other
    private MediaPlayer mPlayer;
    private MyTunesModel mtModel;
    private boolean onPlaylist = false;
    private ObservableList<Playlist> allPlaylist;
    private ObservableList<Song> allSongs;
    private ObservableList<Song> activeObvPlaylist;
    private Playlist activePlaylist;
    private Song activeSong;
    private ContextMenu cmSong;
    private MenuItem deleteSong;
    private MenuItem deleteSongFromPlist;
    private FilteredList<Song> searchList;
    private SortedList<Song> sortedData;
    private MediaPlayerWithElements mPlayer2;
    private MetadataExtractor metadata;
    @FXML
    private Label lblLibrary;
    @FXML
    private Label lblYoutube;

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
        tbvPlayllist.setPlaceholder(new Label("No Playlists yet!"));
        txtSearch.setPromptText("search...");

        // slider
        sldVol.setValue(50);

        //other init
        try {
            mtModel = new MyTunesModel();

            allSongs = FXCollections.observableArrayList();
            allSongs.setAll(mtModel.getAllSong());
            activeObvPlaylist = FXCollections.observableArrayList();
            activeObvPlaylist.setAll(allSongs);
            tbvSongs.setItems(activeObvPlaylist);
            allPlaylist = FXCollections.observableArrayList();
            allPlaylist.setAll(mtModel.getAllPlaylists());
            tbvPlayllist.setItems(allPlaylist);
            

            //Creating MediaPlayerWithElemets
            mPlayer2 = new MediaPlayerWithElements(mPlayer, activeObvPlaylist, activeSong);
            mPlayer2.setPlayButton(btnPlay);
            mPlayer2.setRepeatButton(btnRepeat);
            mPlayer2.setShuffleButton(btnShuffle);
            mPlayer2.setSongProgressBar(songProg);
            mPlayer2.setSongProgressSlider(sldProg);
            mPlayer2.setTimeLabel(lbltime);
            mPlayer2.setSongPlayingLabel(lblPlaying);
            mPlayer2.setVolumeSlider(sldVol);
            if (activeObvPlaylist.size() > 0) {
                mPlayer2.setInitialSong(activeObvPlaylist.get(0));
            }
            
            setupSeachBar();

            //Creating MetadataExtractor
            metadata = new MetadataExtractor();
            metadata.setMtModel(mtModel);

        } catch (SQLException ex) {
            Logger.getLogger(MyTunesController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
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
                try {
                    mtModel.deleteFromPlayist(song, activePlaylist);
                } catch (SQLException ex) {
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
                        changeMusicList(playlist.getSongs());
                        if (!onPlaylist) {
                            onPlaylist = true;
                            switchMenuItems();

                        }

                    }
                }
            }
        });

        // Double-click on Music Library to see all songs
        lblLibrary.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2) {
                        changeMusicList(mtModel.getAllSong());
                        if (onPlaylist) {
                            onPlaylist = false;
                            switchMenuItems();

                        }
                    }
                }
            }
        });

    }

    @FXML
    private void volSlider(MouseEvent event) {
        mPlayer2.ChangeVolume();
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
    
    @FXML
    private void playSongBtn(MouseEvent event) {
        mPlayer2.playPauseSwitch();
    }

    @FXML
    private void NextSongBtn(MouseEvent event) {
        mPlayer2.playNextSong();
    }

    @FXML
    private void prevSongBtn(MouseEvent event) {
        mPlayer2.playPreviousSong();
    }

    @FXML
    private void menuAddSong(ActionEvent event) throws SQLException { 

        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(new ExtensionFilter("mp3 files", "*.mp3"));
        fc.setInitialDirectory(new File("src"));
        File addedFile = fc.showOpenDialog(null);
        try {

            metadata.createSongFromMetadata(addedFile);

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

                    metadata.createSongFromMetadata(addedFile);

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

    @FXML
    private void jumpTo(MouseEvent event) {
        mPlayer2.jumpinSong();
    }

    @FXML
    private void btnShuffle(MouseEvent event) {
        mPlayer2.shuffleSongs();
    }

    @FXML
    private void btnRepeat(MouseEvent event) {
        mPlayer2.RepeatSongs();
    }

    private void playSong(Song song) {
        mPlayer2.playSong(song);
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
        tbvSongs.setItems(list);
        mPlayer2.changeMusicList(list);
        // Needs to setup at every list change for the filterlist, predicator and sortedlist to respond to input
        setupSeachBar(); 

    }

    private void setupSeachBar() {
        searchList = new FilteredList(mPlayer2.getActivelistOfSongs(), p -> true);
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            searchList.setPredicate(song -> {
                // If filter text is empty, display all Songs
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare Title, Artist and Genre of every Song with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (song.getTitle().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches Title.
                } else if (song.getArtist().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches Artist.
                }
                else if (song.getGenre().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches Genre.
                }
                return false; // Does not match.
            });
        });
        sortedData = new SortedList<>(searchList); // Wrap the FilteredList in a SortedList.
        sortedData.comparatorProperty().bind(tbvSongs.comparatorProperty()); // Bind the SortedList comparator to the TableView comparator.
        tbvSongs.setItems(sortedData);//Add sorted (and filtered) data to the table.
    }

    public void switchMenuItems() {
        if (onPlaylist) {
            cmSong.getItems().remove(deleteSong);
            cmSong.getItems().add(deleteSongFromPlist);

        } else {
            cmSong.getItems().remove(deleteSongFromPlist);
            cmSong.getItems().add(deleteSong);

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

}
