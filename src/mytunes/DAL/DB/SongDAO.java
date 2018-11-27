/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.DAL.DB;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import mytunes.BE.Song;

/**
 *
 * @author Christian
 */
public class SongDAO {

    ServerConnect sc;

    public SongDAO(ServerConnect sc) {
        this.sc = sc;
    }

    public Song CreateSong(String filePath, String title, String artist, double duration) throws SQLServerException, SQLException {
        String sql = "INSERT INTO Song (Title, Path, Artist, duration) VALUES (?, ?, ?, ?)"; //måske album og nr på album

        Connection con = sc.getConnection();

        PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        st.setString(1, title);
        st.setString(2, filePath);
        st.setString(3, artist);
        st.setDouble(4, duration);

        int rowsAffected = st.executeUpdate();

        ResultSet rs = st.getGeneratedKeys();

        int id = 0;

        if (rs.next()) {
            id = rs.getInt(1);

        }

        Song song = new Song(filePath, title, id, artist, duration);

        return song;
    }

    public boolean updateSong(Song song) throws SQLException {
        String sql = "UPDATE Song SET Title = ?, Artist = ?, Path = ? WHERE SongID =" + song.getId();

        Connection con = sc.getConnection();

        PreparedStatement st = con.prepareStatement(sql);

        st.setString(1, song.getTitle());
        st.setString(2, song.getArtist());
        st.setString(3, song.getFilePath());

        int rowsAffected = st.executeUpdate();

        if (rowsAffected >= 1) {
            return true;
        }
        return false;
    }

    public void deleteSong(Song song) throws SQLServerException, SQLException {
        Connection con = sc.getConnection();

        Statement statement = con.createStatement();
        statement.execute(
                "DELETE FROM Playlist WHERE PlaylistID = "
                + song.getId()
        );
    }

    public List<Song> getAllSong() throws SQLServerException, SQLException {
        List<Song> songs = new ArrayList<>();
        Connection con = sc.getConnection();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM Song");

        while (rs.next()) {
            int id = rs.getInt("PlaylistID");
            String title = rs.getNString("Title");
            String path = rs.getNString("Path");
            String artist = rs.getNString("Artist");
            double duration = rs.getDouble("duration");
            
            Song song = new Song(path, title, id, artist, duration);
            songs.add(song);
        }
        return songs;
    }

}
