/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.BLL;

import java.io.File;
import java.io.FileNotFoundException;
import mytunes.DAL.DB.YoutubePlaylistDAO;
import mytunes.DAL.DB.YoutubeDAO;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import mytunes.BE.Playlist;
import mytunes.BE.Song;
import mytunes.BE.YoutubePlaylist;
import mytunes.BE.YoutubeVideo;
import mytunes.DAL.DB.PlaylistDAO;
import mytunes.DAL.DB.SongDAO;
import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

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
    public void deleteFromPlayist(Song song, Playlist playlist) throws SQLException {
        playlistDAO.deleteFromPlayist(song, playlist);
    }

    public void addYoutubeVideoToYoutubePlaylist(YoutubeVideo yVideo, YoutubePlaylist yplaylist) throws SQLException {
        youtubePlaylistDAO.addYoutubeVideoToYoutubePlaylist(yVideo, yplaylist);
    }

    public YoutubePlaylist createYoutubePlaylist(String name) throws SQLException {
        return youtubePlaylistDAO.createYoutubePlaylist(name);
    }

    public void updateYouTubeVideo(YoutubeVideo yVideo) throws SQLException {
        youtubeDAO.updateYouTubeVideo(yVideo);
    }

    public void updateYoutubePlaylist(YoutubePlaylist yPlaylist) throws SQLException {
        youtubePlaylistDAO.updateYoutubePlaylist(yPlaylist);
    }

    public void deleteYoutubeVideo(YoutubeVideo yVideo) throws SQLException {
        youtubeDAO.deleteYoutubeVideo(yVideo);
    }

    public void deleteToutubePlaylist(YoutubePlaylist yPlaylist) throws SQLException {
        youtubePlaylistDAO.deleteYoutubePlaylist(yPlaylist);
    }

    public YoutubeVideo createYoutubeVideo(String url) throws IOException, SQLException
    {
        return youtubeDAO.createYoutubeVideo(url);
    }


    public void deleteFromPlaylist(YoutubeVideo video, YoutubePlaylist activePlaylist) throws SQLException
    {
        youtubePlaylistDAO.deleteFromPlaylist( video, activePlaylist);
    }

    public List<YoutubeVideo> getAllYoutubeVideos() throws SQLException
    {
        return youtubeDAO.getAllYoutubeVideos();
    }

    public List<YoutubePlaylist> getAllYoutubePlaylists() throws SQLException
    {
        return youtubePlaylistDAO.getAllYoutubePlaylists();
    }

    public Song createSong(File addedFile) throws IOException, FileNotFoundException, SAXException, TikaException, SQLException
    {
        return songDAO.createSong(addedFile);
    }

}
