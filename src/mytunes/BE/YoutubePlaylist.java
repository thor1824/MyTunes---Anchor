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
 *
 * @author Thorbjørn Schultz Damkjær
 */
public class YoutubePlaylist
{

    private String title;
    private StringProperty titleProperty;
    private final int id;
    private ObservableList<YoutubeVideo> youtubeVideos;

    public YoutubePlaylist(String title, int id)
    {
        this.title = title;
        titleProperty = new SimpleStringProperty();
        titleProperty.setValue(title);
        this.id = id;
        youtubeVideos = FXCollections.observableArrayList();
    }

    /**
     * Get the value of title
     *
     * @return
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * Get the value of title as string property
     *
     * @return
     */
    public StringProperty getTitleProperty()
    {
        return titleProperty;
    }

    /**
     * Get the value of ID
     *
     * @return
     */
    public int getId()
    {
        return id;
    }

    /**
     * gets all the video on the playlist
     *
     * @return
     */
    public ObservableList<YoutubeVideo> getYoutubeVideos()
    {
        return youtubeVideos;
    }

    /**
     * Sets the value of title
     *
     * @param title
     */
    public void setTitle(String title)
    {
        this.title = title;
        this.titleProperty.setValue(title);
    }

    /**
     * Adds a video to the playlist
     *
     * @param yVideo
     */
    public void addToPlaylist(YoutubeVideo yVideo)
    {
        youtubeVideos.add(yVideo);
    }

    /**
     * removes a video from the playlist
     *
     * @param yVideo
     */
    public void RemoveSongFromPlaylist(YoutubeVideo yVideo)
    {
        youtubeVideos.remove(yVideo);
    }

    /**
     * removes a video from playlist bu its ID
     *
     * @param index
     */
    public void RemoveSongFromPlaylistByIndex(int index)
    {
        youtubeVideos.remove(index);
    }

}
