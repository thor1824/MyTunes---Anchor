/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.GUI.Controller.Youtube;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.MediaView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import mytunes.BE.YoutubePlaylist;
import mytunes.BE.YoutubeVideo;
import mytunes.GUI.Controller.Music.MyTunesController;
import mytunes.GUI.Model.YouTubePlayerModel;

/**
 * FXML Controller class
 *
 * @author Thorbjørn Schultz Damkjær
 */
public class YouTubePlayerController implements Initializable
{

    @FXML
    private Label lblLibrary;
    @FXML
    private Label lblYoutube;
    @FXML
    private TableView<YoutubePlaylist> tbvPlayllist;
    @FXML
    private TableColumn<YoutubePlaylist, String> tbvPlaylistName;
    @FXML
    private MediaView mview;
    @FXML
    private Label lblPlaying;
    @FXML
    private WebView webview;
    @FXML
    private TableColumn<YoutubeVideo, String> colTitle;
    @FXML
    private TableColumn<YoutubeVideo, String> colArtist;
    @FXML
    private TableColumn<YoutubeVideo, String> colDuration;
    @FXML
    private AnchorPane mainPane;
    @FXML
    private Label lblVPlayer;
    @FXML
    private Menu addtoMenu;
    @FXML
    private TableView<YoutubeVideo> tbvYoutubeVideos;
    @FXML
    private ContextMenu ctmPlaylists;
    @FXML
    private ContextMenu ctmVideos;


    private static final String startEmbeddedURL = "<iframe width=\"900\" height=\"485\" src=\"http://www.youtube.com/embed/";
    private static final String endEmbeddedURL = "?&autoplay=1&state=enabled&cc_load_policy=1\" frameborder=\"0\" allowfullscreen></iframe>";
    private static final String middelPlaylistEmbeddedURL = "?&autoplay=1&state=enabled&cc_load_policy=1&playlist=";
    private static final String endPlaylistEmbeddedURL = "\" frameborder=\"0\" allowfullscreen></iframe>";
    private static final String endNoneAutoplayeEmbeddedURL = "?&state=enabled&cc_load_policy=1\" frameborder=\"0\" allowfullscreen></iframe>";
    private WebEngine webEngine;
    private Stage myStage;
    private YouTubePlayerModel ytModel;
    private ObservableList<YoutubePlaylist> allPlaylist;
    private ObservableList<YoutubeVideo> allVideos;
    private ObservableList<YoutubeVideo> activeObvPlaylist;
    private YoutubePlaylist activePlaylist;
    private YoutubeVideo activeVideo;
    private boolean onPlaylist = false;
    private MenuItem deleteFromAllVideos;
    private MenuItem deleteFromPlaylist;

    private String Test_Url = "<iframe width=\"900\" height=\"485\" src=\""
            + "http://www.youtube.com/embed/4z9TdDCWN7g"
            + "?&autoplay=1&state=enabled&cc_load_policy=1&playlist=mItWfoNnUag"
            + "\" frameborder=\"0\" allowfullscreen></iframe>";
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        tbvPlaylistName.setCellValueFactory(c -> c.getValue().getTitleProperty());
        colTitle.setCellValueFactory(c -> c.getValue().getTitleProperty());
        colArtist.setCellValueFactory(c -> c.getValue().getArtistProperty());
        colDuration.setCellValueFactory(c -> new SimpleObjectProperty(c.getValue().getFormattetDuration()));
        webEngine = webview.getEngine();
        
        activeObvPlaylist = FXCollections.observableArrayList();
        
