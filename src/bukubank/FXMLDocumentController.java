package bukubank;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import data.DataBank;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import util.ActionButtonTableCell;
import scene2.SceneSecondFXMLController;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * @author Ahmad Faishal Albadri
 * @nim 20523166
 *
 * @author Muhammad Yusuf Hidayat
 * @nim 20523128
 */

public class FXMLDocumentController implements Initializable {

    @FXML
    private TextField tfDescAdd;

    @FXML
    private TextField tfDebitAdd;

    @FXML
    private TextField tfCreditAdd;

    @FXML
    private TextField tfDescEdit;

    @FXML
    private TextField tfDebitEdit;

    @FXML
    private TextField tfCreditEdit;

    @FXML
    private ComboBox<String> cbEdit;

    @FXML
    private TableView tableData;

    @FXML
    private TableColumn tcNomor;

    @FXML
    private TableColumn tcDesc;

    @FXML
    private TableColumn tcDebit;

    @FXML
    private TableColumn tcKredit;

    @FXML
    private TableColumn tcAction;

    @FXML
    private TableColumn tcDetail;

    private DataBank dataBank;

    private ArrayList<DataBank> arrayDataBank = new ArrayList<DataBank>();

    private Alert alert = new Alert(Alert.AlertType.ERROR);

    private ObservableList<DataBank> listitemBanks = FXCollections.observableArrayList();

    @FXML
    private void handleButtonAdd(ActionEvent event) {
        if (tfDescAdd.getText().isEmpty()) {
            alert.setTitle("Kosong!!!");
            alert.setContentText("Data Deskripsi Masih Kosong");
            alert.showAndWait();
        } else if (tfCreditAdd.getText().isEmpty()) {
            alert.setTitle("Kosong!!!");
            alert.setContentText("Data Kredit Masih Kosong");
            alert.showAndWait();
        } else if (tfDebitAdd.getText().isEmpty()) {
            alert.setTitle("Kosong!!!");
            alert.setContentText("Data Debit Masih Kosong");
            alert.showAndWait();
        } else {
            createData(tfDescAdd.getText().toString(), Integer.parseInt(tfDebitAdd.getText().toString()), Integer.parseInt(tfCreditAdd.getText().toString()));
        }
    }

    @FXML
    private void onComboBoxEditClick(ActionEvent event) {
        if (cbEdit.getValue() != null) {
            String cbValue = cbEdit.getValue();
            for (int i = 0; i < arrayDataBank.size(); i++) {
                String itemValue = String.valueOf(arrayDataBank.get(i).getId()) + ". " + arrayDataBank.get(i).getDesc();
                if (itemValue.equals(cbValue)) {
                    tfDescEdit.setText(arrayDataBank.get(i).getDesc());
                    tfCreditEdit.setText(String.valueOf(arrayDataBank.get(i).getCredit()));
                    tfDebitEdit.setText(String.valueOf(arrayDataBank.get(i).getDebit()));
                }
            }
        } else {
            tfDescEdit.setText(null);
            tfCreditEdit.setText(null);
            tfDebitEdit.setText(null);
        }
    }

    @FXML
    private void handleButtonEdit(ActionEvent event) {
        if (tfDescEdit.getText().isEmpty()) {
            alert.setTitle("Kosong!!!");
            alert.setContentText("Data Deskripsi Masih Kosong");
            alert.showAndWait();
        } else if (tfCreditEdit.getText().isEmpty()) {
            alert.setTitle("Kosong!!!");
            alert.setContentText("Data Kredit Masih Kosong");
            alert.showAndWait();
        } else if (tfDebitEdit.getText().isEmpty()) {
            alert.setTitle("Kosong!!!");
            alert.setContentText("Data Debit Masih Kosong");
            alert.showAndWait();
        } else if (cbEdit.getValue() == null) {
            alert.setTitle("Kosong!!!");
            alert.setContentText("Pilih Data Yang Mau Di Edit");
            alert.showAndWait();
        } else {
            editData(cbEdit.getValue(), tfDescEdit.getText().toString(), Integer.parseInt(tfDebitEdit.getText().toString()), Integer.parseInt(tfCreditEdit.getText().toString()));
        }
    }

