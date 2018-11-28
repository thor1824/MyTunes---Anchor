/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.GUI;

import java.sql.SQLException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mytunes.BE.Playlist;
import mytunes.BE.Song;
import mytunes.BLL.MyTunesManager;

/**
 *
 * @author Thorbjørn Schultz Damkjær
 */
public class MyTunesModel {
    
    private ObservableList<Song> songs;
    private ObservableList<Playlist> playlists;
    private MyTunesManager logiclayer;
    
    public MyTunesModel() throws SQLException {
        logiclayer = new MyTunesManager();
        
        songs = FXCollections.observableArrayList();
        songs.addAll(logiclayer.getAllSong());
        
        playlists = FXCollections.observableArrayList();
        playlists.addAll(logiclayer.getAllPlaylits());
    }
    
    public List<Song> getAllSong() {
        return songs;
    }
    
    public List<Playlist> getAllPlaylists() {
        return playlists;
    }
    
    public void createPlaylist(String name) throws SQLException {
        Playlist playlist = logiclayer.createPlaylist(name);
        playlists.add(playlist);
    }
    
    public void CreateSong(String filePath, String title, String artist, double duration) throws SQLException {
        Song song = logiclayer.createSong(filePath, title, artist, duration);
        songs.add(song);
    }
    
    public void updateSong(Song song) throws SQLException {
        logiclayer.updateSong(song);
//        songs.remove(oldSong);
//        songs.add(song);
        for (Song otherSong : songs) {
            if (song.getId() == otherSong.getId()) {
                otherSong.setArtist(song.getArtist());
                otherSong.setFilePath(song.getFilePath());
                otherSong.setTitle(song.getTitle());
            }
        }
    }
    
    public boolean updatePlaylist(Playlist playlist) throws SQLException {
        logiclayer.updatePlaylist(playlist);
        for (Playlist otherPlaylist : playlists) {
            if (playlist.getId() == otherPlaylist.getId()) {
                otherPlaylist.setTitle(playlist.getTitle());
                return true;
            }
        }
        return false;
    }
    
    public void deleteSong(Song song) throws SQLException {
        logiclayer.deleteSong(song);
        songs.remove(song);
    }
    
    public void deletePlayliste(Playlist playlist) throws SQLException {
        logiclayer.deletePlayliste(playlist);
        playlists.remove(playlist);
    }
    
}
