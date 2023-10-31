package run.undead.view;

import run.undead.event.UndeadEvent;
import run.undead.event.UndeadInfo;
import run.undead.context.Context;
import run.undead.template.UndeadTemplate;

import java.net.URI;
import java.util.Map;

/**
 * <p>
 * The View interface defines the lifecycle callbacks for an Undead View.  Views have a lifecycle that
 * consists a short, fast HTTP request/response and a long-lived WebSocket connection.
 * <strong>Implementation Note:</strong> Views should have a no-arg constructor as they are instantiated by reflection.
 * </p>
 *
 * <h3>Interface methods</h3>
 * <p>
 * <ul>
 *   <li>{@link View#mount} - (optional) mount is where a View can be initialized based on session data and/or path or query parameters</li>
 *   <li>{@link View#handleParams} - (optional) handleParams is called after mount and whenever there is a URI change</li>
 *   <li>{@link View#handleEvent} - (optional) handleEvent is called when an event (click, keypress, etc) is received from the browser</li>
 *   <li>{@link View#handleInfo} - (optional) handleInfo is called when an event (a.k.a info) is received from the server</li>
 *   <li>{@link View#render} - (required) render returns an {@link UndeadTemplate} based on the state of the View</li>
 *   <li>{@link View#shutdown} - (optional) shutdown is called when the View is shutting down and is a good place to clean up</li>
 * </ul>
 * As you can see the only required method is {@link View#render} which is called to render the View based on its state.
 * The other methods are optional so you can implement only the callbacks that you need for your View.
 * </p>
 *
 * <h3>View Lifecycle</h3>
 * <h4>Phase 1: HTTP Request/Response</h4>
 * <p>
 *   Views are HTTP GET routes on a web server (e.g. <code>GET /my-view?foo=bar</code>) and when a user navigates to a URL
 *   that is handled by an Undead View, the server runs the View's lifecycle callbacks and renders the View
 *   as HTML.
 *</p>
 * <p>
 *   During this HTTP phase, Views can't handle events or internal messages so only the following lifecycle
 *   methods are called (in order):
 *   <pre>{@link View#mount} => {@link View#handleParams} => {@link View#render}</pre>
 *</p>
 * <p>
 *   First, {@link View#mount} is called and is passed a {@link Context}, sessionData, and parameters
 *   (both path and query) from the HTTP request.  This is where you should initialize any state for the View,
 *   perhaps, based on sessionData, params or both. It is reasonable for authz and authn logic to take
 *   place in mount as well.  See {@link Context} for more information on the Socket.
 *</p>
 * <p>
 *   Next, {@link View#handleParams} is called and is passed the {@link Context}, the {@link URI} of the
 *   View (based on the path), and the parameters.  This is called after {@link View#mount} and whenever there is a live patch
 *   event.  You can use this callback to update the state of the View based on the parameters and is useful
 *   if there are live patch events that update the URI of the View. See {@link Context} for more information
 *<p>
 *   Finally, {@link View#render} is called and is passed a {@link Meta} object.  Render should use the state
 *   of the View return an HTML {@link UndeadTemplate} for the View.  See {@link Meta} for more information.
 *</p>
 *
 * <h4>Phase 2: WebSocket Connection</h4>
 * <p>
 *   After the HTTP request/response phase, the client loads the HTML that we sent back including the Undead
 *   javascript code which connects back to the server via a WebSocket.  Once the WebSocket is connected,
 *   the same initial lifecycle callbacks are called again (in order): mount => handleParams => render but
 *   instead of rendering HTML, we send back a data structure that allows efficient patching of the DOM.
 *   (Note: this is transparent to the developer as the same {@link View#render} method is called).
 * </p>
 * <p>
 *   Now that the View has established a long-running WebSocket connection, it can handle events from the browser
 *   or server events.  To receive browser events, the {@link UndeadTemplate} returned from {@link View#render} uses
 *   the <code>ud-</code> attributes to register event handlers.  For example, <code>ud-click="my-event"</code> added
 *   to a button will send a <code>my-event</code> event to the server when the button is clicked.  Undead will route
 *   the event to the View's {@link View#handleEvent} callback.  See {@link UndeadTemplate} for more information on
 *   the <code>ud-</code> attributes.
 * </p>
 * <p>
 *   Server events are any events that are not triggered by the browser but rather messages that come from a pub/sub
 *   topic or the same View instance.  For example, a View could have a timer that fires periodically sending an
 *   internal message to the client via the {@link Context#sendInfo} method. Regardless of the source of the event,
 *   the View will receive the event via the {@link View#handleInfo} callback.
 * </p>
 * <p>
 *   To review, events from the browser are received via {@link View#handleEvent} and server messages are received
 *   via {@link View#handleInfo}.  In both cases, the View may update its state which will cause {@link View#render}
 *   to be called again and for the diffs to be sent back to the client and applied to the DOM.
 * </p>
 * <p>
 *   Finally, when the WebSocket connection is closed, the {@link View#shutdown} callback is called so the View
 *   can clean up any resources, timers, connections, etc.
 * </p>
 */
