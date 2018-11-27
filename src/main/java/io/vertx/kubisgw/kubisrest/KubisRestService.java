package io.vertx.kubisgw.kubisrest;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.Handler;
import io.vertx.ext.web.client.WebClient;

@ProxyGen
public interface KubisRestService {
  @Fluent
  KubisRestService getVersion(Handler<AsyncResult<JsonObject>> resultHandler);

  @Fluent
  KubisRestService getMetadata(Handler<AsyncResult<JsonObject>> resultHandler);

  static KubisRestService create (WebClient webClient, Handler<AsyncResult<KubisRestService>> readyHandler){
    return new KubisRestServiceImpl(webClient, readyHandler);
  }

  static KubisRestService createProxy(Vertx vertx, String address) {
    return new KubisRestServiceVertxEBProxy(vertx, address);
  }
}
