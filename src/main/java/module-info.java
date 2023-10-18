module com.example.lab4var15 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires com.almasb.fxgl.all;
    requires annotations;

    opens com.example.lab4var15 to javafx.fxml;
    exports com.example.lab4var15;
}