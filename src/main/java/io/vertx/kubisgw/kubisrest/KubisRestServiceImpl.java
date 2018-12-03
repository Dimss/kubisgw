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

  public KubisRestService getVersion(String appUser, Handler<AsyncResult<JsonObject>> resultHandler) {
    LOGGER.info("Fetching service version");
    if (appUser == null) appUser = "anonymous";
    webClient
      .get("kubis-rest", "/v1/system/version")
      .putHeader("X-APP-USER", appUser)
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

  public KubisRestService getMetadata(String appUser, Handler<AsyncResult<JsonObject>> resultHandler) {
    LOGGER.info("Fetching service metadata");
    if (appUser == null) appUser = "anonymous";
    String kubisRestHost = "kubis-rest";
    // If X-APP-USER equals insec, route request to the insecure instance of Kubis Rest service
    if (appUser.equals("insec")) kubisRestHost = "kubis-rest-insec";
    webClient
      .get(kubisRestHost, "/v1/system/metadata")
      .putHeader("X-APP-USER", appUser)
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

  @Override
  public KubisRestService saveMessage(JsonObject message, Handler<AsyncResult<JsonObject>> resultHandler) {
    LOGGER.info("Saving message");
    webClient
      .post("kubis-rest", "/v1/system/message")
      .putHeader("X-APP-USER", "anonymous")
      .sendJsonObject(message, ar ->{
        if (ar.succeeded()) {
          HttpResponse<Buffer> response = ar.result();
          if (response.statusCode() != 200) {
            LOGGER.error("Error during saving  kubis-rest message, Status code: " + response.statusCode());
            resultHandler.handle(Future.failedFuture(ar.cause()));
          }
          else{
            LOGGER.info("Kuibs-rest message saving is done, sending response");
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

  public KubisRestService block(Handler<AsyncResult<JsonObject>> resultHandler) {
    LOGGER.info("Executing block method");
    webClient
      .get("kubis-rest", "/v1/system/block")
      .send(ar -> {
        if (ar.succeeded()) {
          HttpResponse<Buffer> response = ar.result();
          if (response.statusCode() != 200) {
            LOGGER.error("Error during executing kubis-rest block method, Status code: " + response.statusCode());
            resultHandler.handle(Future.failedFuture(ar.cause()));
          }
          else{
            LOGGER.info("Kuibs-rest block is here, sending response");
            resultHandler.handle(Future.succeededFuture(response.bodyAsJsonObject()));
          }
        }
        else{
          LOGGER.error("Error during fetching Kubis-rest block");
          resultHandler.handle(Future.failedFuture(ar.cause()));
        }
      });
    return this;
  }
}
