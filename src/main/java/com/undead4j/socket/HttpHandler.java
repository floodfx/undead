package com.undead4j.socket;

import com.undead4j.handle.http.RequestAdaptor;
import com.undead4j.template.Live;
import com.undead4j.template.PageTemplate;
import com.undead4j.template.PageTitleConfig;
import com.undead4j.template.WrapperTemplate;
import com.undead4j.view.Meta;
import com.undead4j.view.View;

import java.util.Map;
import java.util.UUID;

public class HttpHandler {

  public String handle(
      View view,
      PageTemplate pageTemplate,
      RequestAdaptor adaptor,
      PageTitleConfig pageTitleConfig,
      WrapperTemplate wrapperTemplate
  ) {

    // new LiveViewId for each request
    var liveViewId = UUID.randomUUID().toString();

    // extract csrf token from session data or generate it if it doesn't exist
    var sessionData = adaptor.sessionData();
    var csrfToken = (String)sessionData.get("_csrf_token");
    if (csrfToken == null) {
      csrfToken = UUID.randomUUID().toString();
      sessionData.put("_csrf_token", csrfToken);
    }
    // prepare a http socket for the `LiveView` render lifecycle: mount => handleParams => render
    var socket = new HttpSocket(liveViewId, adaptor.url());

    // execute the `LiveView`'s `mount` function, passing in the data from the HTTP request
    var params = Map.of(
        "_csrf_token", csrfToken,
        "_mounts", -1
        // TODO path params
    );
    view.mount(socket, sessionData, params);//socket, sessionData, params
//    await liveView.mount(
//        liveViewSocket,
//        { ...sessionData },
//    { _csrf_token: sessionData._csrf_token, _mounts: -1, ...pathParams }
//  );

    // check for redirects in `mount`
    if (socket.redirect() != null) {
      adaptor.willRedirect(socket.redirect());
      return null;
    }

    // execute the `LiveView`'s `handleParams` function, passing in the data from the HTTP request
    view.handleParams(socket);// url, socket

    // check for redirects in `handleParams`
    if (socket.redirect() != null) {
      adaptor.willRedirect(socket.redirect());
      return null;
    }

    // now render the `LiveView` including running the lifecycle of any `LiveComponent`s it contains
    var myself = 1;// counter for live_component calls
    var meta = new Meta();
    var tmpl = view.render(meta);
//  const view = await liveView.render(liveViewSocket.context, {
//        csrfToken: sessionData.csrfToken,
//        async live_component(
//        liveComponent: LiveComponent,
//        params?: Partial<unknown & { id: string | number }>
//    ): Promise<LiveViewTemplate> {
      // params may be empty if the `LiveComponent` doesn't have any params
//      params = params ?? {};
//      delete params.id; // remove id before passing to socket
//
//      // prepare a http socket for the `LiveComponent` render lifecycle: mount => update => render
//      const lcSocket = new HttpLiveComponentSocket(liveViewId, params);
//
//      // pass params provided in `LiveView.render` to the `LiveComponent` socket
//      lcSocket.assign(params);
//
//      // start the `LiveComponent` lifecycle
//      await liveComponent.mount(lcSocket);
//      await liveComponent.update(lcSocket);
//
//      // render view with context
//      const newView = await liveComponent.render(lcSocket.context, { myself: myself });
//      myself++;
//      // return the view to the parent `LiveView` to be rendered
//      return newView;
//    },
//    url,
//        uploads: liveViewSocket.uploadConfigs,
//  });
    // now that we've rendered the `LiveView` and its `LiveComponent`s, we can serialize the session data
    // to be passed into the websocket connection

  var serializedSession = "";//await serDe.serialize({ ...sessionData });

    // TODO implement tracking of statics
     var serializedStatics = "";//serDe.serialize({ ...view.statics });
//  const serializedStatics = "";

    // optionally render the `LiveView` inside another template passing the session data
    // and the rendered `LiveView` to the template renderer
    var content = Live.NO_ESC(tmpl);
    if (wrapperTemplate != null) {
      content = Live.NO_ESC(wrapperTemplate.render(sessionData, content));
    }

    // wrap `LiveView` content inside the `phx-main` template along with the serialized
    // session data and the generated live view ID for the websocket connection
  var rootContent = Live.HTML."""
    <div
    data-phx-main="true"
    data-phx-session="\{serializedSession}"
    data-phx-static="\{serializedStatics}"
    id="phx-\{liveViewId}">
        \{content}
    </div>
  """;

    // finally render the `LiveView` root template passing any pageTitle data, the CSRF token,  and the rendered `LiveView`
    var pageTmpl = pageTemplate.render(pageTitleConfig, csrfToken, rootContent);
    return pageTmpl.toString();
  }
}