    @FXML
    private void handleButtonReset(ActionEvent event) {
        resetData();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dataBank = new DataBank();
        tcNomor.setCellValueFactory(new PropertyValueFactory<DataBank, String>("id"));
        tcDesc.setCellValueFactory(new PropertyValueFactory<DataBank, String>("desc"));
        tcDebit.setCellValueFactory(new PropertyValueFactory<DataBank, String>("debit"));
        tcKredit.setCellValueFactory(new PropertyValueFactory<DataBank, String>("credit"));
        
        tcAction.setCellFactory(ActionButtonTableCell.<DataBank>forTableColumn("Delete", (data) -> {
            deleteData(data.getId());
            return data;
        }));
        
        tcDetail.setCellFactory(ActionButtonTableCell.<DataBank>forTableColumn("Detail", (data) -> {
            System.out.println(data.getDesc());

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/scene2/SceneSecondFXML.fxml"));
                Parent root1 = (Parent) fxmlLoader.load();

                SceneSecondFXMLController sceneSecondFXMLController = fxmlLoader.getController();
                sceneSecondFXMLController.setData(data.getId(), data.getDesc(), data.getDebit(), data.getCredit());

                Stage stage = new Stage();
                stage.setScene(new Scene(root1));
                stage.show();
            } catch (IOException e) {
                System.err.println(String.format("Error: %s", e.getMessage()));
            }
            return data;
        }));
        tableData.setItems(listitemBanks);
        
