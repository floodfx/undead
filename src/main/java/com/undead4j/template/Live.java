package com.undead4j.template;

import java.util.List;

public class Live {

  public static final StringTemplate.Processor<LiveTemplate, RuntimeException> HTML =
      template -> {
        return new LiveTemplate(template);
      };

  public static final LiveTemplate EMPTY = HTML."";

  /**
   * NO_ESC ensures no additional escaping occurs
   */
  public static LiveTemplate NO_ESC(Object obj) {
    switch(obj) {
      case LiveTemplate lt -> {
        return lt;
      }
      default -> {
        var fragment = StringTemplate.of(List.of(String.valueOf(obj)), List.of());
        return new LiveTemplate(fragment);
      }
    }
  }

  public static LiveTemplate when(Boolean cond, LiveTemplate trueCase, LiveTemplate falseCase) {
    if(cond) {
      return trueCase;
    }
    return falseCase;
  }

}
