package router;

import io.github.aerain.http.HttpMethod;
import io.github.aerain.http.HttpRequest;
import io.github.aerain.http.HttpResponse;
import io.github.aerain.http.server.RequestMapping;
import io.github.aerain.http.server.RouterFunction;

public class ProfileRouter implements RouterFunction {

    @Override
    @RequestMapping(method = HttpMethod.GET, value = "/profile")
    public void route(HttpRequest req, HttpResponse res) {
        String name = req.getQueryParam("name");
        res.setBody("Hello, " + name + "!");
    }
}
