package dhkthn.p2p.model;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

public interface IPeer{
    void start();
    void stop();
    boolean isRunning();

    void listening();
    void sendFile(File file, String host, int port) throws IOException;
    void receiveFile(Socket socket) throws IOException;
    String getMyIp();
}