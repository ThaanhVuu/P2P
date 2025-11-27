package dhkthn.p2p.service.message;

import dhkthn.p2p.model.IPeer;
import lombok.*;

import java.io.File;

@Getter @Setter @RequiredArgsConstructor
@Builder
public class FileSender implements IFile{
    private final IPeer peer;

    @Override
    public void sendFile(File file, String host, int port) {
//        peer.sendFile(file, host, port);
    }

    @Override
    public void receiveFile() {

    }

    @Override
    public void sendMessageSendFile() {

    }
}
