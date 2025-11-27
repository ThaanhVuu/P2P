package dhkthn.p2p.service.message;

import java.io.File;

public interface IFile {
    void sendFile(File file, String host, int port);
    void receiveFile();
    void sendMessageSendFile();
}
