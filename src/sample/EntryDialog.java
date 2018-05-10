package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class EntryDialog {

  @FXML
  private TextField heightField;
  @FXML
  private TextField widthField;
  @FXML
  private Label errorLabel;

  @FXML
  public void createTerrainMap(){
    DefaultScreen defaultScreen = new DefaultScreen();
    int height = 0;
    int width = 0;
    try {
      height = Integer.parseInt(heightField.getText().trim());
      width = Integer.parseInt(widthField.getText().trim());
    }catch (Exception e){
      errorLabel.setText("Invalid Input");
      return;
    }
    if (height > 0 && width > 0) {
      defaultScreen.setUpDefaultScreen(height,width);
    }
    else{
      errorLabel.setText("Please type in numbers > 0");
    }
  }
}
