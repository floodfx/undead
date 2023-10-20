package com.undead4j.javalin.example.view;

import com.undead4j.event.UndeadEvent;
import com.undead4j.socket.Socket;
import com.undead4j.template.UndeadTemplate;
import com.undead4j.view.Meta;
import com.undead4j.view.View;

import java.util.Map;

import static com.undead4j.template.Undead.*;

public class UndeadCounter implements View {
  private Integer count;

  public UndeadCounter() {
    this.count = 0;
  }

  @Override
  public void mount(Socket socket, Map sessionData, Map params) {
    System.out.println("mount called - sessionData" + sessionData + " params: " + params);
    if (params.get("start") != null) {
      this.count = Integer.parseInt((String) params.get("start"));
    } else {
      this.count = 0;
    }
  }

  @Override
  public void handleEvent(Socket socket, UndeadEvent event) {
    System.out.println("handleEvent called: " + event.type() + " data: " + event.data());
    if (event.type().equals("inc")) {
      this.count++;
    } else if (event.type().equals("dec")) {
      this.count--;
    }
  }

  @Override
  public UndeadTemplate render(Meta meta) {
    return HTML. """
      <div class="flex flex-col space-y-4 mx-4">
        <div class="text-2xl">Zombie Count</div>
        <div class="flex items-center space-x-4">
          <button class="btn btn-primary" type="button" phx-click="dec">-</button>
          <h2 class="text-2xl font-mono">
            <span class="\{ When(count > 0, HTML."text-warning", HTML."text-success") }">
              \{ count }
            </span>
          </h2>
          <button class="btn btn-primary" type="button" phx-click="inc">+</button>
        </div>
        <span class="text-xs">status</span>
        \{Switch(count,
            Case.of(c -> c > 0, c -> {return HTML. """
              <div class="text-sm text-warning">Zombies are coming</div>
            """;}),
            Case.of(c -> c < 0, c -> {return HTML. """
              <div class="text-sm text-success">Zombies are leaving</div>
            """;}),
            Case.defaultOf(c -> {return HTML. """
              <div class="text-sm">\{c} zombies</div>
            """;})
          ) }
      </div>
        """ ;
  }

}
