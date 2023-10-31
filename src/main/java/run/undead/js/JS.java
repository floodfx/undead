package run.undead.js;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import run.undead.template.UndeadTemplate;
import run.undead.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * JS is a helper class for building JS commands that are rendered in {@link UndeadTemplate}s.
 * JS provides a number of methods that make it easier to manipulate the DOM, dispatch client-side events,
 * and push events (and data) to the server.
 *
 * Here is an example of using JS to toggle the visibility of an element:
 * <pre>{@code
 *    // in your {@link View#render} method ...
 *    public UndeadTemplate render() {
 *      return Undead.HTML."""
 *        <div id="my-div" class="hidden">Hello World!</div>
 *        <button ud-click="\{ JS.toggle("#my-div") }">Toggle</button>
 *      """ ;
 *    }
 * }</pre>
 *
 * JS commands are "chainable" so you can build up a list of commands to execute in order.
 * For example, the following hides an element and pushes an event to the server:
 * <pre>{@code
 *   // in your {@link View#render} method ...
 *   return Undead.HTML."""
 *    <button ud-click="\{ JS.hide("#my-div").push("my-event") }">Hide</button>
 *   """ ;
 * }</pre>
 *
 */
public class JS {

  protected final static String DEFAULT_DISPLAY = "block";

  protected final static Moshi moshi = new Moshi.Builder()
      .add(new TransitionAdapter())
      .add(new ShowCmdAdapter())
      .add(new HideCmdAdapter())
      .add(new AddClassCmdAdapter())
      .add(new RemoveClassCmdAdapter())
      .add(new ToggleCmdAdapter())
      .add(new SetAttrCmdAdapter())
      .add(new RemoveAttrCmdAdapter())
      .add(new TransitionCmdAdapter())
      .add(new PushCmdAdapter())
      .add(new DispatchCmdAdapter())
      .add(new ExecCmdAdaptor())
      .add(new FocusCmdAdaptor())
      .add(new FocusFirstCmdAdaptor())
      .add(new NavigateCmdAdaptor())
      .add(new PatchCmdAdaptor())
      .add(new PopFocusCmdAdaptor())
      .add(new PushFocusCmdAdaptor())
      .build();
  protected final static JsonAdapter<List> listAdaptor = moshi.adapter(List.class).serializeNulls();

  private final List<Cmd> cmds;

  public JS () {
    this.cmds = new ArrayList<>();
  }


  /**
   * `toJSON` returns the JSON String representation of the JS commands.
   * @return
   */
  public String toJSON() {
    return listAdaptor.toJson(cmds);
  }


  /**
   * addClass adds css classes to DOM elements
   * @param addClassOpts options for the add_class command
   *                     @see AddClassOpts
   * @return the JS instance for chaining
   */
  public JS addClass(AddClassOpts addClassOpts) {
    this.cmds.add(addClassOpts);
    return this;
  }

  /**
   * addClass adds css classes to DOM elements
   * @param classNames the css class names to add (space separated)
   * @return the JS instance for chaining
   */
  public JS addClass(String classNames) {
    this.cmds.add(new AddClassOpts(classNames));
    return this;
  }

  /**
   * dispatch dispatches an event to the DOM
   * @param dispatchOpts options for the dispatch command
   *                     @see DispatchOpts
   * @return the JS instance for chaining
   */
  public JS dispatch(DispatchOpts dispatchOpts) {
    this.cmds.add(dispatchOpts);
    return this;
  }

  /**
   * dispatch dispatches an event to the DOM
   * @param event the name of the event to dispatch
   * @return the JS instance for chaining
   */
  public JS dispatch(String event) {
    this.cmds.add(new DispatchOpts(event));
    return this;
  }

  /**
   * exec executes JS commands located in element attributes
   * @param execOpts options for the exec command
   *                 @see ExecOpts
   * @return the JS instance for chaining
   */
  public JS exec(ExecOpts execOpts) {
    this.cmds.add(execOpts);
    return this;
  }

  /**
   * exec executes JS commands located in element attributes
   * @param attr the name of the attribute that contains the JS commands
   * @return the JS instance for chaining
   */
  public JS exec(String attr) {
    this.cmds.add(new ExecOpts(attr));
    return this;
  }

  /**
   * focusFirst sends focus to the first focusable child in selector
   * @param focusFirstOpts options for the focus_first command
   *                       @see FocusFirstOpts
   * @return the JS instance for chaining
   */
  public JS focusFirst(FocusFirstOpts focusFirstOpts) {
    this.cmds.add(focusFirstOpts);
    return this;
  }

  /**
   * focusFirst sends focus to the first focusable child in selector
   * @return the JS instance for chaining
   */
  public JS focusFirst() {
    this.cmds.add(new FocusFirstOpts());
    return this;
  }

  /**
   * focus sends focus to a selector
   * @return the JS instance for chaining
   */
  public JS focus() {
    this.cmds.add(new FocusOpts());
    return this;
  }

  /**
   * focus sends focus to a selector
   * @param to the DOM selector to send focus to
   * @return the JS instance for chaining
   */
  public JS focus(String to) {
    this.cmds.add(new FocusOpts(to));
    return this;
  }

  /**
   * hide makes DOM elements invisible
   * @param hideOpts options for the hide command
   *                 @see HideOpts
   * @return the JS instance for chaining
   */
  public JS hide(HideOpts hideOpts) {
    this.cmds.add(hideOpts);
    return this;
  }

