package TennisBallGames;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

// import the required libraries
public class AddScoreController implements Initializable {


    //added fxml functionality
    @FXML
    Button cancelBtn;

    @FXML
    Button saveBtn;

    @FXML
    ComboBox matchBox;

    @FXML
    TextField homeTeam;

    @FXML
    TextField visitorTeam;

    final ObservableList<String> data = FXCollections.observableArrayList();
    private MatchesAdapter matchesAdapter;
    private TeamsAdapter teamsAdapter;

    public void setModel(MatchesAdapter match, TeamsAdapter team) {
        teamsAdapter = team;
        matchesAdapter = match;
        buildComboBoxData();
    }

    // populates combo box with the match list
    public void buildComboBoxData() {
        try {
            data.addAll(matchesAdapter.getMatchesNamesList());
        } catch (SQLException ex) {
            displayAlert("ERROR: " + ex.getMessage());


        }
    }

    public void initialize(URL url, ResourceBundle rb) {
        matchBox.setItems(data);
    }


    @FXML
    // closes the popup when cancel button is pressed
    public void cancel() {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    @FXML
    // saves data to the DB and closes popup
    public void save() {
        try {
            String match = matchBox.getValue().toString();
            String[] matchInfo = matchBox.getValue().toString().split("-");
            // sets the team score
            matchesAdapter.setTeamsScore(Integer.parseInt(matchBox.getValue().toString().substring(0, 1)), Integer.parseInt(homeTeam.getText()), Integer.parseInt(visitorTeam.getText()));

            // sets the win/loss
            teamsAdapter.setStatus(matchInfo[1].trim(), matchInfo[2].trim(), Integer.parseInt(homeTeam.getText()), Integer.parseInt(visitorTeam.getText()));


        } catch (SQLException ex) {
            displayAlert("ERROR: " + ex.getMessage());
        }


        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    // popup displaying the error
    private void displayAlert(String msg) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("Alert.fxml"));
            Parent ERROR = loader.load();
            AlertController controller = (AlertController) loader.getController();

            Scene scene = new Scene(ERROR);
            Stage stage = new Stage();
            stage.setScene(scene);

            stage.getIcons().add(new Image("file:src/TennisBallGames/WesternLogo.png"));
            controller.setAlertText(msg);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException ex1) {

        }
    }

}