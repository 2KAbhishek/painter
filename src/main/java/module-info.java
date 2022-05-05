module com.iam2kabhishek.painter {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.iam2kabhishek.painter to javafx.fxml;
    exports com.iam2kabhishek.painter;
}
