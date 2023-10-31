package run.undead.form;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import run.undead.url.Values;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import run.undead.view.View;

import java.util.*;

/**
 * Form makes it dead easy to validate and bind form data to a model in Undead.
 * Currently, this requires the use <a href="https://beanvalidation.org/">Jakarta Bean Validation (JSR 380)</a>
 * annotations to validate the model.  Form handles mapping the form data to the model automatically
 * and providing a easy-to-use API for rendering form inputs and error messages.
 * <p>
 *   Here is an example {@link View} that uses Form:
 *   <pre>{@code
 *      // first define UserModel with Jakarta Bean Validation annotations
 *      public record UserModel(@NotBlank String name, @NotBlank @Email String email) { }
 *   }</pre>
 *
 *   Then define a view that uses this model and form:
 *   {@code
 *   public class UndeadUserForm implements View {
 *
 *   private Form<UserModel> form;
 *   private UserModel user;
 *
 *   public UndeadUserForm() {
 *     this.form = new Form<>(UserModel.class, new Values());
 *   }
 *
 *   @Override
 *   public void handleEvent(Socket socket, UndeadEvent event) {
 *     if (event.type().equals("validate")) {
 *       // just update the form with the data from the event
 *       // which will trigger validations and update the form state
 *       this.form.update(event.data(), event.type());
 *       return;
 *     }
 *     if (event.type().equals("submit")) {
 *       this.form.update(event.data(), event.type());
 *       // if form is valid, "save" user and reset form
 *       if (this.form.valid()) {
 *         this.user = this.form.model();
 *         this.form = new Form<>(UserModel.class, new Values());
 *       }
 *     }
 *   }
 *
 *   @Override
 *   public UndeadTemplate render(Meta meta) {
 *     // NOTE the <code>ud-change="validate"</code> and <code>ud-submit="submit"</code> attributes
 *     // on the form element.  These are used to trigger the form validation and submission events
 *     return HTML. """
 *       <form class="form" ud-change="validate" ud-submit="submit">
 *         <div class="flex flex-col space-y-4 mx-4 w-[250px]">
 *           <h2 class="text-2xl">User Form</h2>
 *           <label for="name">Name</label>
 *           \{ TextInput(form, "name", "input input-bordered") }
 *           \{ ErrorMsg(form, "name") }
 *           <label for="email">Email</label>
 *           \{ TextInput(form, "email", "input input-bordered") }
 *           \{ ErrorMsg(form, "email") }
 *           <button \{ If(!form.valid(), HTML." disabled", EMPTY) } class="btn btn-primary" type="submit">Submit</button>
 *         </div>
 *       </form>
 *       """ ;
 *   }
 *   }
 *  </p>
 *   <p>
 *     The view above uses the following helper tags to render the form inputs and error messages:
 *     <pre>{@code
 *        public class Input {
 *          public static UndeadTemplate TextInput(Form form, String name, String... classes) {
 *            var value = form.data().get(name);
 *            var classesString = "";
 *            for (var clazz : classes) {
 *              classesString += clazz + " ";
 *            }
 *            return Directive.HTML. """
 *              <input type="text" id="\{ name }" name="\{ name }" value="\{ value }" class="\{ classesString }" />
 *            """ ;
 *          }
 *          public static UndeadTemplate ErrorMsg(Form form, String name) {
 *            var error = form.errorFor(name);
 *            if (Strings.isNullOrEmpty(error)) {
 *              return Directive.EMPTY;
 *            }
 *            return Directive.HTML. """
 *              <div class="text-sm text-error">\{ error }</div>
 *            """ ;
 *          }
 *        }
 *     }</pre>
 *  </p>
 *
 * @param <T> the type of the model to which the form data is bound.
 *
 */
public class Form<T> {

  private final ObjectMapper mapper;
  private final Validator validator;
  private final Values values;
  private final Values changes;
  private final Class clazz;
  private String action;
  private final Set<String> touched;
  private T model;
  private Map<String, String> errors;

  /**
   * Create a new, empty form with the given model.
   * @param clazz the class of the model to bind the form data to
   */
  public Form(Class clazz) {
    this(clazz, new Values());
  }

