package ch.epfl.dedis.template.gui.index;

import ch.epfl.dedis.template.gui.errorScene.ErrorSceneController;
import ch.epfl.dedis.template.gui.json.Person;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import  javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ResourceBundle;

import static ch.epfl.dedis.template.gui.ByzSetup.*;
import static ch.epfl.dedis.template.gui.index.Main.homePath;


public class IndexController implements Initializable {


    @FXML
    private Label sclogoLabel;

    @FXML
    private Label aslogoLabel;

    @FXML
    private Label epflLogoLabel;

    @FXML
    private MenuButton roleButton;

    @FXML
    public static String role;

    @FXML
    private Button submitRoleButton;

    @FXML
    private AnchorPane container;

    private File userFile = new File(homePath + "/json/user.json");
    private File readerFile = new File(homePath + "/json/reader.json");
    private File garageFile = new File(homePath + "/json/garage.json");

    ObjectMapper mapper = new ObjectMapper();

    ArrayList<File> fileList = new ArrayList<>(
        Arrays.asList(userFile, readerFile, garageFile)
    );

    @Override
    public void initialize(URL location, ResourceBundle resources){

        try {

            File fireBackgroung = new File("src/main/java/ch/epfl/dedis/template/gui/index/background.png");
            Image img = new Image(fireBackgroung.getAbsoluteFile().toURI().toString());
            BackgroundImage bgImg = new BackgroundImage(img,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.DEFAULT,
                    BackgroundSize.DEFAULT);
            container.setBackground(new Background(bgImg));

            URL urlsc = new File("src/main/java/ch/epfl/dedis/template/gui/index/sclogo.png").toURL();
            Image sclogo = new Image(urlsc.openStream(), 50, 50, true, true);
            ImageView viewSC = new ImageView(sclogo);
            sclogoLabel.setGraphic(viewSC);

            URL urlas = new File("src/main/java/ch/epfl/dedis/template/gui/index/autosenselogo.png").toURL();
            Image aslogo = new Image(urlas.openStream(), 45, 45, true, true);
            ImageView viewAS = new ImageView(aslogo);
            aslogoLabel.setGraphic(viewAS);

            URL urlepfl = new File("src/main/java/ch/epfl/dedis/template/gui/index/logoEPFL.png").toURL();
            Image epflLogo = new Image(urlepfl.openStream(), 70, 45, true, true);
            ImageView viewEpfl = new ImageView(epflLogo);
            epflLogoLabel.setGraphic(viewEpfl);

            MenuItem adminItem = new MenuItem(getAdminName());
            adminItem.setOnAction(this::onRoleChange);
            roleButton.getItems().add(adminItem);

            for (File file : fileList){
                if(file.exists())
                {
                    HashMap<String, Person> personMap = mapper.readValue(file, HashMap.class);

                    for (HashMap.Entry<String, Person> entry : personMap.entrySet()) {
                        MenuItem userItem = new MenuItem(entry.getKey());
                        userItem.setOnAction(this::onRoleChange);
                        roleButton.getItems().add(userItem);
                    }
                }
            }
            submitRoleButton.setStyle("-fx-background-color: #001155; -fx-text-fill: white");
            submitRoleButton.setOnAction(this::onRoleSubmit);
        }
        catch (Exception e) {
            Main.errorMsg = e.toString();
            Main.loadErrorScene();
            Main.window.setScene(Main.errorScene);
            e.printStackTrace();
        }
    }

    private void onRoleSubmit(ActionEvent event)
    {
        try{

//            HashMap<String, Person> userMap = mapper.readValue(userFile, HashMap.class);
//            HashMap<String, Person> readerMap = mapper.readValue(readerFile, HashMap.class);
//            HashMap<String, Person> garageMap = mapper.readValue(garageFile, HashMap.class);

            role = roleButton.getText();
            //todo same name for reader garage user...
            if (roleButton.getText().equals(getAdminName())){
                URL urlSignUp = new File("src/main/java/ch/epfl/dedis/template/gui/Register/Register.fxml").toURL();
                Parent rootSignUp = FXMLLoader.load(urlSignUp);
                Main.adminScene = new Scene(rootSignUp, 600, 400);
                Main.window.setScene(Main.adminScene);
            }
            else if (userFile.exists() && mapper.readValue(userFile, HashMap.class).containsKey(roleButton.getText())){
                URL urlUserScreen = new File("src/main/java/ch/epfl/dedis/template/gui/userScreen/userScreen.fxml").toURL();
                Parent rootUserScreen = FXMLLoader.load(urlUserScreen);
                Main.userScreenScene = new Scene(rootUserScreen, 600, 400);
                Main.window.setScene(Main.userScreenScene);
            }
            else if(readerFile.exists() && mapper.readValue(readerFile, HashMap.class).containsKey(roleButton.getText())){
                URL urlReadHistory = new File("src/main/java/ch/epfl/dedis/template/gui/readHistory/readHistory.fxml").toURL();
                Parent rootReadHistory = FXMLLoader.load(urlReadHistory);
                Main.readHistoryScene = new Scene(rootReadHistory, 600, 400);
                Main.window.setScene(Main.readHistoryScene);
            }
            else if(garageFile.exists() && mapper.readValue(garageFile, HashMap.class).containsKey(roleButton.getText())){
                URL urlAddReport = new File("src/main/java/ch/epfl/dedis/template/gui/addReport/addReport.fxml").toURL();
                Parent rootAddReport = FXMLLoader.load(urlAddReport);
                Main.addReportScene = new Scene(rootAddReport, 600, 400);
                Main.window.setScene(Main.addReportScene);
            }
            roleButton.setText("Identity");
        }
        catch (Exception e) {
            Main.errorMsg = e.toString();
            Main.loadErrorScene();
            Main.window.setScene(Main.errorScene);
            e.printStackTrace();
        }
    }

    private void onRoleChange(ActionEvent event) {
        String Identity = ((MenuItem) event.getTarget()).getText();
        roleButton.setText(Identity);
    }
}
