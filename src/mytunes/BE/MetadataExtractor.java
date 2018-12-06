/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.BE;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import mytunes.GUI.Model.MyTunesModel;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Thorbjørn Schultz Damkjær
 */
public class MetadataExtractor {
    MyTunesModel mtModel;

    public void setMtModel(MyTunesModel mtModel) {
        this.mtModel = mtModel;
    }
    
    
    public void createSongFromMetadata(File addedFile) throws IOException, SAXException, SQLException, FileNotFoundException, TikaException, NumberFormatException {
        InputStream input = new FileInputStream(addedFile);
        ContentHandler handler = new DefaultHandler();
        Metadata metadata = new Metadata();
        Parser parser = new Mp3Parser();
        ParseContext parseCtx = new ParseContext();
        parser.parse(input, handler, metadata, parseCtx);
        input.close();

        String filePath = addedFile.getPath();
        System.out.println(filePath);
        String title = metadata.get("title");
        String artist = metadata.get("xmpDM:artist");
        double duration = Double.parseDouble(metadata.get("xmpDM:duration"));
        String genre = metadata.get("xmpDM:genre");
        if (genre == null) {
            genre = "";
        }
        boolean added = mtModel.createSong(filePath, title, artist, duration, genre);
        if (added) {
            System.out.println("Song added to Library");
        } else {
            System.out.println(title + ": already exsist");
        }
    }
    public void getYoutubeVideoMetadata(){}
}
