module dhkthn.p2p {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    opens dhkthn.p2p to javafx.fxml;
    exports dhkthn.p2p;
}