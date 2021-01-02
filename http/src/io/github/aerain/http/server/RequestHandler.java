package io.github.aerain.http.server;

import io.github.aerain.http.HttpMethod;
import io.github.aerain.http.HttpRequest;
import io.github.aerain.http.HttpResponse;
import io.github.aerain.http.HttpStatus;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

public class RequestHandler {
    private static Logger logger = Logger.getLogger(RequestHandler.class.getSimpleName());

    private final Map<String, Map<HttpMethod, RouterFunction>> handlerMap;

    public RequestHandler() {
        this.handlerMap = new HashMap<>();
    }

    public RequestHandler get(String requestUrl, RouterFunction routerFunction) {
        add(requestUrl, HttpMethod.GET, routerFunction);
        return this;
    }

    public RequestHandler post(String requestUrl, RouterFunction routerFunction) {
        add(requestUrl, HttpMethod.POST, routerFunction);
        return this;
    }

    private void add(String requestUrl, HttpMethod method, RouterFunction routerFunction) {
        if (!handlerMap.containsKey(requestUrl)) {
            handlerMap.put(requestUrl, new HashMap<>());
        }

        Map<HttpMethod, RouterFunction> routerFunctionPerMethod = handlerMap.get(requestUrl);

        if (routerFunctionPerMethod.containsKey(method)) {
            throw new DuplicateRouterFunctionException();
        }

        routerFunctionPerMethod.put(method, routerFunction);
    }

    public void handle(final HttpRequest req, final HttpResponse res) {
        Optional.ofNullable(handlerMap.get(req.getRequestUrl()))
                .map(it -> it.get(req.getHttpMethod()))
                .ifPresentOrElse(
                        it -> it.route(req, res),
                        () -> handleNotFound(req, res)
                );
    }

    private void handleNotFound(HttpRequest req, HttpResponse res) {
        logger.info("handle Not Found: url = " + req.getRequestUrl());
        res.setHttpStatus(HttpStatus.NOT_FOUND);
        res.setBody("Not Found");
        res.getHttpHeaders().put("Content-Type", "text/html");
    }

    public RequestHandler add(RouterFunction... routerFunctions) {
        for (RouterFunction rf : routerFunctions) {
            add(rf);
        }
        return this;
    }

    /**
     * use if RouterFunction has RequestMapping
     * if there isn't RequestMapping annotation. it won't work.
     *
     * @link RequestMapping
     * @param routerFunction
     * @return
     */
    public RequestHandler add(RouterFunction routerFunction) {
        RequestMapping requestMapping = getRequestMapping(routerFunction);
        if (requestMapping == null) {
            logger.warning("There isn't RequestMapping annotation. routerFunction won't work.");
            return this;
        }

        HttpMethod httpMethod = requestMapping.method();
        String requestUrl = getRequestUrl(requestMapping);

        add(requestUrl, httpMethod, routerFunction);
        return this;
    }

    private RequestMapping getRequestMapping(RouterFunction routerFunction){
        Method method;
        try {
            method = routerFunction.getClass().getMethod("route", HttpRequest.class, HttpResponse.class);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("error occurred while add router function");
        }

        return method.getAnnotation(RequestMapping.class);
    }

    private String getRequestUrl(RequestMapping requestMapping) {
        return Optional.ofNullable(requestMapping)
                .map(RequestMapping::value)
                .filter(it -> !it.isBlank())
                .orElseThrow(() -> new IllegalArgumentException("Couldn't parse requestUrl"));
    }
}
