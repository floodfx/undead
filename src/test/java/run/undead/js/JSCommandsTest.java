package run.undead.js;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JSCommandsTest {

  @Test
  public void testTransition() throws IOException {
    var transition = new Transition();
    assertEquals("[[],[],[]]",
        transition.toJSON());

    assertEquals("[[\"foo\",\"bar\"],[],[]]",
        new Transition("foo bar").toJSON());

    assertEquals("[[\"foo\"],[\"bar\"],[\"baz\"]]",
        new Transition("foo", "bar", "baz").toJSON());

    assertEquals("[[\"foo\"],[\"bar\"],[\"baz\",\"biz\"]]",
        new Transition("foo", "bar", "baz biz").toJSON());
  }

  @Test
  public void testShow() throws IOException {
    var show = new ShowOpts();
    assertEquals("[\"show\",{\"to\":null,\"time\":200,\"transition\":[[],[],[]],\"display\":\"block\"}]",
        show.toJSON());

    show = new ShowOpts("foo");
    assertEquals("[\"show\",{\"to\":\"foo\",\"time\":200,\"transition\":[[],[],[]],\"display\":\"block\"}]",
        show.toJSON());
    show = new ShowOpts("foo", java.time.Duration.ofMillis(100));
    assertEquals("[\"show\",{\"to\":\"foo\",\"time\":100,\"transition\":[[],[],[]],\"display\":\"block\"}]",
        show.toJSON());

    show = new ShowOpts("foo", java.time.Duration.ofMillis(100), new Transition("foo", "bar", "baz"));
    assertEquals("[\"show\",{\"to\":\"foo\",\"time\":100,\"transition\":[[\"foo\"],[\"bar\"],[\"baz\"]],\"display\":\"block\"}]",
        show.toJSON());

    show = new ShowOpts("foo", java.time.Duration.ofMillis(100), new Transition("foo", "bar", "baz"), "inline");
    assertEquals("[\"show\",{\"to\":\"foo\",\"time\":100,\"transition\":[[\"foo\"],[\"bar\"],[\"baz\"]],\"display\":\"inline\"}]",
        show.toJSON());
  }

  @Test
  public void testHide() throws IOException {
    var hide = new HideOpts();
    assertEquals("[\"hide\",{\"to\":null,\"time\":200,\"transition\":[[],[],[]]}]",
        hide.toJSON());

    hide = new HideOpts("foo");
    assertEquals("[\"hide\",{\"to\":\"foo\",\"time\":200,\"transition\":[[],[],[]]}]",
        hide.toJSON());
    hide = new HideOpts("foo", java.time.Duration.ofMillis(100));
    assertEquals("[\"hide\",{\"to\":\"foo\",\"time\":100,\"transition\":[[],[],[]]}]",
        hide.toJSON());

    hide = new HideOpts("foo", java.time.Duration.ofMillis(100), new Transition("foo", "bar", "baz"));
    assertEquals("[\"hide\",{\"to\":\"foo\",\"time\":100,\"transition\":[[\"foo\"],[\"bar\"],[\"baz\"]]}]",
        hide.toJSON());

  }

  @Test
  public void testAddClass() {
    var addClass = new AddClassOpts("foo");
    assertEquals("[\"add_class\",{\"names\":[\"foo\"],\"to\":null,\"time\":200,\"transition\":[[],[],[]]}]",
        addClass.toJSON());

    addClass = new AddClassOpts("foo", "bar");
    assertEquals("[\"add_class\",{\"names\":[\"foo\"],\"to\":\"bar\",\"time\":200,\"transition\":[[],[],[]]}]",
        addClass.toJSON());

    addClass = new AddClassOpts("foo", "bar", java.time.Duration.ofMillis(100));
    assertEquals("[\"add_class\",{\"names\":[\"foo\"],\"to\":\"bar\",\"time\":100,\"transition\":[[],[],[]]}]",
        addClass.toJSON());

    addClass = new AddClassOpts("foo", "bar", java.time.Duration.ofMillis(100), new Transition("foo", "bar", "baz"));
    assertEquals("[\"add_class\",{\"names\":[\"foo\"],\"to\":\"bar\",\"time\":100,\"transition\":[[\"foo\"],[\"bar\"],[\"baz\"]]}]",
        addClass.toJSON());
  }

  @Test
  public void testRemoveClass() {
    var removeClass = new RemoveClassOpts("foo");
    assertEquals("[\"remove_class\",{\"names\":[\"foo\"],\"to\":null,\"time\":200,\"transition\":[[],[],[]]}]",
        removeClass.toJSON());

    removeClass = new RemoveClassOpts("foo", "bar");
    assertEquals("[\"remove_class\",{\"names\":[\"foo\"],\"to\":\"bar\",\"time\":200,\"transition\":[[],[],[]]}]",
        removeClass.toJSON());

    removeClass = new RemoveClassOpts("foo", "bar", java.time.Duration.ofMillis(100));
    assertEquals("[\"remove_class\",{\"names\":[\"foo\"],\"to\":\"bar\",\"time\":100,\"transition\":[[],[],[]]}]",
        removeClass.toJSON());

    removeClass = new RemoveClassOpts("foo", "bar", java.time.Duration.ofMillis(100), new Transition("foo", "bar", "baz"));
    assertEquals("[\"remove_class\",{\"names\":[\"foo\"],\"to\":\"bar\",\"time\":100,\"transition\":[[\"foo\"],[\"bar\"],[\"baz\"]]}]",
        removeClass.toJSON());
  }

  @Test void testToggle() {
    var toggle = new ToggleOpts();
    assertEquals("[\"toggle\",{\"to\":null,\"time\":200,\"ins\":[[],[],[]],\"outs\":[[],[],[]],\"display\":\"block\"}]",
        toggle.toJSON());

    toggle = new ToggleOpts("#foo");
    assertEquals("[\"toggle\",{\"to\":\"#foo\",\"time\":200,\"ins\":[[],[],[]],\"outs\":[[],[],[]],\"display\":\"block\"}]",
        toggle.toJSON());

    toggle = new ToggleOpts("#foo", java.time.Duration.ofMillis(100));
    assertEquals("[\"toggle\",{\"to\":\"#foo\",\"time\":100,\"ins\":[[],[],[]],\"outs\":[[],[],[]],\"display\":\"block\"}]",
        toggle.toJSON());

    toggle = new ToggleOpts("#foo", java.time.Duration.ofMillis(100), new Transition("foo", "bar", "baz"));
    assertEquals("[\"toggle\",{\"to\":\"#foo\",\"time\":100,\"ins\":[[\"foo\"],[\"bar\"],[\"baz\"]],\"outs\":[[],[],[]],\"display\":\"block\"}]",
        toggle.toJSON());

    toggle = new ToggleOpts("#foo", java.time.Duration.ofMillis(100), new Transition("foo", "bar", "baz"), new Transition("foo", "bar", "baz"));
    assertEquals("[\"toggle\",{\"to\":\"#foo\",\"time\":100,\"ins\":[[\"foo\"],[\"bar\"],[\"baz\"]],\"outs\":[[\"foo\"],[\"bar\"],[\"baz\"]],\"display\":\"block\"}]",
        toggle.toJSON());

    toggle = new ToggleOpts("#foo", java.time.Duration.ofMillis(100), new Transition("foo", "bar", "baz"), new Transition("foo", "bar", "baz"), "inline");
    assertEquals("[\"toggle\",{\"to\":\"#foo\",\"time\":100,\"ins\":[[\"foo\"],[\"bar\"],[\"baz\"]],\"outs\":[[\"foo\"],[\"bar\"],[\"baz\"]],\"display\":\"inline\"}]",
        toggle.toJSON());

  }

  @Test
  public void testSetAttr() {
    var setAttr = new SetAttrOpts("foo", "bar");
    assertEquals("[\"set_attr\",{\"to\":null,\"attr\":[\"foo\",\"bar\"]}]",
        setAttr.toJSON());

    setAttr = new SetAttrOpts("foo", "bar", "baz");
    assertEquals("[\"set_attr\",{\"to\":\"baz\",\"attr\":[\"foo\",\"bar\"]}]",
        setAttr.toJSON());
  }

  @Test
  public void testRemoveAttr() {
    var removeAttr = new RemoveAttrOpts("foo");
    assertEquals("[\"remove_attr\",{\"to\":null,\"attr\":\"foo\"}]",
        removeAttr.toJSON());

    removeAttr = new RemoveAttrOpts("foo", "bar");
    assertEquals("[\"remove_attr\",{\"to\":\"bar\",\"attr\":\"foo\"}]",
        removeAttr.toJSON());
  }

  @Test
  public void testTransitionCmd() {
    var tsn = new Transition("foo", "bar", "baz");
    var transition = new TransitionOpts(tsn);
    assertEquals("[\"transition\",{\"to\":null,\"time\":200,\"transition\":[[\"foo\"],[\"bar\"],[\"baz\"]]}]",
        transition.toJSON());

    transition = new TransitionOpts(tsn, "foo");
    assertEquals("[\"transition\",{\"to\":\"foo\",\"time\":200,\"transition\":[[\"foo\"],[\"bar\"],[\"baz\"]]}]",
        transition.toJSON());

    transition = new TransitionOpts(tsn, "foo", java.time.Duration.ofMillis(100));
    assertEquals("[\"transition\",{\"to\":\"foo\",\"time\":100,\"transition\":[[\"foo\"],[\"bar\"],[\"baz\"]]}]",
        transition.toJSON());
  }

  @Test
  public void testPush() {
    var push = new PushOpts("event");
    assertEquals("[\"push\",{\"event\":\"event\"}]",
        push.toJSON());

    push = new PushOpts("event", "#foo");
    assertEquals("[\"push\",{\"event\":\"event\",\"target\":\"#foo\"}]",
        push.toJSON());

    push = new PushOpts("event", "#foo", "#bar");
    assertEquals("[\"push\",{\"event\":\"event\",\"target\":\"#foo\",\"loading\":\"#bar\"}]",
        push.toJSON());

    push = new PushOpts("event", "#foo", "#bar", true);
    assertEquals("[\"push\",{\"event\":\"event\",\"target\":\"#foo\",\"loading\":\"#bar\",\"page_loading\":true}]",
        push.toJSON());

    push = new PushOpts("event", "#foo", "#bar", true, Map.of("foo", "bar"));
    assertEquals("[\"push\",{\"event\":\"event\",\"target\":\"#foo\",\"loading\":\"#bar\",\"page_loading\":true,\"value\":{\"foo\":\"bar\"}}]",
        push.toJSON());

  }

  @Test
  public void testDispatch() {
    var dispatch = new DispatchOpts("event");
    assertEquals("[\"dispatch\",{\"to\":null,\"event\":\"event\"}]",
        dispatch.toJSON());

    dispatch = new DispatchOpts("event", "#foo");
    assertEquals("[\"dispatch\",{\"to\":\"#foo\",\"event\":\"event\"}]",
        dispatch.toJSON());

    dispatch = new DispatchOpts("event", "#foo", Map.of("foo", "bar"));
    assertEquals("[\"dispatch\",{\"to\":\"#foo\",\"event\":\"event\",\"detail\":{\"foo\":\"bar\"}}]",
        dispatch.toJSON());

    dispatch = new DispatchOpts("event", "#foo", Map.of("foo", "bar"), false);
    assertEquals("[\"dispatch\",{\"to\":\"#foo\",\"event\":\"event\",\"detail\":{\"foo\":\"bar\"},\"bubbles\":false}]",
        dispatch.toJSON());
  }

  @Test
  public void testExec() {
    var exec = new ExecOpts("foo");
    assertEquals("[\"exec\",{\"to\":null,\"attr\":\"foo\"}]",
        exec.toJSON());

    exec = new ExecOpts("foo", "#bar");
    assertEquals("[\"exec\",{\"to\":\"#bar\",\"attr\":\"foo\"}]",
        exec.toJSON());
  }

  @Test
  public void testFocus() {
    var focus = new FocusOpts();
    assertEquals("[\"focus\",{\"to\":null}]",
        focus.toJSON());

    focus = new FocusOpts("#foo");
    assertEquals("[\"focus\",{\"to\":\"#foo\"}]",
        focus.toJSON());
  }

  @Test
  public void testFocusFirst() {
    var focusFirst = new FocusFirstOpts();
    assertEquals("[\"focus_first\",{\"to\":null}]",
        focusFirst.toJSON());

    focusFirst = new FocusFirstOpts("#foo");
    assertEquals("[\"focus_first\",{\"to\":\"#foo\"}]",
        focusFirst.toJSON());
  }

  @Test
  public void testNavigate() {
    var nav = new NavigateOpts("/my/path");
    assertEquals("[\"navigate\",{\"href\":\"/my/path\"}]",
        nav.toJSON());

    nav = new NavigateOpts("/my/path", true);
    assertEquals("[\"navigate\",{\"href\":\"/my/path\",\"replace\":true}]",
        nav.toJSON());
  }

  @Test
  public void testPatch() {
    var patch = new PatchOpts("/my/path");
    assertEquals("[\"patch\",{\"href\":\"/my/path\"}]",
        patch.toJSON());

    patch = new PatchOpts("/my/path", true);
    assertEquals("[\"patch\",{\"href\":\"/my/path\",\"replace\":true}]",
        patch.toJSON());
  }

  @Test
  public void testPopFocus() {
    var popFocus = new PopFocusOpts();
    assertEquals("[\"pop_focus\",{}]",
        popFocus.toJSON());
  }

  @Test
  public void testPushFocus() {
    var pushFocus = new PushFocusOpts();
    assertEquals("[\"push_focus\",{\"to\":null}]",
        pushFocus.toJSON());

    pushFocus = new PushFocusOpts("#foo");
    assertEquals("[\"push_focus\",{\"to\":\"#foo\"}]",
        pushFocus.toJSON());
  }


  @Test
  public void testJS() {
    var js = new JS();
    assertEquals("[]", js.toJSON());

    js.show(new ShowOpts());
    assertEquals("[[\"show\",{\"to\":null,\"time\":200,\"transition\":[[],[],[]],\"display\":\"block\"}]]", js.toJSON());

    js = new JS();
    js.hide(new HideOpts("foo"));
    assertEquals("[[\"hide\",{\"to\":\"foo\",\"time\":200,\"transition\":[[],[],[]]}]]", js.toJSON());

    js = new JS();
    js.hide(new HideOpts());
    assertEquals("[[\"hide\",{\"to\":null,\"time\":200,\"transition\":[[],[],[]]}]]", js.toJSON());

    js = new JS();
    js.addClass(new AddClassOpts("foo"));
    assertEquals("[[\"add_class\",{\"names\":[\"foo\"],\"to\":null,\"time\":200,\"transition\":[[],[],[]]}]]", js.toJSON());

    js = new JS();
    js.removeClass(new RemoveClassOpts("foo"));
    assertEquals("[[\"remove_class\",{\"names\":[\"foo\"],\"to\":null,\"time\":200,\"transition\":[[],[],[]]}]]", js.toJSON());

    js = new JS();
    js.toggle(new ToggleOpts());
    assertEquals("[[\"toggle\",{\"to\":null,\"time\":200,\"ins\":[[],[],[]],\"outs\":[[],[],[]],\"display\":\"block\"}]]", js.toJSON());

    js = new JS();
    js.setAttr(new SetAttrOpts("foo", "bar"));
    assertEquals("[[\"set_attr\",{\"to\":null,\"attr\":[\"foo\",\"bar\"]}]]", js.toJSON());

    js = new JS();
    js.removeAttr(new RemoveAttrOpts("foo"));
    assertEquals("[[\"remove_attr\",{\"to\":null,\"attr\":\"foo\"}]]", js.toJSON());

    js = new JS();
    js.transition(new TransitionOpts(new Transition("foo", "bar", "baz")));
    assertEquals("[[\"transition\",{\"to\":null,\"time\":200,\"transition\":[[\"foo\"],[\"bar\"],[\"baz\"]]}]]", js.toJSON());

    js = new JS();
    js.push(new PushOpts("event"));
    assertEquals("[[\"push\",{\"event\":\"event\"}]]", js.toJSON());

    js = new JS();
    js.dispatch(new DispatchOpts("event"));
    assertEquals("[[\"dispatch\",{\"to\":null,\"event\":\"event\"}]]", js.toJSON());

    js = new JS();
    js.exec(new ExecOpts("foo"));
    assertEquals("[[\"exec\",{\"to\":null,\"attr\":\"foo\"}]]", js.toJSON());

    js = new JS();
    js.focus();
    assertEquals("[[\"focus\",{\"to\":null}]]", js.toJSON());

    js = new JS();
    js.focusFirst(new FocusFirstOpts());
    assertEquals("[[\"focus_first\",{\"to\":null}]]", js.toJSON());

    js = new JS();
    js.navigate(new NavigateOpts("/my/path"));
    assertEquals("[[\"navigate\",{\"href\":\"/my/path\"}]]", js.toJSON());

    js = new JS();
    js.patch(new PatchOpts("/my/path"));
    assertEquals("[[\"patch\",{\"href\":\"/my/path\"}]]", js.toJSON());

    js = new JS();
    js.popFocus();
    assertEquals("[[\"pop_focus\",{}]]", js.toJSON());

    js = new JS();
    js.pushFocus(new PushFocusOpts());
    assertEquals("[[\"push_focus\",{\"to\":null}]]", js.toJSON());
  }
}
