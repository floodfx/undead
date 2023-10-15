package com.undead4j.template;

import static com.undead4j.template.Live.*;

public interface PageTemplate{
  public LiveTemplate render(
      PageTitleConfig pageTitleConfig,
      String csrfToken,
      LiveTemplate content
  );

  public default LiveTemplate liveTitle(PageTitleConfig pageTitleConfig) {
    return HTML."""
        <title
          \{HTML." data-prefix='\{pageTitleConfig.prefix()}'"}
          \{HTML." data-suffix='\{pageTitleConfig.suffix()}'"}
        >
          \{pageTitleConfig.title()}
        </title>
    """;
  }
}
