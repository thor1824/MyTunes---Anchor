/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.GUI.Controller.Youtube;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import mytunes.GUI.Model.YouTubePlayerModel;

/**
 * FXML Controller class
 *
 * @author Thorbjørn Schultz Damkjær
 */
public class AddYoutubeVideoController implements Initializable
{

    @FXML
    private TextField txtURL;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnCancel;
    
    private YouTubePlayerModel ytModel;
    @FXML
    private Label lblInstruction;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // TODO
    }
    
    @FXML
    private void cancel(ActionEvent event)
    {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    public void setYtModel(YouTubePlayerModel ytModel)
    {
        this.ytModel = ytModel;
        System.out.println(ytModel);
    }
    
    private boolean isYoutubeIRL(String url)
    {
        if (url.contains("https://www.youtube.com/"))
        {
            return true;
        }
        return false;
    }

    @FXML
    private void add(MouseEvent event) throws IOException, SQLException
    {
        System.out.println("hej");
        if (isYoutubeIRL(txtURL.getText()))
        {
            
            System.out.println(txtURL.getText());
            ytModel.createYoutubeVideo(txtURL.getText());
            Stage stage = (Stage) btnAdd.getScene().getWindow();
            stage.close();
        } else {
            lblInstruction.setText("Illegal URL detected");
        }
    }
    
}
