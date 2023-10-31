package run.undead.context;

import run.undead.event.UndeadEvent;
import run.undead.event.UndeadInfo;
import run.undead.view.View;
import run.undead.template.MainLayout;

/**
 * Context abstracts the underlying transport mechanism (i.e. HTTP or WebSocket) for a
 * {@link View} and provides common functionality and metadata to extend the functionality
 * of the {@link View} instance.
 */
public interface Context {
  // TODO:
  // support tempAssign (perhaps a @TempAssign annotation on a property?)
  // support pushPatch, pushRedirect,
  // how do we inject services into this object?
  // subscribe side of pub/sub
  // uploads: allowUpload, cancelUpload, consumeUploadedEntries, uploadedEntries

  /**
   * id is the unique id of the Undead {@link View} instance
   */
  public String id();

  /**
   * connected is true if connected to a websocket, false for http request
   */
  default public Boolean connected() {
    return false;
  }

  /**
   * url is the URL for this {@link View}
   */
  public String url();

  /**
   * pageTitle updates the `<title>` tag of the {@link View} page.  Requires using the
   * {@link MainLayout#liveTitle} helper in rendering the page.
   */
  default public void pageTitle(String newTitle) {
    // noop by default
  }

  /**
   * pushEvent pushes an event to the client.  Requires either the client javascript
   * to have a {@code window.addEventListener} defined for that event or a client
   * {@code Hook} to be defined and to be listening for the event via {@code this.handleEvent} callback.
   */
  default public void pushEvent(UndeadEvent event){
    // noop by default
  }

  /**
   * sendInfo sends an internal server message to this {@link View} instance.  The
   * {@link View} must implement the {@link View#handleInfo(Context, UndeadInfo)} callback
   * to handle the info message.
   */
  default public void sendInfo(UndeadInfo info) {
    // noop by default
  }

  public void redirect(String url);
}
