package com.undead4j.template;

import org.junit.jupiter.api.Test;

import java.util.List;

import static com.undead4j.template.Undead.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
      \{Map(items, item -> HTML."<div>\{item}</div>")}
    """.trim();
    assertEquals("<div>foo</div><div>bar</div><div>1</div><div>false</div>", template.toString());
  }

  @Test
  public void testJoin() {
    var items = List.of("foo", "bar", 1, false);
    var template = HTML."""
      \{Join(
          Map(items, item -> HTML."<div>\{item}</div>"),
          HTML."|"
      )}
    """.trim();
    assertEquals("<div>foo</div>|<div>bar</div>|<div>1</div>|<div>false</div>", template.toString());
  }

  @Test
  public void testSwitch() {
    var template = HTML."""
      \{Switch(1,
        Case.of(i -> i == 1, i -> HTML."one"),
        Case.of(i -> i == 2, i -> HTML."two"),
        Case.of(i -> i == 3, i -> HTML."three"),
        Case.defaultOf(i -> HTML."other(\{i})")
      )}
    """.trim();
    assertEquals("one", template.toString());

    var color = "red";
    template = HTML."""
      \{Switch(
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
      \{Map(
          Range(10),
          i -> HTML."<div>\{i}</div>"
      )}
    """.trim();
    assertEquals("<div>0</div><div>1</div><div>2</div><div>3</div><div>4</div><div>5</div><div>6</div><div>7</div><div>8</div><div>9</div>", template.toString());

    template = HTML."""
      \{Map(
          Range(2, 10, 2),
          i -> HTML."<div>\{i}</div>"
      )}
    """.trim();
    assertEquals("<div>2</div><div>4</div><div>6</div><div>8</div>", template.toString());

    // negative step
    template = HTML."""
      \{Map(
          Range(10, 2, -2),
          i -> HTML."<div>\{i}</div>"
      )}
    """.trim();
    assertEquals("<div>10</div><div>8</div><div>6</div><div>4</div>", template.toString());
  }

}
