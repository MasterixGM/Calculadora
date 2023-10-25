module com.example.calculadora1 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.scripting;
    requires exp4j;

    opens com.example.calculadora to javafx.fxml;
    exports com.example.calculadora;
}