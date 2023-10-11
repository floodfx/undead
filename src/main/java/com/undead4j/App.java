package com.undead4j;

import static com.undead4j.template.Live.HTML;
import static java.lang.StringTemplate.RAW;

/**
 * Hello world!
 */
public class App {
  public static void main(String[] args) {

    var foo = "<h1>title</h1>";

    var div = HTML. "<div>\{ foo }</div>" ;
    System.out.println(div);

    var name = "foo";
    var bar = "<h1>baz</h1>";
    var num = 22;
    var msg = STR. "<div>\{ name }\{ foo }</div>\{ bar } and a num:\{ num }" ;
    var html = RAW. "<div>\{ name }\{ foo }</div>\{ bar } and a num:\{ num }" ;
    System.out.println("Str:" + msg);
    System.out.println("Raw:" + html);


    var liveHTML = HTML. "<div>\{ name }\{ foo }</div>\{ bar } and a num:\{ num }\n\n\{ div }" ;
    System.out.println(liveHTML.toParts());
    System.out.println(liveHTML);

  }
}