        readData();
    }

    private void readData() {
        ObservableList<String> itemsComboBox = FXCollections.observableArrayList();
        cbEdit.setItems(itemsComboBox);
        listitemBanks.clear();
        arrayDataBank.clear();
        XStream xStream = new XStream(new StaxDriver());
        FileInputStream fileInputStream;
        
        try {
            fileInputStream = new FileInputStream("src/xml/Bank.xml");
            arrayDataBank = (ArrayList<DataBank>) xStream.fromXML(fileInputStream);
            fileInputStream.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        listitemBanks.addAll(arrayDataBank);
        
        for (int i = 0; i < arrayDataBank.size(); i++) {
            int id;
            String desc;
            id = arrayDataBank.get(i).getId();
            desc = arrayDataBank.get(i).getDesc();
            String sComboBox = String.valueOf(id) + ". " + desc;
            itemsComboBox.add(sComboBox);
            
        }
    }

    private void createData(String desc, int debit, int credit) {
        ArrayList<DataBank> dataBanksGet = new ArrayList<>();
        ArrayList<DataBank> dataBanksAdd = new ArrayList<>();
        XStream xStream = new XStream(new StaxDriver());
        FileInputStream fileInputStream;

        try {
            fileInputStream = new FileInputStream("src/xml/Bank.xml");
            dataBanksGet = (ArrayList<DataBank>) xStream.fromXML(fileInputStream);
            fileInputStream.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        int id = dataBanksGet.size();

        if (id == 0) {
            DataBank dataBank = new DataBank();
            dataBank.setId(1);
            dataBank.setCredit(credit);
            dataBank.setDebit(debit);
            dataBank.setDesc(desc);
            dataBanksAdd.add(dataBank);
        } else {
            for (int i = 0; i < dataBanksGet.size(); i++) {
                DataBank dataBank = new DataBank();
                dataBank.setId(dataBanksGet.get(i).getId());
                dataBank.setCredit(dataBanksGet.get(i).getCredit());
                dataBank.setDebit(dataBanksGet.get(i).getDebit());
                dataBank.setDesc(dataBanksGet.get(i).getDesc());
                dataBanksAdd.add(dataBank);
            }
            
            DataBank dataBank = new DataBank();
            dataBank.setId(id + 1);
            dataBank.setCredit(credit);
            dataBank.setDebit(debit);
            dataBank.setDesc(desc);
            dataBanksAdd.add(dataBank);
        }
        
        String xml = xStream.toXML(dataBanksAdd);
        FileOutputStream fileOutputStream;
        
        try {
              byte[] data = xml.getBytes("UTF-8");
              fileOutputStream = new FileOutputStream("src/xml/Bank.xml");
              fileOutputStream.write(data);
              fileOutputStream.close();
              tfCreditAdd.setText("");
              tfDebitAdd.setText("");
              tfDescAdd.setText("");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        readData();
    }

    private void editData(String id, String desc, int debit, int credit) {
        ArrayList<DataBank> dataBanksGet = new ArrayList<>();
        ArrayList<DataBank> dataBanksEdit = new ArrayList<>();
        XStream xStream = new XStream(new StaxDriver());
        FileInputStream fileInputStream;
                
        try {
            fileInputStream = new FileInputStream("src/xml/Bank.xml");
            dataBanksGet = (ArrayList<DataBank>) xStream.fromXML(fileInputStream);
            fileInputStream.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
                
        for (int i = 0; i < dataBanksGet.size(); i++) {
            String idValue = String.valueOf(dataBanksGet.get(i).getId() + ". " + dataBanksGet.get(i).getDesc());
            if (idValue.equals(id)) {
                DataBank dataBank = new DataBank();
                dataBank.setId(dataBanksGet.get(i).getId());
                dataBank.setCredit(credit);
                dataBank.setDebit(debit);
                dataBank.setDesc(desc);
                dataBanksEdit.add(dataBank);
            } else {
                DataBank dataBank = new DataBank();
                dataBank.setId(dataBanksGet.get(i).getId());
                dataBank.setCredit(dataBanksGet.get(i).getCredit());
                dataBank.setDebit(dataBanksGet.get(i).getDebit());
                dataBank.setDesc(dataBanksGet.get(i).getDesc());
                dataBanksEdit.add(dataBank);
            }
        }
        
        String xml = xStream.toXML(dataBanksEdit);
        FileOutputStream fileOutputStream;
        
        try {
              // Mengubah tipe data string menjadi byte agar bisa di input ke Bank.xml
              byte[] data = xml.getBytes("UTF-8");
              fileOutputStream = new FileOutputStream("src/xml/Bank.xml");
              fileOutputStream.write(data);
              fileOutputStream.close();
              tfCreditEdit.setText("");
              tfDebitEdit.setText("");
              tfDescEdit.setText("");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        readData();
    }

    private void resetData() {
        ArrayList<DataBank> dataBanksReset = new ArrayList<>();
        XStream xStream = new XStream(new StaxDriver());
        String xml = xStream.toXML(dataBanksReset);
        FileOutputStream fileOutputStream;
        try {
              byte[] data = xml.getBytes("UTF-8");
              fileOutputStream = new FileOutputStream("src/xml/Bank.xml");
              fileOutputStream.write(data);
              fileOutputStream.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        readData();
    }

    private void deleteData(int id) {
        ArrayList<DataBank> dataBanksGet = new ArrayList<>();
        ArrayList<DataBank> dataBanksDelete = new ArrayList<>();
        XStream xStream = new XStream(new StaxDriver());
        FileInputStream fileInputStream;
                
        try {
            fileInputStream = new FileInputStream("src/xml/Bank.xml");
            dataBanksGet = (ArrayList<DataBank>) xStream.fromXML(fileInputStream);
            fileInputStream.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
                
        for (int i = 0; i < dataBanksGet.size(); i++) {
            if (dataBanksGet.get(i).getId() == id) {

            } else {
                DataBank dataBank = new DataBank();
                dataBank.setId(dataBanksGet.get(i).getId());
                dataBank.setCredit(dataBanksGet.get(i).getCredit());
                dataBank.setDebit(dataBanksGet.get(i).getDebit());
                dataBank.setDesc(dataBanksGet.get(i).getDesc());
                dataBanksDelete.add(dataBank);
            }
        }
        
        String xml = xStream.toXML(dataBanksDelete);
        FileOutputStream fileOutputStream;
        
        try {
              byte[] data = xml.getBytes("UTF-8");
              fileOutputStream = new FileOutputStream("src/xml/Bank.xml");
              fileOutputStream.write(data);
              fileOutputStream.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        readData();
    }
}