package run.undead.context;

import org.junit.jupiter.api.Test;
import run.undead.template.Undead;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ContextTest {

  @Test
  public void testBasicDiff() {
    var ctx = new WsContext(null, null, null);
    var name = "foo";
    var tmpl = Undead.HTML."""
        <div>
        <h1>hello \{name}</h1>
      </div>
    """;
    var p = ctx.diffParts(tmpl);
    // no diff
    assertEquals(tmpl.toParts(), p);

    name = "bar";
    var tmpl2 = Undead.HTML."""
        <div>
        <h1>hello \{name}</h1>
      </div>
    """;
    var p2 = ctx.diffParts(tmpl2);
    assertEquals(p2, Map.of("0", "bar"));
  }

}
