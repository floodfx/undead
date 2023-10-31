package run.undead.javalin.example.view;

import run.undead.event.UndeadEvent;
import run.undead.form.Form;
import run.undead.javalin.example.view.model.UserModel;
import run.undead.context.Context;
import run.undead.template.UndeadTemplate;
import run.undead.view.Meta;
import run.undead.view.View;

import static run.undead.javalin.example.view.tags.Input.ErrorMsg;
import static run.undead.javalin.example.view.tags.Input.TextInput;
import static run.undead.template.Directive.*;

/**
 * UndeadUserForm is a simple form that uses Undead's {@link Form} to validate and save a
 * {@link UserModel} and display the user once saved.
 */
public class UndeadUserForm implements View {

  private Form<UserModel> form;
  private UserModel user;

  public UndeadUserForm() {
    // initialize the form with an empty UserModel
    this.form = new Form<>(UserModel.class);
  }

  @Override
  public void handleEvent(Context context, UndeadEvent event) {
    if (event.type().equals("validate")) {
      // for "validate" (i.e. form changes) we update the form with the data
      // from the event which will trigger validations and update the form state
      // and trigger a re-render
      this.form.update(event.data(), event.type());
      return;
    }
    if (event.type().equals("submit")) {
      // for "submit" we update the form with the data from the event as well
      // and then check if the form is valid
      // if form is valid, "save" user and "reset" the form
      this.form.update(event.data(), event.type());
      if (this.form.valid()) {
        this.user = this.form.model();
        this.form = new Form<>(UserModel.class);
      }
    }
  }

  @Override
  public UndeadTemplate render(Meta meta) {
    return HTML. """
      <form class="form" ud-change="validate" ud-submit="submit">
        <div class="flex flex-col space-y-4 mx-4 w-[250px]">
          <h2 class="text-2xl">User Form</h2>
          <label for="name">Name</label>
          \{ TextInput(form, "name", "input input-bordered") }
          \{ ErrorMsg(form, "name") }
          <label for="email">Email</label>
          \{ TextInput(form, "email", "input input-bordered") }
          \{ ErrorMsg(form, "email") }
          <button \{ If(!form.valid(), HTML." disabled", EMPTY) } class="btn btn-primary" type="submit">Submit</button>
        </div>
      </form>
      \{ maybeShowUser(user) }""" ;
  }

  private UndeadTemplate maybeShowUser(UserModel user) {
    if (user == null) {
      return EMPTY;
    }
    return HTML. """
      <div class="flex flex-col mt-6 space-y-4 mx-4 w-[250px]">
        <h2 class="text-2xl">User</h2>
        <div>Name: \{ user.name() }</div>
        <div>Email: \{ user.email() }</div>
      </div>
    """ ;
  }
}
