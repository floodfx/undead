package com.undead4j.template;

import static com.undead4j.template.Undead.HTML;

public interface PageTemplate {
  UndeadTemplate render(
      PageTitleConfig pageTitleConfig,
      String csrfToken,
      UndeadTemplate content
  );

  default UndeadTemplate liveTitle(PageTitleConfig pageTitleConfig) {
    return HTML. """
        <title
          \{ HTML. " data-prefix='\{ pageTitleConfig.prefix() }'" }
          \{ HTML. " data-suffix='\{ pageTitleConfig.suffix() }'" }
        >
          \{ pageTitleConfig.title() }
        </title>
    """ ;
  }
}
