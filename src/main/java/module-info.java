module nhom1.p2p {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens nhom1.p2p to javafx.fxml;
    exports nhom1.p2p;
}