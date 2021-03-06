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
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

// import the required libraries
public class AddMatchController implements Initializable {
    // Some @FXML declarations
    @FXML
    Button cancelBtn;

    @FXML
    Button saveBtn;

    // Some local variable declarations
    @FXML
    ComboBox<String> homeTeamBox ;
    @FXML
    ComboBox<String> visitorTeamBox ;

    // The data variable is used to populate the ComboBoxs
    final ObservableList<String> data = FXCollections.observableArrayList();
    // To reference the models inside the controller
    private MatchesAdapter matchesAdapter;
    private TeamsAdapter teamsAdapter;
    public void setModel(MatchesAdapter match, TeamsAdapter team) {
        matchesAdapter = match;
        teamsAdapter = team;
        buildComboBoxData();
    }
    @FXML
    // closes the tab when cancel button is pressed
    public void cancel() {

        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }
    @FXML
    // saves information to the DB and closes the tab
    public void save()  {

        try {
            matchesAdapter.insertMatch(matchesAdapter.getMax(), homeTeamBox.getValue(), visitorTeamBox.getValue());
        } catch (SQLException ex) {
            displayAlert("ERROR: " + ex.getMessage());
        }

        Stage stage = (Stage) saveBtn.getScene().getWindow();
        stage.close();
    }
    //populates the combo box with the data from the DB
    public void buildComboBoxData() {
        try {
            data.addAll(teamsAdapter.getTeamsNames());
        } catch (SQLException ex) {
            displayAlert("ERROR: " + ex.getMessage());
        }
    }
    @Override
    // sets the combo box with data
    public void initialize(URL url, ResourceBundle rb) {

        homeTeamBox.setItems(data);
        visitorTeamBox.setItems(data);
    }

    // opens a popup to display error message
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
