package nhom1.p2p;

import nhom1.p2p.config.AppConfig;
import nhom1.p2p.entity.Peer;

import java.io.File;
import java.util.Scanner;

public class Test02 {

    private static final AppConfig appConfig = new AppConfig();
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        var pool = appConfig.executorService();

        Peer peer = Peer.builder()
                .pool(pool)
                .port(5000)
                .name("vu")
                .build();

        System.out.println("============== P2P NODE READY ==============");
        System.out.println("Peer name: " + peer.getName());
        System.out.println("My IP: " + peer.getMyIP());
        System.out.println("Listening port: " + peer.getPort());
        System.out.println("============================================");

        peer.listening();

        while (true) {
            System.out.println();
            System.out.println("Select file to send:");
            File file = inputFile();

            String host = inputString("Destination host: ");
            int port = inputInt("Destination port: ");

            peer.sendFile(host, port, file);
        }
    }

    private static File inputFile() {
        while (true) {
            System.out.print("File path: ");
            String path = sc.nextLine().trim();
            File file = new File(path);
            if (file.exists() && file.isFile()) {
                return file;
            }
            System.out.println("Invalid file. Try again.");
        }
    }

    private static String inputString(String msg) {
        System.out.print(msg);
        return sc.nextLine().trim();
    }

    private static int inputInt(String msg) {
        while (true) {
            System.out.print(msg);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Invalid number. Try again.");
            }
        }
    }
}
