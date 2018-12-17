/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.DAL;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
public class MetadataExtractor
{

    MyTunesModel mtModel;
    org.jsoup.nodes.Document doc;
    org.jsoup.nodes.Element body;

    /**
     * return a metadata object of the give mp3 filepath
     *
     * @param addedFile
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     * @throws SAXException
     * @throws TikaException
     */
    public Metadata getMetaData(File addedFile) throws FileNotFoundException, IOException, SAXException, TikaException
    {
        InputStream input = new FileInputStream(addedFile);
        ContentHandler handler = new DefaultHandler();
        Metadata metadata = new Metadata();
        Parser parser = new Mp3Parser();
        ParseContext parseCtx = new ParseContext();
        parser.parse(input, handler, metadata, parseCtx);
        input.close();
        return metadata;
    }

    /**
     * scrape the the url for the title
     *
     * @param url
     * @return
     * @throws IOException
     */
    public String getYoutubeTitle(String url) throws IOException
    {

        return body.getElementById("eow-title").attr("title");
    }

    /**
     * scrape the the url for the Channel name
     *
     * @param url
     * @return
     * @throws IOException
     */
    public String getYoutubeChannelName(String url) throws IOException
    {
        doc = Jsoup.connect(url).header("User-Agent", "Chrome").get();
        body = doc.body();
        return body.getElementById("watch7-user-header").getElementsByClass("yt-user-info").get(0).child(0).wholeText();
    }

    /**
     * scrape the the url for the Duration
     *
     * @param url
     * @return
     * @throws IOException
     */
    public String getYoutubeDuration(String url) throws IOException
    {
        doc = Jsoup.connect(url).header("User-Agent", "Chrome").get();
        body = doc.body();
        return body.getElementsByAttributeValue("itemprop", "duration").attr("content");
    }

    /**
     * scrape the the url for the embedded url
     *
     * @param url
     * @return
     * @throws IOException
     */
    public String getYoutubeEmbeddedURL(String url) throws IOException
    {
        doc = Jsoup.connect(url).header("User-Agent", "Chrome").get();
        body = doc.body();
        return body.getElementsByAttributeValue("itemprop", "embedURL").get(0).attr("href");
    }

    /**
     * scrape the the url for the VideoID
     *
     * @param url
     * @return
     * @throws IOException
     */
    public String getYoutubeVideoID(String url) throws IOException
    {
        String splitat = "v=";
        String[] splitURL = url.split(splitat);
        return splitURL[1];
    }

}
