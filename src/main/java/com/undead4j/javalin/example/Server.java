package com.undead4j.javalin.example;

import com.undead4j.config.Config;
import com.undead4j.javalin.example.view.UndeadCounter;
import com.undead4j.javalin.example.view.UndeadSalesDashboard;
import com.undead4j.javalin.example.view.UndeadUserForm;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

/**
 * Server is a simple Javalin server that uses Undead to render rich, dynamic views.
 */
public class Server {
  public static void main(String[] args) {

    var undeadConf = new Config();
    undeadConf.debug = msg -> System.out.println("Undead: " + msg);
    var app = new UndeadJavalin(Javalin.create(config ->
        // register static file locations for the Undead client javascript
        config.staticFiles.add(staticFileConfig -> {
          staticFileConfig.directory = "/public/js";
          staticFileConfig.location = Location.CLASSPATH;
          staticFileConfig.hostedPath = "/js";
        })
    ), undeadConf)
        // use the UndeadJavalin instance to register Undead Views to routes
        .undead("/count", new UndeadCounter())
        .undead("/count/{start}", new UndeadCounter())
        .undead("/dashboard", new UndeadSalesDashboard())
        .undead("/user/new", new UndeadUserForm())
        .javalin() // get the underlying Javalin instance from UndeadJavalin
        .get("/", ctx -> {
          ctx.result("Hello");
          ctx.contentType("text/html");
        })
        .start(7070);
  }
}

