package nhom1.p2p;

import nhom1.p2p.config.AppConfig;
import nhom1.p2p.entity.Peer;

import java.io.File;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Launcher {
    private static final AppConfig appConfig = new AppConfig();
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws UnknownHostException {
//        Application.launch(HelloApplication.class, args);
        var pool = appConfig.executorService();

        Peer peer = Peer.builder()
                .pool(pool)
                .port(5000)
                .name("vu")
                .build();

        peer.listening();

        while (true) {
            System.out.println("Send file: ");
            System.out.println("Enter path of File");
            File file = new File(sc.nextLine());
            System.out.println("Enter host: ");
            String host = sc.nextLine();
            System.out.println("Enter port: ");
            int port = sc.nextInt();
            sc.nextLine();

            peer.sendFile(host, port, file);
        }
    }
}
