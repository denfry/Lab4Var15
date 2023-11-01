package com.example.lab4var15;

import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class PeripheralDeviceTableApp extends Application {
    private final TableView<PeripheralDevice> table = new TableView<>();
    private final ObservableList<PeripheralDevice> devices = FXCollections.observableArrayList();

    private boolean ascendingSort = true;

    private final TextField nameTextField = new TextField();
    private final TextField priceTextField = new TextField();
    private final CheckBox wirelessCheckBox = new CheckBox("Wireless");
    private final TextField screenSizeTextField = new TextField();

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Peripheral Device Table");

        TableColumn<PeripheralDevice, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<PeripheralDevice, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<PeripheralDevice, Boolean> wirelessColumn = getWirelessColumn();

        TableColumn<PeripheralDevice, Integer> screenSizeColumn = new TableColumn<>("Screen Size");
        screenSizeColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue() instanceof Monitor) {
                return new SimpleIntegerProperty(((Monitor) cellData.getValue()).getScreenSize()).asObject();
            }
            return new SimpleIntegerProperty().asObject();
        });
        TableColumn<PeripheralDevice, String> deviceTypeColumn = getDeviceTypeColumn();

        table.getColumns().addAll(nameColumn, priceColumn, wirelessColumn, screenSizeColumn, deviceTypeColumn);
        table.setItems(devices);

        Button addButton = new Button("Add Device");
        addButton.setOnAction(e -> addDevice());

        Button addFromArrayButton = new Button("Add Devices from Array");
        addFromArrayButton.setOnAction(e -> addAllDevicesFromArray());

        Button sortButton = new Button("Sort by Price");
        sortButton.setOnAction(e -> sortDevicesByPrice());

        ToggleGroup sortToggleGroup = new ToggleGroup();
        RadioButton ascendingSortButton = new RadioButton("Ascending");
        ascendingSortButton.setToggleGroup(sortToggleGroup);
        ascendingSortButton.setSelected(true);
        RadioButton descendingSortButton = new RadioButton("Descending");
        descendingSortButton.setToggleGroup(sortToggleGroup);

        HBox sortBox = new HBox(10);
        sortBox.getChildren().addAll(sortButton, new Label("Sort Order:"), ascendingSortButton, descendingSortButton);

        HBox inputBox = new HBox(10);
        inputBox.getChildren().addAll(
                new Label("Name:"),
                nameTextField,
                new Label("Price:"),
                priceTextField,
                new Label("Wireless:"),
                wirelessCheckBox,
                new Label("Screen size:"),
                screenSizeTextField,
                addButton
        );

        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(table, inputBox, addFromArrayButton, sortBox);

        Scene scene = new Scene(vbox);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @NotNull
    private static TableColumn<PeripheralDevice, String> getDeviceTypeColumn() {
        TableColumn<PeripheralDevice, String> deviceTypeColumn = new TableColumn<>("Device Type");
        deviceTypeColumn.setCellValueFactory(cellData -> {
            PeripheralDevice device = cellData.getValue();
            String deviceType = device.getDeviceType();
            return new SimpleStringProperty(deviceType);
        });
        deviceTypeColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if ("Mouse".equals(item)) {
                        setStyle("-fx-background-color: lightblue;");
                    } else if ("Monitor".equals(item)) {
                        setStyle("-fx-background-color: lightgreen;");
                    } else {
                        setStyle("-fx-background-color: lightcoral;");
                    }
                }
            }
        });
        return deviceTypeColumn;
    }

    @NotNull
    private static TableColumn<PeripheralDevice, Boolean> getWirelessColumn() {
        TableColumn<PeripheralDevice, Boolean> wirelessColumn = new TableColumn<>("Wireless");
        wirelessColumn.setCellValueFactory(cellData -> {
            PeripheralDevice device = cellData.getValue();
            if (device instanceof Mouse) {
                return new SimpleBooleanProperty(((Mouse) cellData.getValue()).getWireless());
            }
            return new SimpleBooleanProperty().asObject();
        });
        wirelessColumn.setCellFactory(column -> new CheckBoxTableCell<PeripheralDevice, Boolean>() {
            private final CheckBox checkBox = new CheckBox();
            {
                checkBox.setAlignment(Pos.CENTER);
                setAlignment(Pos.CENTER);

                checkBox.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
                    if(isSelected){
                        setStyle("-fx-background-color: lightgreen;");
                    } else {
                        setStyle("-fx-background-color: lightcoral;");
                    }
                });
            }
            @Override
            public void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setGraphic(null);
                    setStyle("");
                } else {
                    checkBox.setSelected(item);
                    setGraphic(checkBox);
                    if (!item) {
                        setStyle("-fx-background-color: lightcoral;");
                    } else {
                        setStyle("-fx-background-color: lightgreen;");
                    }
                }
            }
        });
        return wirelessColumn;
    }


    private void addDevice() {
        try {
            String name = nameTextField.getText();
            double price = Double.parseDouble(priceTextField.getText());
            String deviceType = "Unknown";

            if (wirelessCheckBox.isSelected()) {
                boolean isWireless = true;
                deviceType = "Mouse";
                devices.add(new Mouse(name, price, isWireless, deviceType));
            } else {
                int screenSize = Integer.parseInt(screenSizeTextField.getText());
                deviceType = "Monitor";
                devices.add(new Monitor(name, price, screenSize, deviceType));
            }

            nameTextField.clear();
            priceTextField.clear();
            wirelessCheckBox.setSelected(false);
            screenSizeTextField.clear();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setHeaderText("Invalid input");
            alert.setContentText("Please enter a valid numeric value for Price or Screen Size.");
            alert.showAndWait();
        }
    }


    private void addAllDevicesFromArray() {
        devices.addAll(getDevicesFromArray());
    }

    private ObservableList<PeripheralDevice> getDevicesFromArray() {
        ObservableList<PeripheralDevice> devicesArray = FXCollections.observableArrayList();
        devicesArray.add(new Monitor("Монитор 1", 20000, 24, "Monitor"));
        devicesArray.add(new Mouse("Мышь 1", 3000, true, "Mouse"));
        devicesArray.add(new Monitor("Монитор 2", 15000, 19, "Monitor"));
        devicesArray.add(new Mouse("Мышь 2", 2500, false, "Mouse"));
        devicesArray.add(new Monitor("Монитор 3", 18000, 21, "Monitor"));
        devicesArray.add(new Mouse("Мышь 3", 3500, true, "Mouse"));
        devicesArray.add(new Monitor("Монитор 4", 25000, 27, "Monitor"));
        devicesArray.add(new Mouse("Мышь 4", 3000, false, "Mouse"));
        devicesArray.add(new Monitor("Монитор 5", 17000, 23, "Monitor"));
        devicesArray.add(new Mouse("Мышь 5", 4000, true, "Mouse"));
        return devicesArray;
    }

    private void sortDevicesByPrice() {
        ascendingSort = !ascendingSort;
        Comparator<PeripheralDevice> comparator = ascendingSort
                ? Comparator.comparingDouble(PeripheralDevice::getPrice)
                : (r1, r2) -> Double.compare(r2.getPrice(), r1.getPrice());

        devices.sort(comparator);
    }
}