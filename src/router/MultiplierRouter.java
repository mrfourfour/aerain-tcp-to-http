package router;

import io.github.aerain.http.HttpMethod;
import io.github.aerain.http.HttpRequest;
import io.github.aerain.http.HttpResponse;
import io.github.aerain.http.server.RequestMapping;
import io.github.aerain.http.server.RouterFunction;

public class MultiplierRouter implements RouterFunction {
    @Override
    @RequestMapping(method = HttpMethod.POST, value = "/multiplier")
    public void route(HttpRequest req, HttpResponse res) {
        Integer a = Integer.valueOf(req.getQueryParam("a"));
        Integer b = Integer.valueOf(req.getQueryParam("b"));
        res.setBody(a + b + "입니다.");
    }
}
