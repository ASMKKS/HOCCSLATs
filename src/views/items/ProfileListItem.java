package views.items;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;

import controllers.ProfileController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import models.profiles.ProfileLoader;
import views.ViewManager;

public class ProfileListItem extends HBox {
	private JFXButton selButton;
	private JFXCheckBox selDelete;
	private HashMap<String, String> profileInfo;
	private Label name;
	private Label age;
	private Label gender;
	private HBox ageContainer;
	private Boolean isDeleteMode = false;
	public Boolean isSelectedForDelete = false;

	public ProfileListItem(HashMap<String, String> info) {
		this.getStylesheets().add(ProfileListItem.class.getResource("/resources/styles/profileListItemStyle.css").toString());
		this.profileInfo = info;
		this.setId("item");
		this.setAlignment(Pos.CENTER_LEFT);
		initializeItemContent();
		this.getChildren().addAll(name, gender, age, ageContainer);
	}
	
	private void initializeItemContent() {
		name = new Label("姓名: " + profileInfo.get("name"));
		name.setPrefSize(150, 50);
		name.setTranslateX(50);
		name.setFont(Font.font("System", 20));

		age = new Label("测试年龄: ");
		age.setPrefSize(100, 50);
		age.setTranslateX(200);
		age.setFont(Font.font("System", 20));

		String[] ages = profileInfo.get("ages").split(",");
		ageContainer = new HBox();
		ageContainer.setPrefSize(ages.length * 60, 50);
		ageContainer.setTranslateX(200);
		ageContainer.setSpacing(10);
		ageContainer.setId("age_container");
		ageContainer.setAlignment(Pos.CENTER_LEFT);
		if (ages[0].equalsIgnoreCase("")) {
			Label label = new Label("暂无");
			label.setPrefSize(40, 30);
			label.setFont(Font.font("System", 15));
			label.setAlignment(Pos.CENTER);
			ageContainer.getChildren().add(label);
		} else {
			for (String age : ages) {
				Label label = new Label(age);
				label.setPrefSize(40, 30);
				label.setFont(Font.font("System", 15));
				label.setAlignment(Pos.CENTER);
				ageContainer.getChildren().add(label);
			}
		}
		
		String genderString = profileInfo.get("gender").equals("male") ? "男" : "女";
		gender = new Label("性别: " + genderString);
		gender.setPrefSize(200, 50);
		gender.setTranslateX(200);
		gender.setFont(Font.font("System", 20));
		
		selButton = new JFXButton("选择");
		selButton.setTranslateX(500);
		selButton.setPrefSize(100, 50);
		selButton.setTextFill(Color.AZURE);
		selButton.setFont(Font.font("System", 20));
		selButton.setOnAction(event -> ViewManager.getInstance().switchProfileViewScene(ProfileLoader.profiles.get(this)));
		
		selDelete = new JFXCheckBox("删除");
		selDelete.setTranslateX(500);
		selDelete.setPrefSize(USE_COMPUTED_SIZE, 50);
		selDelete.setFont(Font.font("System", 20));
		selDelete.setCheckedColor(Color.CRIMSON);
		selDelete.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldValue, Boolean newValue) {
                isSelectedForDelete = newValue;
            }
		});
	}

	public void setHandleSelectForAssessment(ProfileController controller) {
		selButton.setOnAction(event -> {
			controller.onSelectProfileForAssessment(ProfileLoader.profiles.get(this));
		});
	}
	
	public void onSelectForView() {
		if (!isDeleteMode) {
			this.getChildren().add(selButton);
		}
	}
	
	public void onLoseFocus() {
		if (!isDeleteMode) {
			this.getChildren().remove(selButton);
		}
	}
	
	public void displayDeleteCheckbox() {
		isDeleteMode = true;
		this.getChildren().add(selDelete);
	}
	
	public void hideDeleteCheckbox() {
		isDeleteMode = false;
		this.getChildren().remove(selDelete);
	}
	
	public void clearDeleteSelect() {
		selDelete.setSelected(false);
	}
}
