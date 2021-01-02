package router;

import io.github.aerain.http.HttpMethod;
import io.github.aerain.http.HttpRequest;
import io.github.aerain.http.HttpResponse;
import io.github.aerain.http.server.RequestMapping;
import io.github.aerain.http.server.RouterFunction;

public class HelloWorld implements RouterFunction {
    @Override
    @RequestMapping(method = HttpMethod.GET, value = "/")
    public void route(HttpRequest req, HttpResponse res) {
        res.setBody("hello, my friend ^^7");
    }
}
