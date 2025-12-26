module org.example.universitymanagementsystem {

    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires java.sql;

    // 🔴 THIS LINE IS MISSING — ADD IT
    opens org.example.universitymanagementsystem.controllers to javafx.fxml;

    // This one is fine
    opens org.example.universitymanagementsystem to javafx.fxml;

    exports org.example.universitymanagementsystem;
}
