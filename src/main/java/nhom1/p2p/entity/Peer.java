package nhom1.p2p.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Peer {

    @Builder.Default
    private UUID id = UUID.randomUUID();
    private String name;
    private int port;
    private ExecutorService pool;

    public String getMyIP() {
        try {
            Enumeration<NetworkInterface> nics = NetworkInterface.getNetworkInterfaces();
            while (nics.hasMoreElements()) {
                NetworkInterface nic = nics.nextElement();
                if (!nic.isUp() || nic.isLoopback() || nic.isVirtual()) {
                    continue;
                }
                Enumeration<InetAddress> addrs = nic.getInetAddresses();
                while (addrs.hasMoreElements()) {
                    InetAddress addr = addrs.nextElement();
                    if (addr instanceof Inet4Address) {
                        return addr.getHostAddress();
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return "127.0.0.1";
    }

    public void listening() {
        pool.execute(() -> {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                System.out.println("Listening on " + getMyIP() + ":" + port);
                while (true) {
                    Socket socket = serverSocket.accept();
                    pool.execute(() -> receiveFile(socket));
                }
            } catch (IOException e) {
                System.out.println("Listening error: " + e.getMessage());
            }
        });
    }

    public void sendFile(String host, int port, File file) {
        if (file == null || !file.exists() || !file.isFile()) {
            System.out.println("Send file error: invalid file");
            return;
        }

        try (Socket socket = new Socket(host, port);
             FileInputStream fis = new FileInputStream(file);
             BufferedInputStream bis = new BufferedInputStream(fis);
             DataOutputStream dos = new DataOutputStream(
                     new BufferedOutputStream(socket.getOutputStream()))) {

            dos.writeUTF(file.getName());
            dos.writeLong(file.length());

            byte[] buffer = new byte[8192];
            int read;
            while ((read = bis.read(buffer)) != -1) {
                dos.write(buffer, 0, read);
            }
            dos.flush();

            System.out.println("Sent file: " + file.getName() + " to " + host + ":" + port);

        } catch (IOException e) {
            System.out.println("Send file error: " + e.getMessage());
        }
    }

    public void receiveFile(Socket socket) {
        try (Socket s = socket;
             DataInputStream dis = new DataInputStream(
                     new BufferedInputStream(s.getInputStream()))) {

            String fileName = dis.readUTF();
            long fileSize = dis.readLong();

            File dir = new File("downloads");
            if (!dir.exists() && !dir.mkdirs()) {
                System.out.println("Receive file error: cannot create downloads directory");
                return;
            }

            File file = new File(dir, fileName);
            try (FileOutputStream fos = new FileOutputStream(file);
                 BufferedOutputStream bos = new BufferedOutputStream(fos)) {

                byte[] buffer = new byte[8192];
                long total = 0;
                int read;

                while (total < fileSize && (read = dis.read(buffer)) != -1) {
                    bos.write(buffer, 0, read);
                    total += read;
                }
                bos.flush();
            }

            System.out.println("Saved: " + file.getAbsolutePath());

        } catch (IOException e) {
            System.out.println("Receive file error: " + e.getMessage());
        }
    }
}
