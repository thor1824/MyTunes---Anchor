/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.BE;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Christian
 */
public class Song {

    private String filePath;
    private String songTitle;
    private StringProperty titleProperty;
    private int id;
    private String artist;
    private StringProperty artistProperty;
    private double duration;
    private String genre;
    private StringProperty genreProperty;
<<<<<<< HEAD
    private int PositionID = 0;
=======
    private int PositionID;
>>>>>>> 56195b58713b03a86f50569715814bfbcde8a6d2

    public Song(String fileParth, String Title, int id, String artist, double duration, String genre) {
        this.filePath = fileParth;
        this.songTitle = Title;
        titleProperty = new SimpleStringProperty(Title);
        this.id = id;
        this.artist = artist;
        artistProperty = new SimpleStringProperty(artist);
        this.duration = duration;
        this.genre = genre;
        genreProperty = new SimpleStringProperty(genre);
    }

    public StringProperty getArtistProperty() {
        return artistProperty;
    }

    public StringProperty getGenreProperty() {
        return genreProperty;
    }
<<<<<<< HEAD
=======


>>>>>>> 56195b58713b03a86f50569715814bfbcde8a6d2

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
        genreProperty.setValue(genre);
    }
<<<<<<< HEAD

=======
    
>>>>>>> 56195b58713b03a86f50569715814bfbcde8a6d2
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
        artistProperty.setValue(artist);
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
     * Get the value of songTitle
     *
     * @return the value of songTitle
     */
    public String getTitle() {
        return songTitle;
    }

    /**
     * Set the value of songTitle
     *
     * @param Title new value of songTitle
     */
    public void setTitle(String Title) {
        this.songTitle = Title;
        titleProperty.setValue(Title);
    }

    public int getId() {
        return id;
    }
<<<<<<< HEAD
=======

>>>>>>> 56195b58713b03a86f50569715814bfbcde8a6d2

    public StringProperty getTitleProperty() {
        return titleProperty;
    }

<<<<<<< HEAD
=======
    
>>>>>>> 56195b58713b03a86f50569715814bfbcde8a6d2
    public int getPositionID() {
        return PositionID;
    }

    public void setPositionID(int PositionID) {
        this.PositionID = PositionID;
    }

<<<<<<< HEAD
=======

>>>>>>> 56195b58713b03a86f50569715814bfbcde8a6d2
}
