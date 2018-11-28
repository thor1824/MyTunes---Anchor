/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.BE;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Christian
 */
public class Playlist {

    private String title;
    private int id;
    private List<Song> songs;

    public Playlist(String title, int id) {
        this.title = title;
        this.id = id;
        songs = new ArrayList<>();
    }

    /**
     * Get the value of id
     *
     * @return the value of id
     */
    public int getId() {
        return id;
    }

    /**
     * Get the value of songs
     *
     * @return the value of songs
     */
    public List<Song> getSongs() {
        return songs;
    }

    /**
     * Get the value of title
     *
     * @return the value of title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the value of title
     *
     * @param Title new value of title
     */
    public void setTitle(String Title) {
        this.title = Title;
    }

    public void addToPlaylist(Song song) {
        songs.add(song);
    }

    public void deleteFromPlaylist(int index) {
        songs.remove(index);
    }
}
