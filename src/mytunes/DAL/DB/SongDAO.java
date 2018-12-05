/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.DAL.DB;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import mytunes.BE.Song;
import mytunes.DAL.ServerConnect;

/**
 *
 * @author Christian
 */
public class SongDAO {

    ServerConnect sc;

    public SongDAO() throws IOException {
        sc = new ServerConnect();
    }

    public Song createSong(String filePath, String title, String artist, double duration, String genre) throws SQLServerException, SQLException {
        String sql = "INSERT INTO Song (Title, Artist, Genre, Duration, Path) VALUES (?, ?, ?, ?, ?);"; //måske album og nr på album

        Connection con = sc.getConnection();

        PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        st.setString(1, title);
        st.setString(5, filePath);
        st.setString(2, artist);
        st.setDouble(4, duration);
        st.setString(3, genre);

        int rowsAffected = st.executeUpdate();

        ResultSet rs = st.getGeneratedKeys();

        int id = 0;

        if (rs.next()) {
            id = rs.getInt(1);

        }

        Song song = new Song(filePath, title, id, artist, duration, genre);

        return song;
    }

    public boolean updateSong(Song song) throws SQLException {
        String sql = "UPDATE Song SET Title = ?, Artist = ?, Path = ? WHERE SongID =" + song.getId();

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

    public void deleteSong(Song song) throws SQLServerException, SQLException {
        Connection con = sc.getConnection();

        Statement statement = con.createStatement();
        statement.execute(
                "DELETE FROM Song_Playlist WHERE SongID = "
                + song.getId()
        );
        statement.execute(
                "DELETE FROM Song WHERE SongID = "
                + song.getId()
        );
        
    }

    public List<Song> getAllSong() throws SQLServerException, SQLException {
        List<Song> songs = new ArrayList<>();
        Connection con = sc.getConnection();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM [MyTunesAnchor].[dbo].[Song]");

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

}
