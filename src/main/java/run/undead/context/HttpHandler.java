package run.undead.context;

import run.undead.handle.http.RequestAdaptor;
import run.undead.template.Directive;
import run.undead.template.MainLayout;
import run.undead.template.PageTitle;
import run.undead.template.WrapperTemplate;
import run.undead.view.Meta;
import run.undead.view.View;
import okhttp3.HttpUrl;

import java.util.Map;
import java.util.UUID;

/**
 * HttpHandler handles the HTTP request lifecycle for a {@link View} and either
 * returns the rendered HTML or redirects the request.
 */
public class HttpHandler {

/**
   * Handle the HTTP request lifecycle for a {@link View} and either
   * return the rendered HTML or redirect the request.
   * @param view the {@link View} to render
   * @param mainLayout the {@link MainLayout} to render the {@link View} inside
   * @param adaptor the {@link RequestAdaptor} to pull data from the HTTP request
   * @param pageTitle the {@link PageTitle} to pass to the {@link MainLayout}
   * @param wrapperTemplate the optional {@link WrapperTemplate} to render the {@link View} inside
   * @return the rendered HTML or null if the request was redirected
   */
  static public String handle(
      View view,
      MainLayout mainLayout,
      RequestAdaptor adaptor,
      PageTitle pageTitle,
      WrapperTemplate wrapperTemplate
  ) {

    // new viewId for each request
    var viewId = UUID.randomUUID().toString();

    // extract csrf token from session data or generate it if it doesn't exist
    var sessionData = adaptor.sessionData();
    var csrfToken = (String) sessionData.get("_csrf_token");
    if (csrfToken == null) {
      csrfToken = UUID.randomUUID().toString();
      sessionData.put("_csrf_token", csrfToken);
    }

    var ctx = new HttpContext(viewId, adaptor.url());

    // execute the `LiveView`'s `mount` function, passing in the data from the HTTP request
    var params = Map.of(
        "_csrf_token", csrfToken,
        "_mounts", -1
        // TODO path params
    );

    // Step 1: call mount
    view.mount(ctx, sessionData, params);//socket, sessionData, params

    // handle redirects in mount
    if (ctx.redirect != null) {
      adaptor.willRedirect(ctx.redirect);
      return null;
    }

    // Step 2: call handleParams
    var url = HttpUrl.parse(adaptor.url());
    view.handleParams(ctx, url.uri(), params);

    // handle redirects in handleParams
    if (ctx.redirect != null) {
      adaptor.willRedirect(ctx.redirect);
      return null;
    }

    // Step 3: call render
    var meta = new Meta();
    // TODO implement Components
    var tmpl = view.render(meta);

    // TODO implement serialization
    var serializedSession = "";//await serDe.serialize({ ...sessionData });

    // TODO implement tracking of statics
    var serializedStatics = "";//serDe.serialize({ ...view.statics });

    // check if there is a WrapperTemplate and render the View inside it
    var content = tmpl;
    if (wrapperTemplate != null) {
      content = wrapperTemplate.render(sessionData, content);
    }

    // render the root container of the View
    var rootContent = Directive.HTML. """
      <div
      data-phx-main="true"
      data-phx-session="\{ serializedSession }"
      data-phx-static="\{ serializedStatics }"
      id="ud-\{ viewId }">
          \{ Directive.NoEscape(content) }
      </div>
    """ ;

    // render the main layout with the View inside
    var pageTmpl = mainLayout.render(pageTitle, csrfToken, rootContent);

    // serialize the View to HTML
    return pageTmpl.toString();
  }
}
