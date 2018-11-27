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
    
        private File fileParth;
        private String Title;
        public int id;

    public Song(File fileParth, String Title, int id) {
        this.fileParth = fileParth;
        this.Title = Title;
        this.id = id;
    }     
        

    /**
     * Get the value of fileParth
     *
     * @return the value of fileParth
     */
    public File getFileParth() {
        return fileParth;
    }

    /**
     * Set the value of fileParth
     *
     * @param fileParth new value of fileParth
     */
    public void setFileParth(File fileParth) {
        this.fileParth = fileParth;
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

    public void setId(int id) {
        this.id = id;
    }

    
}
