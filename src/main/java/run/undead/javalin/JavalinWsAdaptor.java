package run.undead.javalin;

import run.undead.config.Config;
import run.undead.context.WsHandler;
import io.javalin.websocket.WsConfig;
import run.undead.view.View;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *  JavalinWsAdaptor connects Javalin websockets to the Undead {@link WsHandler} which
 *  handles the websocket lifecycle of the Undead {@link View}.
 */
public class JavalinWsAdaptor {

  private final Map<String, WsHandler> handlerRegistry = new ConcurrentHashMap<>();
  private final Config undeadConf;

  public JavalinWsAdaptor(Config undeadConf, WsConfig wsConfig) {
    this.undeadConf = undeadConf;
    wsConfig.onConnect(ctx -> {
      // client sends heartbeat every 30 seconds which is the default idleTimeout,
      // therefore we need to extend default idle timeout to longer
      ctx.session.setIdleTimeout(Duration.of(45, ChronoUnit.SECONDS));
    });
    wsConfig.onError(ctx -> {
      var handler = getHandler(ctx.getSessionId());
      if(handler == null) {
        return;
      }
      handler.setWsSender(new JavalinWsSender(ctx));
      // give socket a chance to handle error
      handler.handleError(ctx.error());
      clearHandler(ctx.getSessionId());
    });
    wsConfig.onClose(ctx -> {
      var handler = getHandler(ctx.getSessionId());
      if(handler == null) {
        return;
      }
      // give socket a chance to handle close / cleanup
      handler.handleClose();
      clearHandler(ctx.getSessionId());
    });
    wsConfig.onMessage(ctx -> {
      undeadConf.debug.accept("WS message: " + ctx.message());
      var handler = getOrCreateHandler(ctx.getSessionId());
      handler.setWsSender(new JavalinWsSender(ctx));
      handler.handleMessage(ctx.message());
    });
  }

  private WsHandler getHandler(String sessionId) {
    return handlerRegistry.get(sessionId);
  }

  private WsHandler getOrCreateHandler(String sessionId) {
    return handlerRegistry.computeIfAbsent(sessionId, k -> new WsHandler(undeadConf));
  }

  private void clearHandler(String sessionId) {
    handlerRegistry.remove(sessionId);
  }

}
