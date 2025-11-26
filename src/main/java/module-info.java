module dhkthn.p2p {
    requires com.google.gson;
    requires MaterialFX;
    requires org.kordamp.bootstrapfx.core;
    requires static lombok;
    opens dhkthn.p2p to javafx.fxml;
    exports dhkthn.p2p;
}