module nhom1.p2p {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires static lombok;

    opens nhom1.p2p to javafx.fxml;
    exports nhom1.p2p;
}