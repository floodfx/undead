package com.undead4j.javalin.example.view.component;

import com.google.common.base.Strings;
import com.undead4j.form.Form;
import com.undead4j.template.Live;
import com.undead4j.template.LiveTemplate;

public class Input {

  public static LiveTemplate TextInput(Form form, String name, String... classes)  {
    var value = form.data().get(name);
    var classesString = "";
    for(var clazz : classes) {
      classesString += clazz + " ";
    }
    return Live.HTML."""
        <input type="text" id="\{name}" name="\{name}" value="\{value}" class="\{classesString}" />
        """;
  }

  public static LiveTemplate ErrorMsg(Form form, String key) {
    var error = form.errorFor(key);
    if(Strings.isNullOrEmpty(error)) {
      return Live.EMPTY;
    }
    return Live.HTML."""
      <div class="text-sm text-error">\{error}</div>
    """;
  }

}
