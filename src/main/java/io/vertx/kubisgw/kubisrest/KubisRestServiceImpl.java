package io.vertx.kubisgw.kubisrest;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.kubisgw.MainVerticle;
import io.vertx.core.buffer.Buffer;

public class KubisRestServiceImpl implements KubisRestService {
  private final static Logger LOGGER = LoggerFactory.getLogger(MainVerticle.class);
  private WebClient webClient;

  public KubisRestServiceImpl(WebClient webClient, Handler<AsyncResult<KubisRestService>> readyHandler){
    this.webClient = webClient;
    readyHandler.handle(Future.succeededFuture(this));
  }

  public KubisRestService getVersion(Handler<AsyncResult<JsonObject>> resultHandler) {
    LOGGER.info("Fetching service version");
    webClient
      .get("kuibs-rest", "/v1/system/version")
      .send(ar -> {
        if (ar.succeeded()) {
          HttpResponse<Buffer> response = ar.result();
          if (response.statusCode() != 200) {
            LOGGER.error("Error during fetching kubis-rest version, Status code: " + response.statusCode());
            resultHandler.handle(Future.failedFuture(ar.cause()));
          }
          else{
            LOGGER.info("Kuibs-rest version is here, sending response");
            resultHandler.handle(Future.succeededFuture(response.bodyAsJsonObject()));
          }
        }
        else{
          LOGGER.error("Error during fetching Kubis-rest version");
          resultHandler.handle(Future.failedFuture(ar.cause()));
        }
      });
    return this;
  }

  public KubisRestService getMetadata(Handler<AsyncResult<JsonObject>> resultHandler) {
    LOGGER.info("Fetching service metadata");
    webClient
      .get("kuibs-rest", "/v1/system/metadata")
      .send(ar -> {
        if (ar.succeeded()) {
          HttpResponse<Buffer> response = ar.result();
          if (response.statusCode() != 200) {
            LOGGER.error("Error during fetching kubis-rest metadata, Status code: " + response.statusCode());
            resultHandler.handle(Future.failedFuture(ar.cause()));
          }
          else{
            LOGGER.info("Kuibs-rest metadata is here, sending response");
            resultHandler.handle(Future.succeededFuture(response.bodyAsJsonObject()));
          }
        }
        else{
          LOGGER.error("Error during fetching Kubis-rest metadata");
          resultHandler.handle(Future.failedFuture(ar.cause()));
        }
      });
    return this;
  }

}
