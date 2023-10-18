package com.undead4j.template;

import java.util.List;

public class Undead {

  public static final StringTemplate.Processor<UndeadTemplate, RuntimeException> HTML =
      template -> {
        return new UndeadTemplate(template);
      };

  public static final UndeadTemplate EMPTY = HTML."";

  /**
   * NO_ESC ensures no additional escaping occurs
   */
  public static UndeadTemplate NO_ESC(Object obj) {
    switch (obj) {
      case UndeadTemplate lt -> {
        return lt;
      }
      default -> {
        var fragment = StringTemplate.of(List.of(String.valueOf(obj)), List.of());
        return new UndeadTemplate(fragment);
      }
    }
  }

  public static UndeadTemplate when(Boolean cond, UndeadTemplate trueCase, UndeadTemplate falseCase) {
    if (cond) {
      return trueCase;
    }
    return falseCase;
  }

}
