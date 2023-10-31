package run.undead.javalin.example.view.tags;

import com.google.common.base.Strings;
import run.undead.form.Form;
import run.undead.template.Undead;
import run.undead.template.UndeadTemplate;

/**
 * Input contains some helper tags for rendering form text inputs and their error messages.
 */
public class Input {

  /**
   * TextInput renders a text input with the given name and value with the optional css classes.
   * @param form the form to pull the value from
   * @param name the name of the input field and the key to pull the value from the form
   * @param classes the optional css classes to apply to the input
   * @return the {@link UndeadTemplate} for the text input
   */
  public static UndeadTemplate TextInput(Form form, String name, String... classes) {
    var value = form.data().get(name);
    var classesString = "";
    for (var clazz : classes) {
      classesString += clazz + " ";
    }
    return Undead.HTML. """
        <input type="text" id="\{ name }" name="\{ name }" value="\{ value }" ud-debounce="500" class="\{ classesString }" />
        """ ;
  }

  /**
   * ErrorMsg renders the error message for the given form and input name
   * @param form the form to pull the error message from
   * @param name the name of the input field and the key to pull the error message from the form
   * @return the {@link UndeadTemplate} for the error message
   */
  public static UndeadTemplate ErrorMsg(Form form, String name) {
    var error = form.errorFor(name);
    if (Strings.isNullOrEmpty(error)) {
      return Undead.EMPTY;
    }
    return Undead.HTML. """
      <div class="text-sm text-error">\{ error }</div>
    """ ;
  }

}
