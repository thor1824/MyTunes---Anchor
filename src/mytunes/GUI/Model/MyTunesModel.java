/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.GUI.Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mytunes.BE.Playlist;
import mytunes.BE.Song;
import mytunes.BLL.MyTunesManager;
import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

/**
 *
 * @author Thorbjørn Schultz Damkjær
 */
public class MyTunesModel
{

    private ObservableList<Song> songs;
    private ObservableList<Playlist> playlists;
    private MyTunesManager logiclayer;

    public MyTunesModel() throws SQLException, IOException
    {
        logiclayer = new MyTunesManager();

        songs = FXCollections.observableArrayList();
        songs.setAll(logiclayer.getAllSong());
        songs.addListener((Observable observable) ->
        {
        });

        playlists = FXCollections.observableArrayList();
        playlists.setAll(logiclayer.getAllPlaylits());
        playlists.addListener((Observable observable) ->
        {
        });
    }
    
    /**
     * get all Songs as an observablelist
     * 
     * @return 
     */
    public ObservableList<Song> getAllSong()
    {
        return songs;
    }
    
    /**
     * get all playlist as an observable list
     * 
     * @return 
     */
    public ObservableList<Playlist> getAllPlaylists()
    {
        return playlists;
    }
    
    /**
     * deletes the given song on the given playlist
     * 
     * @param song
     * @param playlist
     * @throws SQLException 
     */
    public void deleteFromPlayist(Song song, Playlist playlist) throws SQLException
    {
        playlist.deleteFromPlaylist(playlist.getSongs().indexOf(song));
        logiclayer.deleteFromPlayist(song, playlist);

    }
    
    /**
     * adds the given song on the given playlist
     * 
     * @param song
     * @param playlist
     * @throws SQLException 
     */
    public void addSongToPlaylist(Song song, Playlist playlist) throws SQLException
    {
        logiclayer.addSongToPlaylist(song, playlist);
        int index = playlists.indexOf(playlist);
        playlists.get(index).addToPlaylist(song);
    }
    
    /**
     * creates a playlist with given name
     * 
     * @param name
     * @throws SQLException 
     */
    public void createPlaylist(String name) throws SQLException
    {
        Playlist playlist = logiclayer.createPlaylist(name);
        playlists.add(playlist);
    }
    
    /**
     * updates the the information of the given song
     * 
     * @param song
     * @throws SQLException 
     */
    public void updateSong(Song song) throws SQLException
    {
        logiclayer.updateSong(song);

        for (Song otherSong : songs)
        {
            if (song.getId() == otherSong.getId())
            {
                otherSong.setArtist(song.getArtist());
                otherSong.setFilePath(formatePathTosrc(song.getFilePath()));
                otherSong.setTitle(song.getTitle());
                otherSong.setGenre(song.getGenre());
            }
        }
    }

    public String formatePathTosrc(String path)
    {
        path.replace("\\", "/").replaceAll(" ", "%20");
        if (path.contains("src\\Music"))
        {
            String[] urlSplit = path.split("src");
            String newURL = "src" + urlSplit[1];
            return newURL;
        }
        return path;
    }

    public boolean updatePlaylist(Playlist playlist) throws SQLException
    {
        logiclayer.updatePlaylist(playlist);
        for (Playlist otherPlaylist : playlists)
        {
            if (playlist.getId() == otherPlaylist.getId())
            {
                otherPlaylist.setTitle(playlist.getTitle());
                return true;
            }
        }
        return false;
    }

    public void deleteSong(Song song) throws SQLException
    {
        logiclayer.deleteSong(song);
        songs.remove(song);
        for (Playlist playlist : playlists)
        {
            playlist.RemoveSongFromPlaylist(song);
        }
    }

    public void deletePlayliste(Playlist playlist) throws SQLException
    {
        logiclayer.deletePlayliste(playlist);
        playlists.remove(playlist);
    }

    public void createSong(File addedFile) throws IOException, FileNotFoundException, SAXException, TikaException, SQLException
    {
        Song song = logiclayer.createSong(new File(formatePathTosrc(addedFile.getAbsolutePath())));
        songs.add(song);

    }

}
