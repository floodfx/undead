package com.undead4j.socket;

public interface Socket<Context> {
  /**
   * The id of the `LiveView` this socket is associated with
   */
   public String id();
  /**
   * Whether the websocket is connected.
   * true if connected to a websocket, false for http request
   */
  public Boolean connected();
  /**
   * The current context (i.e. state) of the `LiveView`
   */
  public Context context();
  /**
   * The current URL of the `LiveView`
   */
  public String url();
  /**
   * `assign` is used to update the context (i.e. state) of the `LiveComponent`
   * @param context a `Partial` of the LiveView's context to update
   */
  public void assign(Context ctx);
  /**
   * Marks any set properties as temporary and will be reset to the given
   * value after the next render cycle.  Typically used to ensure large but
   * infrequently updated values are not kept in memory.
   *
   * @param context a partial of the context that should be temporary and the value to reset it to
   */
  public void tempAssign(Context ctx);
  /**
   * Updates the `<title>` tag of the `LiveView` page.  Requires using the
   * `live_title` helper in rendering the page.
   *
   * @param newPageTitle the new text value of the page - note the prefix and suffix will not be changed
   */
  public void pageTitle(String newTitle);
  /**
   * Pushes and event (possibly with data) from the server to the client.  Requires
   * either a `window.addEventListener` defined for that event or a client `Hook`
   * to be defined and to be listening for the event via `this.handleEvent` callback.
   *
   * @param pushEvent the event to push to the client
   */
  public void pushEvent(Object event);
  /**
   * Updates the LiveView's browser URL with the given path and query parameters.
   *
   * @param path the path whose query params are being updated
   * @param params the query params to update the path with
   * @param replaceHistory whether to replace the current history entry or push a new one (defaults to false)
   */
//  public void pushPatch(String path, Map params, Boolean replaceHistory);
//  public default void pushPatch(String path, Map params) {
//    this.pushPatch(path, params, null);
//  }
//  public default void pushPatch(String path) {
//    this.pushPatch(path, null, null);
//  }
  /**
   * Shutdowns the current `LiveView`and loads another `LiveView`in its place
   * without reloading the whole page (i.e. making a full HTTP request).  Can be
   * used to remount the current `LiveView`if need be. Use `pushPatch` to update the
   * current `LiveView`without unloading and remounting.
   *
   * @param path the path whose query params are being updated
   * @param params the query params to update the path with
   * @param replaceHistory whether to replace the current history entry or push a new one (defaults to false)
   */
//  public void pushRedirect(String path, Map params, Boolean replaceHistory);
//  public default void pushRedirect(String path, Map params) {
//    this.pushPatch(path, params, null);
//  }
//  public default void pushRedirect(String path) {
//    this.pushPatch(path, null, null);
//  }
  /**
   * Add flash to the socket for a given key and value.
   * @param key the key to add the flash to
   * @param value the flash value
   */
//  public void putFlash(String key, String value);
  /**
   * Send an internal event (a.k.a "Info") to the LiveView's `handleInfo` method
   *
   * @param event the event to send to `handleInfo`
   */
//  public void sendInfo(Object info);
  /**
   * Subscribe to the given topic using pub/sub. Events published to this topic
   * will be delivered to `handleInfo`.
   *
   * @param topic the topic to subscribe this `LiveView`to
   */
//  public void subscribe(String topic);
  /**
   * Allows file uploads for the given `LiveView`and configures the upload
   * options (filetypes, size, etc).
   * @param name the name of the upload
   * @param options the options for the upload (optional)
   */
//  public void allowUpload(String name, UploadConfigOptions options);
  /**
   * Cancels the file upload for a given UploadConfig by config name and file ref.
   * @param name the name of the upload from which to cancel
   * @param ref the ref of the upload entry to cancel
   */
//  public void cancelUpload(String configName, String ref);

  /**
   * Consume the uploaded files for a given UploadConfig (by name). This
   * should only be called after the form's "save" event has occurred which
   * guarantees all the files for the upload have been fully uploaded.
   * @param name the name of the upload from which to consume
   * @param fn the callback to run for each entry
   * @returns an array of promises based on the return type of the callback function
   * @throws if any of the entries are not fully uploaded (i.e. completed)
   */
//
//  consumeUploadedEntries<T>(
//  configName: string,
//  fn: (meta: ConsumeUploadedEntriesMeta, entry: UploadEntry) => Promise<T>
//  ): Promise<T[]>;
  /**
   * Returns two sets of files that are being uploaded, those `completed` and
   * those `inProgress` for a given UploadConfig (by name).  Unlike `consumeUploadedEntries`,
   * this does not require the form's "save" event to have occurred and will not
   * throw if any of the entries are not fully uploaded.
   * @param name the name of the upload from which to get the entries
   * @returns an object with `completed` and `inProgress` entries
   */
//  uploadedEntries(configName: string): Promise<{
//    completed: UploadEntry[];
//    inProgress: UploadEntry[];
//  }>;
}
