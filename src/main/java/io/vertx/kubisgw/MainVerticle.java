package io.vertx.kubisgw;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class MainVerticle extends AbstractVerticle {
  private final static Logger LOGGER = LoggerFactory.getLogger(MainVerticle.class);

  @Override
  public void start(Future<Void> startFuture) throws Exception {
    vertx.deployVerticle("io.vertx.kubisgw.http.HttpVerticle", ar -> {
      LOGGER.info("*** HTTP Verticle deployed: " + ar.result());
    });
    vertx.deployVerticle("io.vertx.kubisgw.kubisrest.KubisRestVerticle", ar -> {
      LOGGER.info("*** KUBIS-REST Verticle deployed: " + ar.result());
    });
  }

}