  /**
   * hide makes DOM elements invisible
   * @return the JS instance for chaining
   */
  public JS hide() {
    this.cmds.add(new HideOpts());
    return this;
  }

  /**
   * navigate sends a navigation event to the server and updates the
   * browser's pushState history.
   * @param navigateOpts options for the navigate command
   *                     @see NavigateOpts
   * @return the JS instance for chaining
   */
  public JS navigate(NavigateOpts navigateOpts) {
    this.cmds.add(navigateOpts);
    return this;
  }

  /**
   * navigate sends a navigation event to the server and updates the
   * browser's pushState history.
   * @param href the href to navigate to
   * @return the JS instance for chaining
   */
  public JS navigate(String href) {
    this.cmds.add(new NavigateOpts(href));
    return this;
  }

  /**
   * patch sends a patch event to the server
   * @param patchOpts options for the patch command
   *                  @see PatchOpts
   * @return the JS instance for chaining
   */
  public JS patch(PatchOpts patchOpts) {
    this.cmds.add(patchOpts);
    return this;
  }

  /**
   * patch sends a patch event to the server
   * @param href the href to patch
   * @return the JS instance for chaining
   */
  public JS patch(String href) {
    this.cmds.add(new PatchOpts(href));
    return this;
  }

  /**
   * popFocus focuses the last pushed element
   * @return the JS instance for chaining
   */
  public JS popFocus() {
    this.cmds.add(new PopFocusOpts());
    return this;
  }


  /**
   * pushFocus pushes focus from the source element to be later popped
   * @param pushFocusOpts options for the push_focus command
   *                      @see PushFocusOpts
   * @return the JS instance for chaining
   */
  public JS pushFocus(PushFocusOpts pushFocusOpts) {
    this.cmds.add(pushFocusOpts);
    return this;
  }

  /**
   * pushFocus pushes focus from the source element to be later popped
   * @param to the DOM selector to push focus to
   * @return the JS instance for chaining
   */
  public JS pushFocus(String to) {
    this.cmds.add(new PushFocusOpts(to));
    return this;
  }

  /**
   * push sends an event to the server
   * @param pushOpts options for the push command
   *                 @see PushOpts
   * @return the JS instance for chaining
   */
  public JS push(PushOpts pushOpts) {
    this.cmds.add(pushOpts);
    return this;
  }

  /**
   * push sends an event to the server
   * @param event the name of the event to push
   * @return the JS instance for chaining
   */
  public JS push(String event) {
    this.cmds.add(new PushOpts(event));
    return this;
  }

  /**
   * removeAttr removes an attribute from a DOM element
   * @param removeAttrOpts options for the remove_attr command
   *                       @see RemoveAttrOpts
   * @return the JS instance for chaining
   */
  public JS removeAttr(RemoveAttrOpts removeAttrOpts) {
    this.cmds.add(removeAttrOpts);
    return this;
  }

  /**
   * removeAttr removes an attribute from a DOM element
   * @param name the name of the attribute to remove
   * @return the JS instance for chaining
   */
  public JS removeAttr(String name) {
    this.cmds.add(new RemoveAttrOpts(name));
    return this;
  }

  /**
   * removeClass removes css classes from DOM elements
   * @param removeClassOpts options for the remove_class command
   *                        @see RemoveClassOpts
   * @return the JS instance for chaining
   */
  public JS removeClass(RemoveClassOpts removeClassOpts) {
    this.cmds.add(removeClassOpts);
    return this;
  }

  /**
   * removeClass removes css classes from DOM elements
   * @param classNames the css class names to remove (space separated)
   * @return the JS instance for chaining
   */
  public JS removeClass(String classNames) {
    this.cmds.add(new RemoveClassOpts(classNames));
    return this;
  }

  /**
   * setAttr sets an attribute on a DOM element
   * @param setAttrOpts options for the set_attr command
   *                    @see SetAttrOpts
   * @return the JS instance for chaining
   */
  public JS setAttr(SetAttrOpts setAttrOpts) {
    this.cmds.add(setAttrOpts);
    return this;
  }

  /**
   * setAttr sets an attribute on a DOM element
   * @param name the name of the attribute to set
   * @param value the value of the attribute to set
   * @return the JS instance for chaining
   */
  public JS setAttr(String name, String value) {
    this.cmds.add(new SetAttrOpts(name, value));
    return this;
  }

  /**
   * show makes DOM elements visible
   * @param showOpts options for the show command
   *                 @see ShowOpts
   * @return the JS instance for chaining
   */
  public JS show(ShowOpts showOpts) {
    this.cmds.add(showOpts);
    return this;
  }

  /**
   * show makes DOM elements visible
   * @return the JS instance for chaining
   */
  public JS show() {
    this.cmds.add(new ShowOpts());
    return this;
  }

  /**
   * toggle toggles the visibility of DOM elements
   * @param toggleOpts options for the toggle command
   *                   @see ToggleOpts
   * @return the JS instance for chaining
   */
  public JS toggle(ToggleOpts toggleOpts) {
    this.cmds.add(toggleOpts);
    return this;
  }

  /**
   * toggle toggles the visibility of DOM elements
   * @return the JS instance for chaining
   */
  public JS toggle() {
    this.cmds.add(new ToggleOpts());
    return this;
  }

  /**
   * transition applies a css transition to a DOM element
   * @param transitionOpts options for the transition command
   *                       @see TransitionOpts
   * @return the JS instance for chaining
   */
  public JS transition(TransitionOpts transitionOpts) {
    this.cmds.add(transitionOpts);
    return this;
  }

}
