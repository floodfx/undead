package com.undead4j.javalin.example.view.component;

import com.google.common.base.Strings;
import com.undead4j.form.Form;
import com.undead4j.template.Undead;
import com.undead4j.template.UndeadTemplate;

public class Input {

  public static UndeadTemplate TextInput(Form form, String name, String... classes) {
    var value = form.data().get(name);
    var classesString = "";
    for (var clazz : classes) {
      classesString += clazz + " ";
    }
    return Undead.HTML. """
        <input type="text" id="\{ name }" name="\{ name }" value="\{ value }" class="\{ classesString }" />
        """ ;
  }

  public static UndeadTemplate ErrorMsg(Form form, String key) {
    var error = form.errorFor(key);
    if (Strings.isNullOrEmpty(error)) {
      return Undead.EMPTY;
    }
    return Undead.HTML. """
      <div class="text-sm text-error">\{ error }</div>
    """ ;
  }

}
