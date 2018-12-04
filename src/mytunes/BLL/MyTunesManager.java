/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.BLL;

import java.sql.SQLException;
import java.util.List;
import mytunes.BE.Playlist;
import mytunes.BE.Song;
import mytunes.DAL.DB.PlaylistDAO;
import mytunes.DAL.DB.SongDAO;

/**
 *
 * @author Thorbjørn Schultz Damkjær
 */
public class MyTunesManager {

    PlaylistDAO playlistDAO;
    SongDAO songDAO;

    public MyTunesManager() {
        this.playlistDAO = new PlaylistDAO();
        this.songDAO = new SongDAO();
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

}
