import io.github.aerain.http.server.HttpServer;
import router.HelloWorld;
import router.MultiplierRouter;
import router.ProfileRouter;

public class Main {
    public static void main(String[] args) {
        HttpServer server = new HttpServer(8080);
        server.router()
                .add(new ProfileRouter(), new MultiplierRouter(), new HelloWorld());
        server.start();
    }
}
