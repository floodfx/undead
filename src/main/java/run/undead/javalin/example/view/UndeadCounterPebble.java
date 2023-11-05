package run.undead.javalin.example.view;

import io.pebbletemplates.pebble.PebbleEngine;
import run.undead.context.Context;
import run.undead.event.UndeadEvent;
import run.undead.pebble.PebbleTemplateAdaptor;
import run.undead.template.UndeadTemplate;
import run.undead.view.Meta;
import run.undead.view.View;

import java.util.Map;

public class UndeadCounterPebble implements View {
  private Integer count;
  private PebbleTemplateAdaptor adaptor = new PebbleTemplateAdaptor(new PebbleEngine.Builder()
      .build());

  public UndeadCounterPebble() {
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
    try {
    return adaptor.render("count.html", Map.of("count", this.count));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
