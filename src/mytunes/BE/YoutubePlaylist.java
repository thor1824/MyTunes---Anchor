/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.BE;

import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

/**
 *
 * @author Thorbjørn Schultz Damkjær
 */
public class YoutubePlaylist {
    
    private String title;
    private StringProperty titleProperty;
    private final int id;
    private ObservableList<YoutubeVideo> youtubeVideos;

    public YoutubePlaylist(String title, int id) {
        this.title = title;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public StringProperty getTitleProperty() {
        return titleProperty;
    }

    public int getId() {
        return id;
    }

    public ObservableList<YoutubeVideo> getYoutubeVideos() {
        return youtubeVideos;
    }

    public void setTitle(String title) {
        this.title = title;
        this.titleProperty.setValue(title);
    }
    
    
    public void addToPlaylist(YoutubeVideo yVideo) {
        youtubeVideos.add(yVideo);
    }

    public void RemoveSongFromPlaylist(YoutubeVideo yVideo) {
        youtubeVideos.remove(yVideo);
    }
    public void RemoveSongFromPlaylistByIndex(int index) {
        youtubeVideos.remove(index);
    }
    
    
}