  /**
   * Create a new form with the given model and initial values.
   * @param clazz the class of the model to bind the form data to
   * @param initial the initial values for the form which can be empty
   */
  public Form(Class clazz, Values initial) {
    this.mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    this.validator = Validation.byDefaultProvider()
        .configure()
        .messageInterpolator(new ParameterMessageInterpolator())
        .buildValidatorFactory()
        .getValidator();
    this.touched = new HashSet<>();
    this.values = initial;
    this.changes = new Values();
    this.clazz = clazz;
    this.errors = new HashMap<>();
    this.model = (T) mapper.convertValue(values.asMap(), clazz);
  }

  /**
   * Updates the form with new values and an action.  If the action is
   * null or empty, the form will not be validated (valid() will return true)
   * regardless of whether or not there are errors.
   * @param newValues the new values to update the form with
   * @param action the action to validate the form with
   */
  public void update(Values newValues, String action) {
    this.action = action;

    // merge old and new data and calculate changes
    newValues.asMap().forEach((k, v) -> {
      if (values.get(k) == null || !values.get(k).equals(v)) {
        switch (v) {
          case String s -> changes.add(k, s);
          case List l -> {
            for (Object o : l) {
              changes.add(k, String.valueOf(o));
            }
          }
          // throw error?
          default -> changes.add(k, String.valueOf(v));
        }
      }
      values.set(k, v);
    });
    // handle case where _target is set but the newData does not contain the _target field
    // this happens in the case of a checkbox that is unchecked
    var target = newValues.get("_target");
    if (target != null && newValues.get(target) == null) {
      values.delete(target);
      changes.add(target, "");
    }
    // validate if action is not empty
    if (!Strings.isNullOrEmpty(action)) {
      // map values to model
      model = (T) mapper.convertValue(values.asMap(), clazz);
      // validate model
      var violations = validator.validate(model);
      // convert violations to errors
      errors = new HashMap<>();
      for (var v : violations) {
        errors.put(v.getPropertyPath().toString(), v.getMessage());
      }

      // if we get a _target field in the form, use it to indicate which fields were touched
      // if not, assume all fields in input were touched and all fields
      // with errors were touched
      if (target != null) {
        this.touched.add(target);
      } else {
        for (var k : values.asMap().keySet()) {
          this.touched.add(k);
        }
        for (var e : errors.keySet()) {
          this.touched.add(e);
        }
      }
    }
  }

  /**
   * Returns the errors for the form.
   * @return the errors for the form mapped by field name
   */
  public Map<String, String> errors() {
    return this.errors;
  }

  /**
   * Returns the error for the given field name.
   * @param key the field name to get the error for
   * @return the error for the given field name or null if there is no error
   */
  public String errorFor(String key) {
    if (errors == null || errors.get(key) == null || touched == null || !touched.contains(key) || valid()) {
      return null;
    }
    return this.errors.get(key);
  }

  /**
   * Returns the latest model for the form with the form data bound to it.
   * @return the latest model for the form as type T
   */
  public T model() {
    return this.model;
  }

  /**
   * Returns the changes for the form, that is, the fields that have changed
   * since it was initialized and their current values.
   * @return the changes for the form mapped by field name
   */
  public Map<String, Object> changes() {
    return this.changes.asMap();
  }

  /**
   * Returns the values for the form, that is, the last set of values that were
   * passed to the form.
   * @return the {@link Values} for the form
   */
  public Values data() {
    return this.values;
  }

  /**
   * Returns whether or not the form is "valid".  A form is valid if there has been an update
   * with an non-null or empty action and there are no errors on any of the fields that have been
   * touched (i.e. attempted to be changed) since the last update.
   * @return true if the form is valid, false otherwise
   */
  public boolean valid() {
    // no action or empty errors means is always valid
    if (Strings.isNullOrEmpty(action) || errors().isEmpty() || touched.isEmpty()) {
      return true;
    }
    // if nothing was touched the changeset is valid
    // regardless of whether or not there are errors
    // otherwise, only check for errors on touched fields
    // and return false if there are any errors
    for (String k : touched) {
      if (errors().containsKey(k)) {
        return false;
      }
    }
    return true;
  }
}
