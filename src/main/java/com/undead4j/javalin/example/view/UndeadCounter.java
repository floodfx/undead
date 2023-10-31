package com.undead4j.javalin.example.view;

import com.undead4j.event.UndeadEvent;
import com.undead4j.js.HideOpts;
import com.undead4j.js.JS;
import com.undead4j.js.ShowOpts;
import com.undead4j.context.Context;
import com.undead4j.template.UndeadTemplate;
import com.undead4j.view.Meta;
import com.undead4j.view.View;

import java.util.Map;

import static com.undead4j.template.Directive.*;

public class UndeadCounter implements View {
  private Integer count;

  public UndeadCounter() {
    this.count = 0;
  }

  @Override
  public void mount(Context context, Map sessionData, Map params) {
    if (params.get("start") != null) {
      this.count = Integer.parseInt((String) params.get("start"));
    } else {
      this.count = 0;
    }
  }

  @Override
  public void handleEvent(Context context, UndeadEvent event) {
    if (event.type().equals("inc")) {
      this.count++;
    } else if (event.type().equals("dec") && this.count > 0) {
      this.count--;
    }
  }

  @Override
  public UndeadTemplate render(Meta meta) {
    return HTML. """
      <div class="flex flex-col space-y-4 mx-4">
        <div class="text-2xl">Zombie Count</div>
        <div class="flex items-center space-x-4">
          <button class="btn btn-primary" \{If(count <= 0, HTML." disabled")} type="button" ud-click="dec">-</button>
          <span class="countdown font-mono text-6xl \{Switch(count, Case.of(count == 0, HTML."text-neutral"), Case.of(count <= 9, HTML."text-warning"), Case.of(count > 9, HTML."text-error"))}">
            <span style="--value:\{ count };"></span>
          </span>
          <button class="btn btn-primary" type="button" ud-click="inc">+</button>
        </div>
        <div class="flex flex-col items-start">
          <button id="show-btn" class="hidden link text-xs" ud-click="\{new JS().show(new ShowOpts("#status")).hide(new HideOpts()).show(new ShowOpts("#hide-btn"))}">Show Status</button>
          <button id="hide-btn" class="link text-xs" ud-click='\{new JS().hide(new HideOpts("#status")).show(new ShowOpts("#show-btn")).hide(new HideOpts())}'>Hide Status</button>
        </div>
        <div id="status" class="flex flex-col">
          <span class="text-xs" ud-click="\{new JS().hide()}">🧟 status</span>
          \{Switch(count,
              Case.of(c -> c == 1, c -> {return HTML. """
                <div class="text-sm text-warning">Zombies are coming</div>
              """;}),
              Case.of(c -> 1 < c && c <= 4, c -> {return HTML. """
                <div class="text-sm text-warning">Zombies are here</div>
              """;}),
              Case.of(c -> 4 < c && c <= 9, c -> {return HTML. """
                <div class="text-sm text-warning">Uh oh, they are breaking in!</div>
              """;}),
              Case.of(c -> c > 9, c -> {return HTML. """
                <div class="text-sm text-error">Zombies are eating your 🧠</div>
              """;}),
              Case.defaultOf(c -> {return HTML. """
                <div class="text-sm">\{c} zombies.  That's good.</div>
              """;})
            ) }
        </div>
      </div>
        """ ;
  }

}
