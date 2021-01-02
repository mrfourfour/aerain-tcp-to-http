package io.github.aerain.http.server;

import io.github.aerain.http.HttpRequest;
import io.github.aerain.http.HttpResponse;

public interface RouterFunction {
    void route(HttpRequest req, HttpResponse res);
}
