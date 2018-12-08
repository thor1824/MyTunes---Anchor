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

    public MyTunesModel() throws SQLException, IOException {
        logiclayer = new MyTunesManager();

        songs = FXCollections.observableArrayList();
        songs.setAll(logiclayer.getAllSong());
        songs.addListener((Observable observable) -> {
        });

        playlists = FXCollections.observableArrayList();
        playlists.setAll(logiclayer.getAllPlaylits());
        playlists.addListener((Observable observable) -> {
        });
    }

    public ObservableList<Song> getAllSong() {
        return songs;
    }

    public ObservableList<Playlist> getAllPlaylists() {
        return playlists;
    }
    public void deleteFromPlayist(Song song, Playlist playlist) throws SQLException
    {
        playlist.deleteFromPlaylist(playlist.getSongs().indexOf(song));
        logiclayer.deleteFromPlayist(song, playlist);

    }

    public void addSongToPlaylist(Song song, Playlist playlist) throws SQLException {
        logiclayer.addSongToPlaylist(song, playlist);
        int index = playlists.indexOf(playlist);
        playlists.get(index).addToPlaylist(song);
    }

    public void createPlaylist(String name) throws SQLException {
        Playlist playlist = logiclayer.createPlaylist(name);
        playlists.add(playlist);
    }

    public boolean createSong(String filePath, String title, String artist, double duration, String genre) throws SQLException {
        boolean nonIdentical = true;
        for (Song song : songs) {
            
            if (song.getTitle().equals(title)) {

                nonIdentical = false;
            }
        }
        Song song = logiclayer.createSong(formatePathTosrc(filePath), title, artist, duration, genre);
        songs.add(song);
        return nonIdentical;
    }

    public void updateSong(Song song) throws SQLException {
        logiclayer.updateSong(song);

        for (Song otherSong : songs) {
            if (song.getId() == otherSong.getId()) {
                otherSong.setArtist(song.getArtist());
                otherSong.setFilePath(formatePathTosrc(song.getFilePath()));
                otherSong.setTitle(song.getTitle());
                otherSong.setGenre(song.getGenre());
            }
        }
    }

    public String formatePathTosrc(String path) {
        path.replace("\\", "/").replaceAll(" ", "%20");
        if (path.contains("src\\Music")) {
            String[] urlSplit = path.split("src");
            String newURL = "src" + urlSplit[1];
            return newURL;
        }
        return path;
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
        for (Playlist playlist : playlists) {
            playlist.RemoveSongFromPlaylist(song);
        }
    }

    public void deletePlayliste(Playlist playlist) throws SQLException {
        logiclayer.deletePlayliste(playlist);
        playlists.remove(playlist);
    }

}
