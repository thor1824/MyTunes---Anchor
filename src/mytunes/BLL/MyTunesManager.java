/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.BLL;

import mytunes.DAL.DB.YoutubePlaylistDAO;
import mytunes.DAL.DB.YoutubeDAO;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javafx.collections.ObservableList;
import mytunes.BE.Playlist;
import mytunes.BE.Song;
import mytunes.BE.YoutubePlaylist;
import mytunes.BE.YoutubeVideo;
import mytunes.DAL.DB.PlaylistDAO;
import mytunes.DAL.DB.SongDAO;

/**
 *
 * @author Thorbjørn Schultz Damkjær
 */
public class MyTunesManager {

    PlaylistDAO playlistDAO;
    SongDAO songDAO;
    YoutubeDAO youtubeDAO;
    YoutubePlaylistDAO youtubePlaylistDAO;
            

    public MyTunesManager() throws IOException {
        this.playlistDAO = new PlaylistDAO();
        this.songDAO = new SongDAO();
        this.youtubeDAO = new YoutubeDAO();
        this.youtubePlaylistDAO = new YoutubePlaylistDAO();
    }

    public Playlist createPlaylist(String name) throws SQLException {
        return playlistDAO.createPlaylist(name);
    }

    public void deletePlayliste(Playlist playlist) throws SQLException {
        playlistDAO.deletePlayliste(playlist);
    }

    public List<Playlist> getAllPlaylits() throws SQLException {
        return playlistDAO.getAllPlaylits();
    }

    public boolean updatePlaylist(Playlist playlist) throws SQLException {
        return playlistDAO.updatePlaylist(playlist);
    }

    public void deleteSong(Song song) throws SQLException {
        songDAO.deleteSong(song);
    }

    public List<Song> getAllSong() throws SQLException {
        return songDAO.getAllSong();
    }

    public boolean updateSong(Song song) throws SQLException {
        return songDAO.updateSong(song);
    }

    public void addSongToPlaylist(Song song, Playlist playlist) throws SQLException {
        playlistDAO.addSongToPlaylist(song, playlist);
    }

    public Song createSong(String filePath, String title, String artist, double duration, String genre) throws SQLException {
        return songDAO.createSong(filePath, title, artist, duration, genre);
    }

    public void deleteFromPlayist(Song song, Playlist playlist) throws SQLException {
        playlistDAO.deleteFromPlayist(song, playlist);
    }

    public void addYoutubeVideoToYoutubePlaylist(YoutubeVideo yVideo, YoutubePlaylist yplaylist) {
        youtubePlaylistDAO.addYoutubeVideoToYoutubePlaylist(yVideo, yplaylist);
    }

    public YoutubePlaylist createYoutubePlaylist(String name) {
        return youtubePlaylistDAO.createYoutubePlaylist(name);
    }

    public void updateYouTube(YoutubeVideo yVideo) {
        youtubeDAO.addYoutubeVideoToYoutubePlaylist(yVideo);
    }

    public void updateYoutubePlaylist(YoutubePlaylist yPlaylist) {
        youtubePlaylistDAO.updateYoutubePlaylist(yPlaylist);
    }

    public void deleteYoutubeVideo(YoutubeVideo yVideo) {
        youtubeDAO.deleteYoutubeVideo(yVideo);
    }

    public void deleteToutubePlaylist(YoutubePlaylist yPlaylist) {
        youtubePlaylistDAO.deleteToutubePlaylist(yPlaylist);
    }

    public YoutubeVideo createYoutubeVideo(String url)
    {
        return youtubeDAO.createYoutubeVideo(url);
    }

    public void addSongToPlaylist(YoutubeVideo video, YoutubePlaylist playlist)
    {
        youtubePlaylistDAO.addSongToPlaylist(video, playlist);
    }

    public void deleteFromPlaylist(YoutubeVideo video, YoutubePlaylist activePlaylist)
    {
        youtubePlaylistDAO.deleteFromPlaylist( video, activePlaylist);
    }

    public ObservableList<YoutubeVideo> getAllYoutubeVideos()
    {
        return youtubeDAO.getAllYoutubeVideos();
    }

    public ObservableList<YoutubePlaylist> getAllYoutubePlaylists()
    {
        return youtubePlaylistDAO.getAllYoutubePlaylists();
    }

}
