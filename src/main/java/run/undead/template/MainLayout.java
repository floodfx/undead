package run.undead.template;

import run.undead.view.View;

import static run.undead.template.Directive.HTML;
import static run.undead.template.Directive.NoEscape;

/**
 * MainLayout wraps all Undead {@link View} with a common layout and
 * typically contains HTML head and body tags including any css, javascript, and other
 * static assets.  The default implementation loads the Undead javascript, TailwindCSS,
 * and DaisyUI.
 * See {@link #render} for more information on how to customize the default layout.
 */
public interface MainLayout {

  /**
   * render wraps the given content with a common layout and typically contains HTML head and body tags including any
   * css, javascript, and other static assets.
   * @param pageTitle the title of the page (see {@link PageTitle})
   * @param csrfToken
   * @param content
   * @return
   */
  default public UndeadTemplate render(
      PageTitle pageTitle,
      String csrfToken,
      UndeadTemplate content
  ) {
    return HTML. """
      <!DOCTYPE html>
      <html lang="en">
        <head>
          <meta charset="utf-8" />
          <meta http-equiv="X-UA-Compatible" content="IE=edge" />
          <meta name="viewport" content="width=device-width, initial-scale=1.0" />
          <meta name="csrf-token" content="\{ csrfToken }" />
          \{ this.liveTitle(pageTitle) }
          <script defer type="text/javascript" src="/js/index.js"></script>
          <!-- DaisyUI + Tailwind CSS: we recommend replacing this with your own CSS/Components -->
          <link href="https://cdn.jsdelivr.net/npm/daisyui@3.9.2/dist/full.css" rel="stylesheet" type="text/css" />
          <script src="https://cdn.tailwindcss.com"></script>
        </head>
        <body>
          <div class="navbar bg-base-100 mb-4 border-b">
            <a class="btn btn-ghost normal-case text-xl">🧟Undead</a>
          </div>
          <!-- Embedded LiveView -->
          \{ NoEscape(content) }
        </body>
      </html>
     """ ;
  }

  default UndeadTemplate liveTitle(PageTitle pageTitle) {
    return HTML. """
        <title
          \{ HTML. " data-prefix='\{ pageTitle.prefix() }'" }
          \{ HTML. " data-suffix='\{ pageTitle.suffix() }'" }
        >
          \{ pageTitle.title() }
        </title>
    """ ;
  }
}
