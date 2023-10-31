package run.undead.javalin.example.view.tags;

import run.undead.template.Undead;
import run.undead.template.UndeadTemplate;

public class ExCard {
  public final String title;
  public final String body;
  public final String href;

  public ExCard(String title, String body, String href) {
    this.title = title;
    this.body = body;
    this.href = href;
  }

  public UndeadTemplate render() {
    return Undead.HTML."""
        <div class="card w-96 bg-primary text-primary-content">
          <div class="card-body">
            <h2 class="card-title">\{title}</h2>
            <p>\{body}</p>
            <div class="card-actions justify-end">
              <a href="\{href}" class="btn">Go</a>
            </div>
          </div>
        </div>
        """;
  }
}
