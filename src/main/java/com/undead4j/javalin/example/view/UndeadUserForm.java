package com.undead4j.javalin.example.view;

import com.undead4j.event.UndeadEvent;
import com.undead4j.form.Form;
import com.undead4j.javalin.example.view.model.UserModel;
import com.undead4j.socket.Socket;
import com.undead4j.template.LiveTemplate;
import com.undead4j.url.Values;
import com.undead4j.view.Meta;
import com.undead4j.view.View;

import static com.undead4j.javalin.example.view.component.Input.ErrorMsg;
import static com.undead4j.javalin.example.view.component.Input.TextInput;
import static com.undead4j.template.Live.*;

public class UndeadUserForm implements View {

  private Form<UserModel> form;
  private UserModel user;

  public UndeadUserForm() {
    this.form = new Form<>(UserModel.class, new Values());
  }

  @Override
  public void handleEvent(Socket socket, UndeadEvent event) {
    if(event.type().equals("validate")) {
      // just update the form with the data from the event
      // which will trigger validations and update the form state
      this.form.update(event.data(), event.type());
      return;
    }
    if(event.type().equals("submit")) {
      this.form.update(event.data(), event.type());
      // if form is valid, "save" user and reset form
      if (this.form.valid()) {
        this.user = this.form.model();
        this.form = new Form<>(UserModel.class, new Values());
      }
    }
  }

  @Override
  public LiveTemplate render(Meta meta) {
    return HTML."""
      <form class="form" phx-change="validate" phx-submit="submit">
        <div class="flex flex-col space-y-4 mx-4 w-[250px]">
          <h2 class="text-2xl">User Form</h2>
          <label for="name">Name</label>
          \{ TextInput(form, "name", "input input-bordered")}
          \{ ErrorMsg(form, "name")}
          <label for="email">Email</label>
          \{ TextInput(form, "email", "input input-bordered")}
          \{ ErrorMsg(form, "email")}
          <button \{when(!form.valid(), HTML." disabled", EMPTY)} class="btn btn-primary" type="button" phx-click="submit">Submit</button>
        </div>
      </form>
      \{maybeShowUser(user)}""";
  }

  private LiveTemplate maybeShowUser(UserModel user) {
    if(user == null) {
      return EMPTY;
    }
    return HTML."""
      <div class="flex flex-col mt-6 space-y-4 mx-4 w-[250px]">
        <h2 class="text-2xl">User</h2>
        <div>Name: \{user.name()}</div>
        <div>Email: \{user.email()}</div>
      </div>
    """;
  }
}
