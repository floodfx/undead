package run.undead.template;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UndeadTemplateTest {

  @Test
  public void testUndeadTemplate() {
    UndeadTemplate template = Directive.HTML."test";
    assertEquals("test", template.toString());
  }

  @Test
  public void testUndeadTemplateWithTags() {
    UndeadTemplate template = Directive.HTML."<h1>test</h1>";
    assertEquals("<h1>test</h1>", template.toString());
  }

  @Test
  public void testConcat() {
    UndeadTemplate template = UndeadTemplate.concat(Directive.HTML."test", Directive.HTML."test");
    assertEquals("testtest", template.toString());

    // use values
    var foo = "foo";
    var bar = "bar";
    template = UndeadTemplate.concat(Directive.HTML."<foo>\{foo}</foo>", Directive.HTML."test", Directive.HTML."<bar>\{bar}</bar>");
    assertEquals("<foo>foo</foo>test<bar>bar</bar>", template.toString());
  }

  @Test
  public void testMap() {
    var items = List.of("foo", "bar", 1, false);
    var template = Directive.HTML."""
      \{ Directive.Map(items, item -> Directive.HTML."<div>\{item}</div>")}
    """.trim();
    assertEquals("<div>foo</div><div>bar</div><div>1</div><div>false</div>", template.toString());
  }

  @Test
  public void testJoin() {
    var items = List.of("foo", "bar", 1, false);
    var template = Directive.HTML."""
      \{ Directive.Join(
          Directive.Map(items, item -> Directive.HTML."<div>\{item}</div>"),
          Directive.HTML."|"
      )}
    """.trim();
    assertEquals("<div>foo</div>|<div>bar</div>|<div>1</div>|<div>false</div>", template.toString());
  }

  @Test
  public void testSwitch() {
    var template = Directive.HTML."""
      \{ Directive.Switch(1,
        Directive.Case.of(i -> i == 1, i -> Directive.HTML."one"),
        Directive.Case.of(i -> i == 2, i -> Directive.HTML."two"),
        Directive.Case.of(i -> i == 3, i -> Directive.HTML."three"),
        Directive.Case.defaultOf(i -> Directive.HTML."other(\{i})")
      )}
    """.trim();
    assertEquals("one", template.toString());

    var color = "red";
    template = Directive.HTML."""
      \{ Directive.Switch(
          Directive.Case.of("blue".equals(color), Directive.HTML."blue"),
          Directive.Case.of("red".equals(color), Directive.HTML."red"),
          Directive.Case.defaultOf(Directive.HTML."green")
        )}
    """.trim();
    assertEquals("red", template.toString());
  }

  @Test
  public void testRange() {
    var template = Directive.HTML."""
      \{ Directive.Map(
          Directive.Range(10),
          i -> Directive.HTML."<div>\{i}</div>"
      )}
    """.trim();
    assertEquals("<div>0</div><div>1</div><div>2</div><div>3</div><div>4</div><div>5</div><div>6</div><div>7</div><div>8</div><div>9</div>", template.toString());

    template = Directive.HTML."""
      \{ Directive.Map(
          Directive.Range(2, 10, 2),
          i -> Directive.HTML."<div>\{i}</div>"
      )}
    """.trim();
    assertEquals("<div>2</div><div>4</div><div>6</div><div>8</div>", template.toString());

    // negative step
    template = Directive.HTML."""
      \{ Directive.Map(
          Directive.Range(10, 2, -2),
          i -> Directive.HTML."<div>\{i}</div>"
      )}
    """.trim();
    assertEquals("<div>10</div><div>8</div><div>6</div><div>4</div>", template.toString());
  }

}
