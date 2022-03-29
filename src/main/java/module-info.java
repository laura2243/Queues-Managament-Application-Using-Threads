module com.example.tema2javafxbun {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.tema2javafxbun to javafx.fxml;
    exports com.example.tema2javafxbun;
}