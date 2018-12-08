/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.GUI.Model;

import java.io.IOException;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mytunes.BE.Playlist;
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

    public YouTubePlayerModel() throws IOException {
        logiclayer = new MyTunesManager();
        yVideos = FXCollections.observableArrayList();
        yVideos.setAll(logiclayer.getAllYoutubeVideos());
        yPlaylists = FXCollections.observableArrayList();
        yPlaylists.setAll(logiclayer.getAllYoutubePlaylists());
    }
    
    public ObservableList<YoutubeVideo> getAllSong() {
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
    
    public void createYoutubeVideo(String url) {
        YoutubeVideo yVideo = logiclayer.createYoutubeVideo(url);
        yVideos.add(yVideo);
        
    }
    
    public void updateYoutubeVideo(YoutubeVideo yVideo) {
        logiclayer.updateYouTube(yVideo);

        for (YoutubeVideo othervideo : yVideos) {
            if (othervideo.getId() == yVideo.getId()) {
                othervideo.setArtist(yVideo.getArtist());
                othervideo.setTitle(yVideo.getTitle());
            }
        }
    }
    
    public void deleteYoutubeVideo(YoutubeVideo yVideo) {
        logiclayer.deleteYoutubeVideo(yVideo);
        yVideos.remove(yVideo);
        for (YoutubePlaylist playlist : yPlaylists) {
            playlist.RemoveSongFromPlaylist(yVideo);
        }
    }

    public void deletePlayliste(YoutubePlaylist yPlaylist) {
        logiclayer.deleteToutubePlaylist(yPlaylist);
        yPlaylists.remove(yPlaylist);
    }
    
    public ObservableList<YoutubePlaylist> getAllPlaylists()
    {
        return yPlaylists;
    }
    
    public void addSongToPlaylist(YoutubeVideo video, YoutubePlaylist playlist)
    {
        logiclayer.addSongToPlaylist(video, playlist);
        playlist.addToPlaylist(video);
    }

    public void deleteFromPlaylist(YoutubeVideo video, YoutubePlaylist activePlaylist)
    {
        logiclayer.deleteFromPlaylist(video, activePlaylist); 
        activePlaylist.RemoveSongFromPlaylist(video);
    }
    
    
    
    
    
}
