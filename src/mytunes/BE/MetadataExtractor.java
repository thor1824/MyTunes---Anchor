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
import org.jsoup.Jsoup;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Thorbjørn Schultz Damkjær
 */
public class MetadataExtractor {

    MyTunesModel mtModel;
    org.jsoup.nodes.Document doc;
    org.jsoup.nodes.Element body;

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

    public void getVideoData(String videoUrl) throws IOException {
        doc = Jsoup.connect(videoUrl).header("User-Agent", "Chrome").get();
        body = doc.body();
        String videoThumbnail = body.getElementsByAttributeValue("itemprop", "thumbnailUrl").get(0).attr("href");
        String videoEmbedUrl = body.getElementsByAttributeValue("itemprop", "embedURL").get(0).attr("href");
        String videoTitle = body.getElementById("eow-title").attr("title");
        String userLink = body.getElementById("watch7-user-header").getElementsByAttributeValue("class", "yt-user-photo yt-uix-sessionlink      spf-link").attr("href");
        String userPhoto = body.getElementById("watch7-user-header").getElementsByTag("img").attr("data-thumb");
        String channelLink = body.getElementById("watch7-user-header").getElementsByClass("yt-user-info").get(0).child(0).attr("href");
        String channelName = body.getElementById("watch7-user-header").getElementsByClass("yt-user-info").get(0).child(0).wholeText();
        boolean isChannelVerified;
        try {
            isChannelVerified = body.getElementById("watch7-user-header").getElementsByClass("yt-user-info").get(0).child(1).attr("aria-label").equalsIgnoreCase("Verified") ? true : false;
        } catch (Exception e) {
            isChannelVerified = false;
        }
        String noOfSubs = body.getElementsByClass("yt-subscription-button-subscriber-count-branded-horizontal yt-subscriber-count").attr("title");
        String viewCount = body.getElementsByClass("watch-view-count").text();
        String noOfLikes = body.getElementsByAttributeValue("title", "I like this").get(0).text();
        String noOfDislikes = body.getElementsByAttributeValue("title", "I dislike this").get(0).text();
        String publishedOn = body.getElementById("watch-uploader-info").text().replace("Published on ", "");
        String description = body.getElementById("watch-description-text").children().text();
        boolean isFamilyFriendly = body.getElementsByAttributeValue("itemprop", "isFamilyFriendly").attr("content").equalsIgnoreCase("True") ? true : false;
        String genre = body.getElementsByAttributeValue("itemprop", "genre").attr("content");
//        VideoData videoData = new VideoData(videoThumbnail, videoEmbedUrl, videoTitle, userLink, userPhoto, channelLink, channelName, isChannelVerified, noOfSubs, viewCount, noOfLikes, noOfDislikes, publishedOn, description, isFamilyFriendly, genre);

    }

    public String getTitle(String url) throws IOException {
        doc = Jsoup.connect(url).header("User-Agent", "Chrome").get();
        body = doc.body();
        return body.getElementById("eow-title").attr("title");
    }

    public String getChannelName(String url) throws IOException {
        doc = Jsoup.connect(url).header("User-Agent", "Chrome").get();
        body = doc.body();
        return body.getElementById("watch7-user-header").getElementsByClass("yt-user-info").get(0).child(0).wholeText();
    }

    public String getDuration(String url) throws IOException {
        doc = Jsoup.connect(url).header("User-Agent", "Chrome").get();
        body = doc.body();
        return body.getElementsByAttributeValue("itemprop", "duration").attr("content");
    }

    public String getEmbeddedURL(String url) throws IOException {
        doc = Jsoup.connect(url).header("User-Agent", "Chrome").get();
        body = doc.body();
        return body.getElementsByAttributeValue("itemprop", "embedURL").get(0).attr("href");
    } 

}
