package io.vertx.kubisgw.http;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.kubisgw.MainVerticle;
import io.vertx.kubisgw.kubisrest.KubisRestService;

public class HttpVerticle extends AbstractVerticle {
  private KubisRestService kubisRestService;
  private final static Logger LOGGER = LoggerFactory.getLogger(MainVerticle.class);

  public void start(Future<Void> startFuture) throws Exception {
    kubisRestService = KubisRestService.createProxy(vertx, "kubis-rest");
    HttpServer httpServer = vertx.createHttpServer();
    Router router = Router.router(vertx);
    router.get("/version").handler(this::getVersion);
    router.get("/metadata").handler(this::getMetadata);
    router.get("/health").handler(this::healthCheck);


    httpServer.requestHandler(router::accept).listen(8080, ar -> {
      if (ar.succeeded()) {
        LOGGER.info("HTTP server is running on port 8080");
        startFuture.complete();
      } else {
        LOGGER.error("Could not start HTTP server", ar.cause());
        startFuture.fail(ar.cause());
      }
    });
  }

  private void getVersion(RoutingContext ctx) {
    String appUser = ctx.request().getHeader("X-APP-USER");
    kubisRestService.getVersion(appUser, ar -> {
      if (ar.succeeded()) {
        ctx
          .response()
          .putHeader("content-type", "application/json")
          .end(ar.result().toString());
      } else {
        ctx.fail(ar.cause());
      }
    });
  }

  private void getMetadata(RoutingContext ctx) {
    String appUser = ctx.request().getHeader("X-APP-USER");
    kubisRestService.getMetadata(appUser, ar -> {
      if (ar.succeeded()) {
        ctx
          .response()
          .putHeader("content-type", "application/json")
          .end(ar.result().toString());
      } else {
        ctx.fail(ar.cause());
      }
    });
  }

  private void healthCheck(RoutingContext ctx) {
    ctx
      .response()
      .putHeader("content-type", "application/json")
      .end("ok");
  }
}
