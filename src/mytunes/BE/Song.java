/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.BE;

import java.io.File;

/**
 *
 * @author Christian
 */
public class Song {

    private static String filePath;
    private static String Title;
    private static File file;
    private static int id;
    private static String artist;
    private static double duration;
    private static String genre;

    /**
     * Get the value of genre
     *
     * @return the value of genre
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Set the value of genre
     *
     * @param genre new value of genre
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }


    public Song(String fileParth, String Title, int id, String artist, double duration, String genre) {
        this.filePath = fileParth;
        this.Title = Title;
        this.id = id;
        this.artist = artist;
        this.duration = duration;
        this.genre = genre;
    }

    

    /**
     * Get the value of duration
     *
     * @return the value of duration
     */
    public double getDuration() {
        return duration;
    }

    /**
     * Set the value of duration
     *
     * @param duration new value of duration
     */
    public void setDuration(double duration) {
        this.duration = duration;
    }

    /**
     * Get the value of artist
     *
     * @return the value of artist
     */
    public String getArtist() {
        return artist;
    }

    /**
     * Set the value of artist
     *
     * @param artist new value of artist
     */
    public void setArtist(String artist) {
        this.artist = artist;
    }

    /**
     * Get the value of filePath
     *
     * @return the value of filePath
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * Set the value of filePath
     *
     * @param fileParth new value of filePath
     */
    public void setFilePath(String fileParth) {
        this.filePath = fileParth;
    }

    /**
     * Get the value of Title
     *
     * @return the value of Title
     */
    public String getTitle() {
        return Title;
    }

    /**
     * Set the value of Title
     *
     * @param Title new value of Title
     */
    public void setTitle(String Title) {
        this.Title = Title;
    }

    public int getId() {
        return id;
    }
}
