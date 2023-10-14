package com.undead4j.javalin.example;

import com.undead4j.template.Live;
import com.undead4j.template.LiveTemplate;
import com.undead4j.view.Meta;
import com.undead4j.view.View;
class Counter {
  public int count = 0;
}
public class CounterLiveView implements View<Counter> {
  private Counter counter;
  public CounterLiveView(Counter c) {
    this.counter = c;
  }

  public CounterLiveView() {

  }

  @Override
  public void mount() {
    System.out.println("mount called");
    if(this.counter == null) {
      this.counter = new Counter();
    }
  }

  @Override
  public LiveTemplate render(Counter counter, Meta meta) {
    return Live.HTML."""
        Count is \{this.counter.count}
        <br />
        \{Live.when(this.counter.count > 0, Live.HTML."Positive", Live.HTML."Negative")}
        <button type="button" phx-click="inc">Increment</button>
        """;
  }

}