        setupDobbleClick();
        try
        {
            ytModel = new YouTubePlayerModel();
        } catch (IOException ex)
        {
            Logger.getLogger(YouTubePlayerController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex)
        {
            Logger.getLogger(YouTubePlayerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        allPlaylist = FXCollections.observableArrayList();
        allPlaylist = ytModel.getAllPlaylists();
        tbvPlayllist.setItems(allPlaylist);

        allVideos = FXCollections.observableArrayList();
        allVideos = ytModel.getAllVideos();
        activeObvPlaylist.setAll(allVideos);
        tbvYoutubeVideos.setItems(allVideos);
        if (!allVideos.isEmpty())
        {
           webEngine.loadContent(startEmbeddedURL + allVideos.get(0).getYoutubeID() + endNoneAutoplayeEmbeddedURL);
           activeVideo= allVideos.get(0);
        }
        Label location = new Label();
        location.textProperty().bind(webEngine.locationProperty());
        location.textProperty().addListener((observable, oldValue, newValue) ->
        {
           webEngine.loadContent(startEmbeddedURL + activeVideo.getYoutubeID() + endNoneAutoplayeEmbeddedURL);
           
        });
//        webEngine.loadContent(Test_Url);
        

        deleteFromAllVideos = new MenuItem("Delete from Library");
        this.deleteFromAllVideos.setOnAction(new EventHandler<ActionEvent>()
        {

            @Override
            public void handle(ActionEvent event)
            {
                YoutubeVideo video = tbvYoutubeVideos.getSelectionModel().getSelectedItem();
                try
                {
                    ytModel.deleteYoutubeVideo(video);
                } catch (SQLException ex)
                {
                    Logger.getLogger(YouTubePlayerController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });
        ctmVideos.getItems().add(deleteFromAllVideos);
        deleteFromPlaylist = new MenuItem("Delete from Playlist");
        this.deleteFromPlaylist.setOnAction(new EventHandler<ActionEvent>()
        {

            @Override
            public void handle(ActionEvent event)
            {
                YoutubeVideo video = tbvYoutubeVideos.getSelectionModel().getSelectedItem();
                try
                {
                    ytModel.deleteFromPlaylist(video, activePlaylist);
                } catch (SQLException ex)
                {
                    Logger.getLogger(YouTubePlayerController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        genratePlaylistMenuItems(addtoMenu);

    }

    @FXML
    private void prevSongBtn(MouseEvent event)
    {
        int previousIndex = activeObvPlaylist.indexOf(activeVideo) - 1;
        if (previousIndex < 0)
        {
            previousIndex = activeObvPlaylist.size() - 1;
        }
        activeVideo = activeObvPlaylist.get(previousIndex);
        playVideo(activeVideo);

    }

    @FXML
    private void NextSongBtn(MouseEvent event)
    {
        int nextIndex = activeObvPlaylist.indexOf(activeVideo) + 1;
        if ((activeObvPlaylist.size() - 1) < nextIndex)
        {
            nextIndex = 0;
        }
        activeVideo = activeObvPlaylist.get(nextIndex);
        playVideo(activeVideo);
    }

    public void setStage(Stage stage)
    {
        myStage = stage;
        myStage.setOnCloseRequest(new EventHandler<WindowEvent>()
        {

            @Override
            public void handle(WindowEvent event)
            {
                webview.getEngine().load("");
            }
        });
    }

    @FXML
    private void menuAddYVideo(ActionEvent event)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("mytunes/GUI/View/Youtube/AddYoutubeVideo.fxml"));
            Parent root;
            root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Add Youtube Video");
            stage.setScene(new Scene(root));
            stage.show();
            AddYoutubeVideoController addCon = loader.getController();
            addCon.setYtModel(ytModel);
        } catch (IOException ex)
        {
            Logger.getLogger(YouTubePlayerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void chooseYPlaylist(ActionEvent event)
    {
        changToPlaylist();
    }

    @FXML
    private void playWholePlaylist(ActionEvent event)
    {
        YoutubePlaylist playlist = tbvPlayllist.getSelectionModel().getSelectedItem();

        if (playlist.getYoutubeVideos().size() > 1)
        {
            String youtubeIDPlaylist = "";
            for (int i = 1; i < playlist.getYoutubeVideos().size(); i++)
            {
                youtubeIDPlaylist += playlist.getYoutubeVideos().get(i).getYoutubeID() + ",";
            }

            webEngine.loadContent(startEmbeddedURL + playlist.getYoutubeVideos().get(0).getYoutubeID()
                    + middelPlaylistEmbeddedURL + youtubeIDPlaylist + endPlaylistEmbeddedURL);
        } else if (playlist.getYoutubeVideos().size() > 0)
        {
            playVideo(playlist.getYoutubeVideos().get(0));
        }
    }

    @FXML
    private void deleteYPlaylist(ActionEvent event) throws SQLException
    {
        ytModel.deletePlayliste(tbvPlayllist.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void editYPInfo(ActionEvent event)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("mytunes/GUI/View/Youtube/EditYoutubePlaylist.fxml"));
            Parent root;
            root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("edit");
            stage.setScene(new Scene(root));
            stage.show();
            EditYoutubePlaylistController addCon = loader.getController();
            addCon.setyPlaylist(tbvPlayllist.getSelectionModel().getSelectedItem());
            addCon.setYtModel(ytModel);
        } catch (IOException ex)
        {
            Logger.getLogger(YouTubePlayerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void playYVideo(ActionEvent event)
    {
        playVideo(tbvYoutubeVideos.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void deleteYVideo(ActionEvent event) throws SQLException
    {
        ytModel.deleteYoutubeVideo(tbvYoutubeVideos.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void EditYVInfo(ActionEvent event)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("mytunes/GUI/View/Youtube/EditYoutubeInfo.fxml"));
            Parent root;
            root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Edit Info");
            stage.setScene(new Scene(root));
            stage.show();
            EditYoutubeInfoController addCon = loader.getController();
            addCon.setyVideo(tbvYoutubeVideos.getSelectionModel().getSelectedItem());
            addCon.setYtModel(ytModel);
        } catch (IOException ex)
        {
            Logger.getLogger(YouTubePlayerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void newYPlaylist(ActionEvent event)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("mytunes/GUI/View/Youtube/CreateYoutubePlaylistView.fxml"));
            Parent root;
            root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Create Playlist");
            stage.setScene(new Scene(root));
            stage.show();

            CreateYoutubePlaylistViewController addCon = loader.getController();
            addCon.setYoutubeModel(ytModel);
        } catch (IOException ex)
        {
            Logger.getLogger(YouTubePlayerController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void playVideo(YoutubeVideo video)
    {
        activeVideo = video;
        System.out.println(video.getYoutubeID());
        webEngine.loadContent(startEmbeddedURL + video.getYoutubeID() + endEmbeddedURL);
    }

    private void setupDobbleClick()
    {
        lblLibrary.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent mouseEvent)
            {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY))
                {
                    if (mouseEvent.getClickCount() == 2)
                    {
                        try
                        {
                            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("mytunes/GUI/View/Music/MyTunes.fxml"));

                            Parent root;

                            root = loader.load();

                            Stage stage = new Stage();
                            stage.setScene(new Scene(root));
                            MyTunesController controller = loader.getController();
                            controller.setStage(stage);
                            stage.setTitle("MyTube");
                            stage.show();
                            webview.getEngine().load("");
                            myStage.close();
                        } catch (IOException ex)
                        {
                            Logger.getLogger(YouTubePlayerController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        });

        lblYoutube.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent mouseEvent)
            {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY))
                {
                    if (mouseEvent.getClickCount() == 2)
                    {
                        System.out.println("dobbleclick");
                        System.out.println(allVideos);
                        tbvYoutubeVideos.setItems(allVideos);
                        if (onPlaylist)
                        {
                            onPlaylist = false;
                            switchMenuItems();

                        }

                    }
                }
            }
        });

        tbvPlayllist.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent mouseEvent)
            {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY))
                {
                    if (mouseEvent.getClickCount() == 2)
                    {
                        changToPlaylist();
                    }
                }
            }

        });

        lblVPlayer.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent mouseEvent)
            {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY))
                {
                    if (mouseEvent.getClickCount() == 2)
                    {

                    }
                }
            }
        });

        tbvYoutubeVideos.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent mouseEvent)
            {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY))
                {
                    if (mouseEvent.getClickCount() == 2)
                    {

                        playVideo(tbvYoutubeVideos.getSelectionModel().getSelectedItem());

                    }
                }
            }
        });

    }

    private void switchMenuItems()
    {
        if (onPlaylist)
        {
            ctmVideos.getItems().remove(deleteFromAllVideos);
            ctmVideos.getItems().add(deleteFromPlaylist);

        } else
        {
            ctmVideos.getItems().remove(deleteFromPlaylist);
            ctmVideos.getItems().add(deleteFromAllVideos);

        }
    }

    private void genratePlaylistMenuItems(Menu menuAdd)
    {
        menuAdd.getItems().clear();
        for (YoutubePlaylist playlist : allPlaylist)
        {
            String title = playlist.getTitle();
            MenuItem playlistAdd = new MenuItem(title);

            playlistAdd.setOnAction(new EventHandler<ActionEvent>()
            {

                @Override
                public void handle(ActionEvent event)
                {
                    YoutubeVideo video = tbvYoutubeVideos.getSelectionModel().getSelectedItem();
                    try
                    {
                        ytModel.addYoutubeVideoToYoutubePlaylist(video, playlist);
                    } catch (SQLException ex)
                    {
                        Logger.getLogger(YouTubePlayerController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            });
            menuAdd.getItems().add(playlistAdd);

        }
    }

    private void changToPlaylist()
    {
        YoutubePlaylist playlist = tbvPlayllist.getSelectionModel().getSelectedItem();
        activePlaylist = playlist;
        System.out.println(playlist.getYoutubeVideos());
        activeObvPlaylist = playlist.getYoutubeVideos();
        tbvYoutubeVideos.setItems(activeObvPlaylist);

        if (!onPlaylist)
        {
            onPlaylist = true;
            switchMenuItems();

        }
    }

}
