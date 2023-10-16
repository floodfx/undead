package com.undead4j.javalin.example.view;

import com.undead4j.event.UndeadEvent;
import com.undead4j.socket.Socket;
import com.undead4j.template.Live;
import com.undead4j.template.LiveTemplate;
import com.undead4j.view.Meta;
import com.undead4j.view.View;

import java.util.Map;

public class UndeadCounter implements View {
  private int count;

  public UndeadCounter() {
    this.count = 0;
  }

  @Override
  public void mount(Socket socket, Map sessionData, Map params) {
    System.out.println("mount called - sessionData"+sessionData + " params: "+params);
    if(params.get("start") != null) {
      this.count = Integer.parseInt((String)params.get("start"));
    } else {
      this.count = 0;
    }
  }

  @Override
  public void handleEvent(Socket socket, UndeadEvent event) {
    System.out.println("handleEvent called: "+event.type()+" data: "+event.data());
    if(event.type().equals("inc")) {
      this.count++;
    } else if(event.type().equals("dec")) {
      this.count--;
    }
  }

  @Override
  public LiveTemplate render(Meta meta) {
    return Live.HTML."""
      <div class="flex flex-col space-y-4 mx-4">
        <h2 class="text-2xl">Count:
          <span class="\{Live.when(count > 0, Live.HTML."text-success", Live.HTML."text-warning")}">
            \{count}
          </span>
        </h2>
        <div class="flex space-x-4">
          <button class="btn btn-secondary" type="button" phx-click="dec">Decrement</button>
          <button class="btn btn-secondary" type="button" phx-click="inc">Increment</button>
        </div>
        """;
  }

}
