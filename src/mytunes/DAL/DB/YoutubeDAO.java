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
import mytunes.BE.YoutubeVideo;
import mytunes.DAL.MetadataExtractor;
import mytunes.DAL.ServerConnect;

/**
 *
 * @author Thorbjørn Schultz Damkjær
 */
public class YoutubeDAO
{

    private static ServerConnect server;

    public YoutubeDAO() throws IOException
    {
        this.server = new ServerConnect();
    }

    MetadataExtractor meta = new MetadataExtractor();

    public void deleteYoutubeVideo(YoutubeVideo yVideo) throws SQLServerException, SQLException
    {
        Connection con = server.getConnection();

        Statement statement = con.createStatement();
        statement.execute(
                "DELETE FROM [MyTunesAnchor].[dbo].[YVIDEO_YPlaylist] WHERE VideoID = "
                + yVideo.getId()
        );
        statement.execute(
                "DELETE FROM [MyTunesAnchor].[dbo].[YoutubeVideo] WHERE ID = "
                + yVideo.getId()
        );
        

    }

    public YoutubeVideo createYoutubeVideo(String url) throws IOException, SQLException
    {
        String title = meta.getYoutubeTitle(url);
        String artist = meta.getYoutubeChannelName(url);
        String youtubeID = meta.getYoutubeVideoID(url);
        String duration = meta.getYoutubeDuration(url);

        String sql = "INSERT INTO [MyTunesAnchor].[dbo].[YoutubeVideo] (Title, Artist, YoutubeID, Duration) VALUES (?, ?, ?, ?);"; //måske album og nr på album

        Connection con = server.getConnection();

        PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        //makes it so that only the given parameter can be used
        st.setString(1, title);
        st.setString(2, artist);
        st.setString(4, duration);
        st.setString(3, youtubeID);

        int rowsAffected = st.executeUpdate();
        //get an generated key from sever and asains it as song id
        ResultSet rs = st.getGeneratedKeys();

        int id = 0;

        if (rs.next())
        {
            id = rs.getInt(1);

        }

        YoutubeVideo yVideo = new YoutubeVideo(title, artist, youtubeID, id, duration);
//
        return yVideo;
    }

    public List<YoutubeVideo> getAllYoutubeVideos() throws SQLServerException, SQLException
    {
        List<YoutubeVideo> youtubeVideos = new ArrayList<>();
        Connection con = server.getConnection();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM [MyTunesAnchor].[dbo].[YoutubeVideo]");

        while (rs.next())
        {
            int id = rs.getInt("ID");
            String title = rs.getNString("Title");
            String artist = rs.getNString("Artist");
            String youtubeID = rs.getNString("YoutubeID");
            String duration = rs.getNString("Duration");

            YoutubeVideo video = new YoutubeVideo(title, artist, youtubeID, id, duration);
            youtubeVideos.add(video);
        }
        return youtubeVideos;
    }

    public void updateYouTubeVideo(YoutubeVideo yVideo) throws SQLServerException, SQLException
    {
        String sql = "UPDATE [MyTunesAnchor].[dbo].[YoutubeVideo] SET Title = ?, Artist = ? WHERE ID =" + yVideo.getId();

        Connection con = server.getConnection();

        PreparedStatement pst = con.prepareStatement(sql);

        pst.setNString(1, yVideo.getTitle());
        pst.setNString(2, yVideo.getArtist());

        int rowsAffected = pst.executeUpdate();
    }

}
