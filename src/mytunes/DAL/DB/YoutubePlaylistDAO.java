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
import javafx.collections.ObservableList;
import mytunes.BE.Playlist;
import mytunes.BE.Song;
import mytunes.BE.YoutubePlaylist;
import mytunes.BE.YoutubeVideo;
import mytunes.DAL.ServerConnect;

/**
 *
 * @author Thorbjørn Schultz Damkjær
 */
public class YoutubePlaylistDAO
{
    private static ServerConnect server;
    
    public YoutubePlaylistDAO() throws IOException
    {
        this.server = new ServerConnect();
    }
    
    public void addYoutubeVideoToYoutubePlaylist(YoutubeVideo yVideo, YoutubePlaylist yplaylist) throws SQLServerException, SQLException
    {
        Connection con = server.getConnection();
        String sql = "INSERT INTO [MyTunesAnchor].[dbo].[YVIDEO_YPlaylist] (VideoID, PlaylistID) VALUES (?,?)";

        PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        st.setInt(1, yVideo.getId());
        st.setInt(2, yplaylist.getId());

        int rowsAffected = st.executeUpdate();

        ResultSet rs = st.getGeneratedKeys();

        if (rs.next()) {
            yVideo.setPositionID(rs.getInt(1));
        }
    }

    public YoutubePlaylist createYoutubePlaylist(String name) throws SQLException
    {
        String sql = "INSERT INTO [MyTunesAnchor].[dbo].[YoutubePlaylist] (Title) VALUES (?)";

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

        YoutubePlaylist playlist = new YoutubePlaylist(name, id);

        return playlist;
    }

    public void updateYoutubePlaylist(YoutubePlaylist yPlaylist) throws SQLServerException, SQLException
    {
        String sql = "UPDATE [MyTunesAnchor].[dbo].[YoutubePlaylist] SET Title = ? WHERE ID =" + yPlaylist.getId();

        Connection con = server.getConnection();

        PreparedStatement pst = con.prepareStatement(sql);

        pst.setString(1, yPlaylist.getTitle());

        int rowsAffected = pst.executeUpdate();

    }

    public void deleteYoutubePlaylist(YoutubePlaylist yPlaylist) throws SQLException
    {
        Connection con = server.getConnection();
        //sql that delete the playlist from sever tabel
        Statement statement = con.createStatement();
        statement.execute(
                "DELETE FROM [MyTunesAnchor].[dbo].[YoutubePlaylist] WHERE PlaylistID = "
                + yPlaylist.getId()
        );
        statement.execute(
                "DELETE FROM [MyTunesAnchor].[dbo].[YVIDEO_YPlaylist] WHERE PlaylistID = "
                + yPlaylist.getId()
        );
    }

    public void deleteFromPlaylist(YoutubeVideo video, YoutubePlaylist activePlaylist) throws SQLException
    {
        Connection con = server.getConnection();
        Statement st = con.createStatement();

        st.execute(
                "DELETE FROM [MyTunesAnchor].[dbo].[YVIDEO_YPlaylist] WHERE PlaylistID = " + activePlaylist.getId()
                + " AND PositionID = " + video.getPositionID()
        );
    }

    public List<YoutubePlaylist> getAllYoutubePlaylists() throws SQLException
    {
        List<YoutubePlaylist> playlists = new ArrayList<>();
        Connection con = server.getConnection();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM [MyTunesAnchor].[dbo].[YouTubePlaylist]");

        while (rs.next()) {
            int id = rs.getInt("ID");
            String title = rs.getNString("Title");
            YoutubePlaylist playlist = new YoutubePlaylist(title, id);
            getYoutubeVideosFromPlaylist(playlist);
            playlists.add(playlist);
        }
        return playlists;
    }
    
    public void getYoutubeVideosFromPlaylist(YoutubePlaylist playlist) throws SQLException {
        Connection con = server.getConnection();
        Statement st = con.createStatement();
        ResultSet resultSet = st.executeQuery("SELECT * "
                + "FROM [MyTunesAnchor].[dbo].[YoutubeVideo] "
                + "RIGHT JOIN [MyTunesAnchor].[dbo].[YVIDEO_YPlaylist] ON [MyTunesAnchor].[dbo].[YoutubeVideo].[ID] = [MyTunesAnchor].[dbo].[YVIDEO_YPlaylist].[VideoID] "
                + "WHERE PlaylistID = " + playlist.getId()
        );
        //runns all songes through
        while (resultSet.next()) {
            int id = resultSet.getInt("ID");
            String duration = resultSet.getNString("Duration");
            String title = resultSet.getNString("Title");
            String youtubeID = resultSet.getNString("YoutubeID");
            String artist = resultSet.getNString("Artist");
            int position = resultSet.getInt("PositionID");

            YoutubeVideo video = new YoutubeVideo(title, artist, youtubeID, id, duration);
            video.setPositionID(position);
            playlist.addToPlaylist(video);
        }
    }
    
}
