package nhom1.p2p.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppConfig {
    public ExecutorService executorService(){
        return Executors.newFixedThreadPool(20);
    }
}
