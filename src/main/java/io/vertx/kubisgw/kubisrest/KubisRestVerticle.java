package io.vertx.kubisgw.kubisrest;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.ext.web.client.WebClient;
import io.vertx.serviceproxy.ServiceBinder;

public class KubisRestVerticle extends AbstractVerticle {
  public void start(Future<Void> startFeature) throws Exception {
    KubisRestService.create(WebClient.create(vertx), ready -> {
      ServiceBinder serviceBinder = new ServiceBinder(vertx);
      serviceBinder
        .setAddress("kubis-rest")
        .register(KubisRestService.class, ready.result());
      startFeature.complete();
    });
  }
}
