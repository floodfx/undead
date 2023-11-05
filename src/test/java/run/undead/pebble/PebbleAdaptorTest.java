package run.undead.pebble;

import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
public class PebbleAdaptorTest {

  @Test
  public void testAdaptor() throws Exception {
    var adaptor = new PebbleTemplateAdaptor();
    var template = adaptor.render("test.html",
        Map.of(
            "name", "<h1>Undead</h1>",
            "articles", List.of(),
            "sub", "foo"
        )
    );

    assertEquals(StringTemplate.STR."""
        <!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <title>Title</title>
        </head>
        <body>
        Hello &lt;h1&gt;Undead&lt;/h1&gt;!
                
        <p> There are no articles. </p>
                
                
        <p>Please select a category</p>
                
        <p>foo</p>
        </body>
        </html>""",template.toString());
    System.out.println("parts:" + template.toParts());
    var parts = new LinkedHashMap<>();
        parts.put("0", "&amp;lt;h1&amp;gt;Undead&amp;lt;&#x2F;h1&amp;gt;");
        parts.put("1", "&lt;p&gt; There are no articles. &lt;&#x2F;p&gt;\n");
        parts.put("2", "&lt;p&gt;Please select a category&lt;&#x2F;p&gt;\n");
        parts.put("3", "&lt;p&gt;foo&lt;&#x2F;p&gt;\n");
        parts.put("s", List.of(
            "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Title</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "Hello ",
            "!\n" +
                "\n",
            "\n" +
                "\n",
            "\n",
            "</body>\n" +
                "</html>"
        ));
    assertEquals(parts, template.toParts());

  }
}
