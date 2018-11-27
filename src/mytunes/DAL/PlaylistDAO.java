/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.DAL;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import mytune.DAL.ServerConnect;
import mytunes.BE.Playlist;

/**
 *
 * @author Christian
 */
public class PlaylistDAO {

    private static ServerConnect server;

    public PlaylistDAO() {

        server = new ServerConnect();
    }

    public Playlist createPlaylist(String name) throws SQLServerException, SQLException {
        String sql = "INSERT INTO Playlist (Title) VALUES (?)";

        Connection con = server.getConnection();

        PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        st.setString(1, name);

        int rowsAffected = st.executeUpdate();

        ResultSet rs = st.getGeneratedKeys();

        int id = 0;

        if (rs.next()) {
            id = rs.getInt(1);

        }

        Playlist playlist = new Playlist(name, id);

        return playlist;

    }

    public void deletePlayliste(Playlist playlist) throws SQLServerException, SQLException {
        Connection con = server.getConnection();

        Statement statement = con.createStatement();
        statement.execute(
                "DELETE FROM Playlist WHERE PlaylistID = "
                + playlist.getId()
        );

    }

    public List<Playlist> getAllPlaylits() throws SQLException {
        List<Playlist> playlists = new ArrayList<>();
        Connection con = server.getConnection();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM Playlist");
        //ResultSet resultSet = st.executeQuery("SELECT FROM Song_Playlist ")
        while (rs.next()) {
            int id = rs.getInt("PlaylistID");
            String title = rs.getNString("Title");
            Playlist playlist = new Playlist(title, id);
            playlists.add(playlist);
        }
        return playlists;
    }

    public boolean updatePlaylist(Playlist playlist) throws SQLServerException, SQLException {

        String sql = "UPDATE Playlist SET Title = ? WHERE PlaylistID =" + playlist.getId();

        Connection con = server.getConnection();

        PreparedStatement st = con.prepareStatement(sql);

        st.setString(1, playlist.getTitle());

        int rowsAffected = st.executeUpdate();
        
        if (rowsAffected >= 1){
            return true;
        }
        return false;

    }

}
