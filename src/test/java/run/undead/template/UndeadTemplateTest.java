package run.undead.template;

import com.google.common.collect.Maps;
import org.junit.jupiter.api.Test;
import run.undead.template.Directive.Case;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static run.undead.template.Directive.Switch;
import static run.undead.template.Undead.HTML;

public class UndeadTemplateTest {

  @Test
  public void testUndeadTemplate() {
    UndeadTemplate template = HTML."test";
    assertEquals("test", template.toString());
  }

  @Test
  public void testUndeadTemplateWithTags() {
    UndeadTemplate template = HTML."<h1>test</h1>";
    assertEquals("<h1>test</h1>", template.toString());
  }

  @Test
  public void testConcat() {
    UndeadTemplate template = UndeadTemplate.concat(HTML."test", HTML."test");
    assertEquals("testtest", template.toString());

    // use values
    var foo = "foo";
    var bar = "bar";
    template = UndeadTemplate.concat(HTML."<foo>\{foo}</foo>", HTML."test", HTML."<bar>\{bar}</bar>");
    assertEquals("<foo>foo</foo>test<bar>bar</bar>", template.toString());
  }

  @Test
  public void testMap() {
    var items = List.of("foo", "bar", 1, false);
    var template = HTML."""
      \{ Directive.Map(items, item -> HTML."<div>\{item}</div>")}
    """.trim();
    assertEquals("<div>foo</div><div>bar</div><div>1</div><div>false</div>", template.toString());
  }

  @Test
  public void testJoin() {
    var items = List.of("foo", "bar", 1, false);
    var template = HTML."""
      \{ Directive.Join(
          Directive.Map(items, item -> HTML."<div>\{item}</div>"),
        HTML."|"
      )}
    """.trim();
    assertEquals("<div>foo</div>|<div>bar</div>|<div>1</div>|<div>false</div>", template.toString());
  }

  @Test
  public void testSwitch() {
    var template = HTML."""
      \{ Switch(1,
        Case.of(i -> i == 1, i -> HTML."one"),
        Case.of(i -> i == 2, i -> HTML."two"),
        Case.of(i -> i == 3, i -> HTML."three"),
        Case.defaultOf(i -> HTML."other(\{i})")
      )}
    """.trim();
    assertEquals("one", template.toString());

    var color = "red";
    template = HTML."""
      \{ Switch(
          Case.of("blue".equals(color), HTML."blue"),
          Case.of("red".equals(color), HTML."red"),
          Case.defaultOf(HTML."green")
        )}
    """.trim();
    assertEquals("red", template.toString());
  }

  @Test
  public void testRange() {
    var template = HTML."""
      \{ Directive.Map(
          Directive.Range(10),
          i -> HTML."<div>\{i}</div>"
      )}
    """.trim();
    assertEquals("<div>0</div><div>1</div><div>2</div><div>3</div><div>4</div><div>5</div><div>6</div><div>7</div><div>8</div><div>9</div>", template.toString());

    template = HTML."""
      \{ Directive.Map(
          Directive.Range(2, 10, 2),
          i -> HTML."<div>\{i}</div>"
      )}
    """.trim();
    assertEquals("<div>2</div><div>4</div><div>6</div><div>8</div>", template.toString());

    // negative step
    template = HTML."""
      \{ Directive.Map(
          Directive.Range(10, 2, -2),
          i -> HTML."<div>\{i}</div>"
      )}
    """.trim();
    assertEquals("<div>10</div><div>8</div><div>6</div><div>4</div>", template.toString());
  }

  @Test
  public void testBasicDiff() {
    Function<Map, UndeadTemplate> tmplFn = (Map vars) -> {
      return HTML."""
      <div>
        <h1>hello \{vars.get("name")}</h1>
        \{ HTML."<h2>\{vars.get("email")}</h2>"}
      </div>
    """;
    };
    var vars = Maps.newHashMap();
    vars.put("name", "foo");
    vars.put("email", "e1");
    var t = tmplFn.apply(vars);
    vars.put("email", "e2");
    var t2 = tmplFn.apply(vars);
    var d = UndeadTemplate.diff(t.toParts(), t2.toParts());
    assertEquals(d, Map.of("1", Map.of("0","e2")));
  }

  @Test
  public void testSwitchDiff() {
    Function<Map, UndeadTemplate> tmplFn = (Map vars) -> {
      return HTML."""
      <div>
        <h1>hello \{vars.get("name")}</h1>
        \{ Switch( (Integer)vars.get("count"),
              Case.of(c -> c == 0, c -> HTML." zero:\{c} "),
              Case.of(c -> c == 1, c -> HTML."one:\{c}  "),
              Case.of(c -> c >= 2, c -> HTML."  more:\{c}")
          )
        }
      </div>
    """;
    };
    var vars = Maps.newHashMap();
    vars.put("name", "foo");
    vars.put("count", 0);
    var t = tmplFn.apply(vars);
    vars.put("count", 1);
    var t2 = tmplFn.apply(vars);
    var d = UndeadTemplate.diff(t.toParts(), t2.toParts());
    assertEquals(d, Map.of("1", Map.of("0","1", "s", List.of("one:", "  "))));
    vars.put("count", 2);
    var t3 = tmplFn.apply(vars);
    d = UndeadTemplate.diff(t2.toParts(), t3.toParts());
    assertEquals(d, Map.of("1", Map.of("0","2", "s", List.of("  more:", ""))));
  }

}
