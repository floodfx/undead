package com.undead4j.template;

import static com.undead4j.template.Live.HTML;

public interface PageTemplate {
  LiveTemplate render(
      PageTitleConfig pageTitleConfig,
      String csrfToken,
      LiveTemplate content
  );

  default LiveTemplate liveTitle(PageTitleConfig pageTitleConfig) {
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
