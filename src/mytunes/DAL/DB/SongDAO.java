
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.DAL.DB;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import mytunes.BE.Song;
import mytunes.DAL.MetadataExtractor;
import mytunes.DAL.ServerConnect;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.xml.sax.SAXException;

/**
 *
 * @author Christian
 */
public class SongDAO {

    //makes a server connection "sc" that can be accessed throughout the class
    ServerConnect sc;
    MetadataExtractor metaExtractor;

    public SongDAO() throws IOException {
        sc = new ServerConnect();
        metaExtractor = new MetadataExtractor();
    }
    
    
    //updates a song witht new Title, Artist, Path.
    public boolean updateSong(Song song) throws SQLException {
        String sql = "UPDATE [MyTunesAnchor].[dbo].[Song] SET Title = ?, Artist = ?, Path = ? WHERE SongID =" + song.getId();

        Connection con = sc.getConnection();

        PreparedStatement pst = con.prepareStatement(sql);

        pst.setString(1, song.getTitle());
        pst.setString(2, song.getArtist());
        pst.setString(3, song.getFilePath());

        int rowsAffected = pst.executeUpdate();
        if (rowsAffected >= 1) {
            return true;
        }
        return false;
//        return oldSong;
    }
    /*
    *deletes a song both on the playlist and from the list of songs
    *@pahrameter song
    */
    public void deleteSong(Song song) throws SQLServerException, SQLException {
        Connection con = sc.getConnection();

        Statement statement = con.createStatement();
        statement.execute(
                "DELETE FROM [MyTunesAnchor].[dbo].[Song_Playlist] WHERE SongID = "
                + song.getId()
        );
        statement.execute(
                "DELETE FROM [MyTunesAnchor].[dbo].[Song] WHERE SongID = "
                + song.getId()
        );

    }
    /*
    *gets all the songs in the sever table Song
    *@retuns List of all songs
    */
    public List<Song> getAllSong() throws SQLServerException, SQLException {
        List<Song> songs = new ArrayList<>();
        Connection con = sc.getConnection();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM [MyTunesAnchor].[dbo].[Song]");
        //runs all the songs through
        while (rs.next()) {
            int id = rs.getInt("SongID");
            String title = rs.getNString("Title");
            String path = rs.getNString("Path");
            String artist = rs.getNString("Artist");
            double duration = rs.getDouble("Duration");
            String genre = rs.getString("genre");

            Song song = new Song(path, title, id, artist, duration, genre);

            songs.add(song);

        }

        return songs;
    }
    
    /*
    *
    
    *@retuns a song
    */
    /**
     *  extracts metadat from file, adds it to the Database and returns a song
     * 
     * @param addedFile
     * 
     * @return Song
     * 
     * @throws IOException
     * @throws FileNotFoundException
     * @throws SAXException
     * @throws TikaException
     * @throws SQLServerException
     * @throws SQLException 
     */
    public Song createSong(File addedFile) throws IOException, FileNotFoundException, SAXException, TikaException, SQLServerException, SQLException
    {
        Metadata meta = metaExtractor.getMetaData(addedFile);
        String filePath = addedFile.getPath();
        String title = meta.get("title");
        String artist = meta.get("xmpDM:artist");
        double duration = Double.parseDouble(meta.get("xmpDM:duration"));
        String genre = meta.get("xmpDM:genre");
        
        String sql = "INSERT INTO [MyTunesAnchor].[dbo].[Song] (Title, Artist, Genre, Duration, Path) VALUES (?, ?, ?, ?, ?);"; //måske album og nr på album

        Connection con = sc.getConnection();

        PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        //makes it so that only the given parameter can be used
        st.setString(1, title);
        st.setString(5, filePath);
        st.setString(2, artist);
        st.setDouble(4, duration);
        st.setString(3, genre);

        int rowsAffected = st.executeUpdate();
        //get an generated key from sever and asains it as song id
        ResultSet rs = st.getGeneratedKeys();

        int id = 0;

        if (rs.next()) {
            id = rs.getInt(1);

        }

        Song song = new Song(filePath, title, id, artist, duration, genre);

        return song;
    }

}
