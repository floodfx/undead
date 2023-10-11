package com.undead4j.template;

public class Live {

  public static final StringTemplate.Processor<LiveTemplate, RuntimeException> HTML =
      template -> {
        return new LiveTemplate(template);
      };
}
