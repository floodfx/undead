package com.undead4j.javalin.example;

import com.undead4j.event.UndeadEvent;
import com.undead4j.socket.Socket;
import com.undead4j.template.Live;
import com.undead4j.template.LiveTemplate;
import com.undead4j.view.Meta;
import com.undead4j.view.View;

import java.util.Map;

class Counter {
  public int count = 0;
}
public class CounterLiveView implements View {
  private Counter counter;

  public CounterLiveView() {
    this.counter = new Counter();
  }

  @Override
  public void mount(Socket socket, Map sessionData, Map params) {
    System.out.println("mount called - sessionData"+sessionData + " params: "+params);
    if(params.get("start") != null) {
      this.counter.count = Integer.parseInt((String)params.get("start"));
    } else {
      this.counter.count = 0;
    }
  }

  @Override
  public void handleEvent(Socket socket, UndeadEvent event) {
    System.out.println("handleEvent called: "+event.type()+" data: "+event.data());
    if(event.type().equals("inc")) {
      this.counter.count++;
    }
  }

  @Override
  public LiveTemplate render(Meta meta) {
    return Live.HTML."""
        Count is \{this.counter.count}
        <br />
        \{Live.when(this.counter.count > 0, Live.HTML."Positive", Live.HTML."Negative")}
        <button class="btn" type="button" phx-click="inc">Increment</button>

        <br />
        <form phx-change="form">
          <input type="text" name="name" />
          <button type="submit">Submit</button>
        </form>
        """;
  }

}
