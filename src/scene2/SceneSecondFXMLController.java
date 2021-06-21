/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scene2;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author admin
 */
public class SceneSecondFXMLController implements Initializable {

    @FXML
    private TextArea text;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    public void setData(int id, String desc, int debit, int credit) {
        text.setText(
                "Id : " + String.valueOf(id) +
                "\nDeskripsi : " + desc +
                "\nDebit : " + String.valueOf(debit) +
                "\nCredit : " + String.valueOf(credit) 
        );
    }
    
}
