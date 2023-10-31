package run.undead.js;

import com.squareup.moshi.ToJson;

import java.util.List;

/**
 * Transition represents the css transition classes to apply to an element.
 * Transitions are useful for temporarily adding an animation class to element(s),
 * such as for highlighting content changes.
 */
class Transition {
  protected List<String[]> data;

  Transition() {
    data = List.of(
        new String[]{},
        new String[]{},
        new String[]{}
    );
  }

  /**
   * the transition css classes to apply before removing classes
   * @param cssClasses the transition css classes to apply for the transition
   */
  public Transition(String cssClasses) {
    data = List.of(
        cssClasses.split("\s+"),
        new String[]{},
        new String[]{}
    );
  }

  /**
   * a transition with the transition classes, the class to apply to start the transition,
   * and the ending transition class: "ease-out duration-300", "opacity-0", "opacity-100"
   * @param transitionClass the transition css classes to apply for the transition
   * @param startClass the class to apply to start the transition
   * @param endClass the class to apply to end the transition
   */
  public Transition(String transitionClass, String startClass, String endClass) {
    data = List.of(
        transitionClass.split("\s+"),
        startClass.split("\s+"),
        endClass.split("\s+")
    );
  }

  public String toJSON() {
    return JS.moshi.adapter(Transition.class).toJson(this);
  }

}

/**
 * TransitionAdapter is a Moshi adaptor for the Transition class.
 */
class TransitionAdapter {
  @ToJson
  public List toJSON(Transition t) {
    return t.data;
  }


}
