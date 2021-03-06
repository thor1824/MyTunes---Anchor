/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.DAL.DB;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import mytunes.BE.Playlist;
import mytunes.BE.Song;
import mytunes.DAL.ServerConnect;

/**
 *
 * @author Christian
 */
public class PlaylistDAO {

    //establishes a server connect witch can be used in the inter class
    private static ServerConnect server;
    
    public PlaylistDAO() throws IOException {

        this.server = new ServerConnect();
    }
    /* 
     * creats a playlist on the sever, using sql
     */
    
    public Playlist createPlaylist(String name) throws SQLServerException, SQLException {
        String sql = "INSERT INTO [MyTunesAnchor].[dbo].[Playlist] (Title) VALUES (?)";

        Connection con = server.getConnection();

        PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        st.setString(1, name);

        int rowsAffected = st.executeUpdate();
        //get a sever generated number to use as id
        ResultSet rs = st.getGeneratedKeys();

        int id = 0;

        if (rs.next()) {
            id = rs.getInt(1);
        }

        Playlist playlist = new Playlist(name, id);

        return playlist;

    }
    
    /*
    *adds a song to a playlist by inserting a song object and a playlist object into a joind table 
    @SongID
    @PlaylistID
    */
    public void addSongToPlaylist(Song song, Playlist playlist) throws SQLServerException, SQLException {
        Connection con = server.getConnection();
        String sql = "INSERT INTO [MyTunesAnchor].[dbo].[Song_Playlist] (SongID, PlaylistID) VALUES (?,?)";

        PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        st.setInt(1, song.getId());
        st.setInt(2, playlist.getId());

        int rowsAffected = st.executeUpdate();

        ResultSet rs = st.getGeneratedKeys();

        if (rs.next()) {
            song.setPositionID(rs.getInt(1));
        }

    }
    //delete a playlist by playlist id
    public void deletePlayliste(Playlist playlist) throws SQLServerException, SQLException {
        Connection con = server.getConnection();
        //sql that delete the playlist from sever tabel
        Statement statement = con.createStatement();
        statement.execute(
                "DELETE FROM [MyTunesAnchor].[dbo].[Playlist] WHERE PlaylistID = "
                + playlist.getId()
        );
        statement.execute(
                "DELETE FROM [MyTunesAnchor].[dbo].[Song_Playlist] WHERE PlaylistID = "
                + playlist.getId()
        );

    }
    /*
    * gets all playlists from sever
    *@returns all playlists 
    */
    public List<Playlist> getAllPlaylits() throws SQLException {
        List<Playlist> playlists = new ArrayList<>();
        Connection con = server.getConnection();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM [MyTunesAnchor].[dbo].[Playlist]");

        while (rs.next()) {
            int id = rs.getInt("PlaylistID");
            String title = rs.getNString("Title");
            Playlist playlist = new Playlist(title, id);
            getSongFromPlaylist(playlist);

            playlists.add(playlist);
        }
        return playlists;
    }
    /*
    * gets all the songs on a playlist 
    */
    public void getSongFromPlaylist(Playlist playlist) throws SQLException {
        Connection con = server.getConnection();
        Statement st = con.createStatement();
        ResultSet resultSet = st.executeQuery("SELECT * "
                + "FROM [MyTunesAnchor].[dbo].[Song] "
                + "RIGHT JOIN [MyTunesAnchor].[dbo].[Song_Playlist] ON [MyTunesAnchor].[dbo].[Song].[SongID] = [MyTunesAnchor].[dbo].[Song_Playlist].[SongID] "
                + "WHERE PlaylistID = " + playlist.getId()
        );
        while (resultSet.next()) {
            int id = resultSet.getInt("SongID");
            double duration = resultSet.getDouble("Duration");
            String title = resultSet.getNString("Title");
            String path = resultSet.getNString("Path");
            String genre = resultSet.getNString("Genre");
            String artist = resultSet.getNString("Artist");
            int position = resultSet.getInt("PositionID");

            Song song = new Song(path, title, id, artist, duration, genre);
            song.setPositionID(position);
            playlist.addToPlaylist(song);
        }
    }

    // updates the name of the playlist from id 
    public boolean updatePlaylist(Playlist playlist) throws SQLServerException, SQLException {

        String sql = "UPDATE [MyTunesAnchor].[dbo].[Playlist] SET Title = ? WHERE PlaylistID =" + playlist.getId();

        Connection con = server.getConnection();

        PreparedStatement pst = con.prepareStatement(sql);

        pst.setString(1, playlist.getTitle());

        int rowsAffected = pst.executeUpdate();

        if (rowsAffected >= 1) {
            return true;
        }
        return false;

    }
    //delets a song from the playlist using a playlist id and the song PositionID 
    public void deleteFromPlayist(Song song, Playlist playlist) throws SQLException {
        Connection con = server.getConnection();
        Statement st = con.createStatement();

        st.execute(
                "DELETE FROM [MyTunesAnchor].[dbo].[Song_Playlist] WHERE PlaylistID = " + playlist.getId()
                + " AND PositionID = " + song.getPositionID()
        );

    }

}
