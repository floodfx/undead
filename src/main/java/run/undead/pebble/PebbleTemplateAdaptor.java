package run.undead.pebble;

import io.pebbletemplates.pebble.PebbleEngine;
import io.pebbletemplates.pebble.template.PebbleTemplate;
import run.undead.template.UndeadTemplate;

import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PebbleTemplateAdaptor {

  private final PebbleEngine engine;

  public PebbleTemplateAdaptor() {
    this(new PebbleEngine.Builder().build());
  }
  public PebbleTemplateAdaptor(PebbleEngine engine) {
    this.engine = engine;
  }

  public UndeadTemplate render(String template, Map<String, Object> data) throws Exception {
    PebbleTemplate compiledTemplate = engine.getTemplate(template);

    var writer = new StringTemplateWriter();
    compiledTemplate.evaluate(writer, data);
    return new UndeadTemplate(writer.toStringTemplate(), false);
  }

  class StringTemplateWriter extends Writer {
    private final List<String> fragments = new ArrayList<>();
    private final List<Object> values = new ArrayList<>();
    private int writeCounts = 0;

    @Override
    public void write(char[] cbuf, int off, int len) {
      var data = new String(cbuf, off, len);
//      System.out.println("write:" + data);
      // all we can do is alternate between fragments and values
      if(writeCounts % 2 == 0) {
        fragments.add(new String(cbuf, off, len));
      } else {
        values.add(new String(cbuf, off, len));
      }
      writeCounts++;
    }

    @Override
    public void flush() {
      // no-op
    }

    @Override
    public void close() {
      // no-op
    }

    public StringTemplate toStringTemplate() {
      if(fragments.size() == values.size()) {
        throw new RuntimeException("template part needs default value; likely caused by a control statement like an if statement with zero output. try adding an else statement with a default or empty value");
      }
      return StringTemplate.of(fragments, values);
    }

    @Override
    public String toString() {
      return toStringTemplate().interpolate();
    }

  }
}
