/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.GUI.Controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mytunes.DAL.DB.PlaylistDAO;
import mytunes.GUI.Model.MyTunesModel;

/**
 * FXML Controller class
 *
 * @author Nijas Hansen
 */
public class AddPlaylistController implements Initializable {

    private Button btnAdd;
    @FXML
    private Button btnCancel;

    private MyTunesModel mtModel;
    @FXML
    private TextField txtPlaylistName;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            mtModel = new MyTunesModel();
        } catch (SQLException ex) {
            Logger.getLogger(AddPlaylistController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AddPlaylistController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void addPlaylistBTN(ActionEvent event) throws SQLException {
        mtModel.createPlaylist(txtPlaylistName.getText());
    }

    public void setMtModel(MyTunesModel mtm) {
        mtModel = mtm;
    }

    /**
     *
     * @param event closes the window
     */
    @FXML
    private void cancelPlaylist(ActionEvent event) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();

        stage.close();
    }

}
