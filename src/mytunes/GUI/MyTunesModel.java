/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.GUI;

import java.sql.SQLException;
import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
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
    private ObservableList<Playlist> playlists = FXCollections.observableArrayList();
    private MyTunesManager logiclayer;
    
    public MyTunesModel() throws SQLException {
        logiclayer = new MyTunesManager();
        
        songs = FXCollections.observableArrayList();
        //songs.addAll(logiclayer.getAllSong());
        
        playlists = FXCollections.observableArrayList();
        playlists.addAll(logiclayer.getAllPlaylits());
        playlists.addListener((Observable observable) -> {
        });
    }
    
    public List<Song> getAllSong() {
        return songs;
    }
    
    public ObservableList<Playlist> getAllPlaylists() {
        return playlists;
    }
    
    public void addSongToPlaylist(Song song, Playlist playlist) throws SQLException
    {
        logiclayer.addSongToPlaylist(song, playlist);
        int index = playlists.indexOf(playlist);
        playlists.get(index).addToPlaylist(song);
    }
    
    public void createPlaylist(String name) throws SQLException {
        Playlist playlist = logiclayer.createPlaylist(name);
        playlists.add(playlist);
    }
    
    public void createSong(String filePath, String title, String artist, double duration, String genre) throws SQLException {
        System.out.println("0");
        Song song = logiclayer.createSong(filePath, title, artist, duration, genre);
        System.out.println("1");
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
