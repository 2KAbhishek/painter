module com.iam2kabhishek.painter {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.iam2kabhishek.painter to javafx.fxml;
    exports com.iam2kabhishek.painter;
}
