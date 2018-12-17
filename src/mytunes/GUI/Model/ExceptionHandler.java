/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.GUI.Model;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mytunes.BLL.exception.MyTunesException;
import mytunes.GUI.Controller.ExceptionPromtController;

/**
 *
 * @author Thorbjørn Schultz Damkjær
 */
public class ExceptionHandler
{
    MyTunesException error;

    public void openExceptionPromt(String message)
    {
        
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("mytunes/GUI/Model/ExceptionPromt.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            ExceptionPromtController controller = loader.getController();
            stage.setTitle("Somthing went Wrong");
            stage.show();
            controller.setWarningLabel(message);
            new MyTunesException(message);
        } catch (IOException ex)
        {
           new MyTunesException("ERROR! cannot fint error promt");
        }
    }
}
