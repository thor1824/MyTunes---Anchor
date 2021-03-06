/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.BE;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Playlist contains a oberservablelist with songs, a title and a id
 *
 * @author Christian
 */
public class Playlist
{

    private String title;
    private StringProperty titleProperty;
    private int id;
    private ObservableList<Song> songs;

    /**
     * Constructs the PlayList.
     *
     * @param title
     * @param id
     */
    public Playlist(String title, int id)
    {
        this.title = title;
        this.id = id;
        songs = FXCollections.observableArrayList();
        titleProperty = new SimpleStringProperty(title);
    }

    /**
     * Get the value of id
     *
     * @return the value of id
     */
    public int getId()
    {
        return id;
    }

    public StringProperty getTitleProperty()
    {
        return titleProperty;
    }

    /**
     * Get the value of songs
     *
     * @return the value of songs
     */
    public ObservableList<Song> getSongs()
    {
        return songs;
    }

    /**
     * Get the value of title
     *
     * @return the value of title
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * Set the value of title
     *
     * @param title new value of title
     */
    public void setTitle(String title)
    {
        this.title = title;
        titleProperty.setValue(title);

    }

    /**
     * Add a song to the PlayList.
     *
     * @param song
     */
    public void addToPlaylist(Song song)
    {
        songs.add(song);
    }

    /**
     * Remove a song from songs.
     *
     * @param song
     */
    public void RemoveSongFromPlaylist(Song song)
    {
        songs.remove(song);
    }

    /**
     * Remove a song from songs by index number.
     *
     * @param index
     */
    public void deleteFromPlaylist(int index)
    {
        songs.remove(index);
    }

    /**
     * object to string metode.
     *
     * @return title
     */
    @Override
    public String toString()
    {
        return title;
    }

}