public interface View {

  /**
   * mount is called once for both the HTTP request/response and the WebSocket connection phase and is
   * typically used to initialize the state of the View based on the sessionData and params.
   * @see Context
   * @param context the {@link Context} for the View
   * @param sessionData a Map of session data from the HTTP request
   * @param params a Map of parameters (both path and query) from the HTTP request
   */
  default void mount(Context context, Map sessionData, Map params) {
    // by default mount does nothing which is ok
  }

  /**
   * handleParams is called once after mount and whenever there is a URI change.  You can use this callback
   * to update the state of the View based on the parameters and is useful if there are events that update
   * the URI of the View (e.g. add / update / remove query parameters).
   * @param context the {@link Context} for the View
   * @param uri the {@link URI} of the View
   * @param params a Map of parameters (both path and query)
   */
  default void handleParams(Context context, URI uri, Map params) {
    // by default handleParams does nothing which is ok
  }

  /**
   * handleEvent is called when an event (click, keypress, etc) is received from the browser from an element
   * that has a <code>ud-</code> attribute.  For example, <code>ud-click="my-event"</code> added to a button
   * will send a <code>my-event</code> event to handleEvent when the button is clicked.  When handleEvent is
   * called, the View may update its state which will cause {@link View#render} to be called again and for
   * the diffs to be sent back to the client and applied to the DOM.  <strong>Note:</strong> the default
   * implementation throws a {@link RuntimeException} so you must implement this method in your View if it will
   * receive {@link UndeadEvent}.
   * @see UndeadTemplate
   * @param context the {@link Context} for the View
   * @param event the {@link UndeadEvent} with the event type and data
   */
  default void handleEvent(Context context, UndeadEvent event) {
    // if we get an event, tell the developer they need to implement this
    throw new RuntimeException("Implement handleEvent in your view");
  }

  /**
   * handleInfo is called when an event (a.k.a info) is received from the server.  For example, a View could
   * have a timer that fires periodically sending an internal message to the client via the {@link Context#sendInfo}
   * method.  When handleInfo is called, the View may update its state which will cause {@link View#render} to
   * be called again and for the diffs to be sent back to the client and applied to the DOM.  <strong>Note:</strong>
   * the default implementation throws a {@link RuntimeException} so you must implement this method in your View if
   * it will receive {@link UndeadInfo}.
   * @see Context
   * @param context the {@link Context} for the View
   * @param info the {@link UndeadInfo} with the info type and data
   */
  default void handleInfo(Context context, UndeadInfo info) {
    // if we get an info, tell the developer they need to implement this
    throw new RuntimeException("Implement handleInfo in your view");
  }

  /**
   * render returns an {@link UndeadTemplate} based on the state of the View.  This method is called
   * during both the HTTP request/response phase, the WebSocket connection phase, and after any event
   * is received by the View.  The {@link Meta} object is passed to the View and provides additional
   * metadata and helper methods for rendering the View.
   * @see UndeadTemplate
   * @see Meta
   * @param meta the {@link Meta} object for the View
   * @return an {@link UndeadTemplate} based on the state of the View
   */
  UndeadTemplate render(Meta meta);

  /**
   * shutdown is called when the View is shutting down and is a good place to clean up any resources,
   * timers, connections, etc.
   */
  default void shutdown() {
    // empty implementation is ok
  }
}

