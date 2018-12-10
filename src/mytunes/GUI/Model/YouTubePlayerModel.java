/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.GUI.Model;

import java.io.IOException;
import java.sql.SQLException;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mytunes.BE.YoutubePlaylist;
import mytunes.BE.YoutubeVideo;
import mytunes.BLL.MyTunesManager;

/**
 *
 * @author Thorbjørn Schultz Damkjær
 */
public class YouTubePlayerModel {
    private ObservableList<YoutubeVideo> yVideos;
    private ObservableList<YoutubePlaylist> yPlaylists;
    private MyTunesManager logiclayer;

    public YouTubePlayerModel() throws IOException, SQLException {
        logiclayer = new MyTunesManager();
        yVideos = FXCollections.observableArrayList();
        yVideos.setAll(logiclayer.getAllYoutubeVideos());
        yVideos.addListener((Observable observable) -> {
        });

        yPlaylists = FXCollections.observableArrayList();
        yPlaylists.setAll(logiclayer.getAllYoutubePlaylists());
        yPlaylists.addListener((Observable observable) -> {
        });

    }
    
    public ObservableList<YoutubeVideo> getAllVideos() {
        return yVideos;
    }
    
    public void addYoutubeVideoToYoutubePlaylist(YoutubeVideo yVideo, YoutubePlaylist yplaylist) throws SQLException {
        logiclayer.addYoutubeVideoToYoutubePlaylist(yVideo, yplaylist);
        int index = yPlaylists.indexOf(yplaylist);
        yPlaylists.get(index).addToPlaylist(yVideo);
    }
    
    public void createYoutubePlaylist(String name) throws SQLException {
        YoutubePlaylist yPlaylist = logiclayer.createYoutubePlaylist(name);
        yPlaylists.add(yPlaylist);
    }
    
    public void createYoutubeVideo(String url) throws IOException, SQLException {
        
        YoutubeVideo yVideo = logiclayer.createYoutubeVideo(url);
        yVideos.add(yVideo);
        
    }
    
    public void updateYoutubeVideo(YoutubeVideo yVideo) throws SQLException {
        logiclayer.updateYouTubeVideo(yVideo);

        for (YoutubeVideo othervideo : yVideos) {
            if (othervideo.getId() == yVideo.getId()) {
                othervideo.setArtist(yVideo.getArtist());
                othervideo.setTitle(yVideo.getTitle());
            }
        }
    }
    
    public void deleteYoutubeVideo(YoutubeVideo yVideo) throws SQLException {
        logiclayer.deleteYoutubeVideo(yVideo);
        yVideos.remove(yVideo);
        for (YoutubePlaylist playlist : yPlaylists) {
            playlist.RemoveSongFromPlaylist(yVideo);
        }
    }

    public void deletePlayliste(YoutubePlaylist yPlaylist) throws SQLException {
        logiclayer.deleteToutubePlaylist(yPlaylist);
        yPlaylists.remove(yPlaylist);
    }
    
    public ObservableList<YoutubePlaylist> getAllPlaylists()
    {
        return yPlaylists;
    }
    
    public void deleteFromPlaylist(YoutubeVideo video, YoutubePlaylist activePlaylist) throws SQLException
    {
        logiclayer.deleteFromPlaylist(video, activePlaylist); 
        activePlaylist.RemoveSongFromPlaylist(video);
    }

    public void addSongToPlaylist(YoutubeVideo video, YoutubePlaylist playlist)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public boolean isYouTubeVideosEmpty()
    {
        if (yVideos.isEmpty())
        {
            return true;
        }
        return false;
    }
    
    public boolean isYouTubePlaylistEmpty()
    {
        if (yPlaylists.isEmpty())
        {
            return true;
        }
        return false;
    }
    
    
    
    
}
