package run.undead.config;

import run.undead.template.MainLayout;
import run.undead.template.PageTitle;
import run.undead.template.UndeadTemplate;
import run.undead.template.WrapperTemplate;
import run.undead.view.RouteMatcher;
import run.undead.view.View;

import java.util.function.Consumer;

/**
 * Config contains the configuration that is used across all Undead Views. At a minimum, you must
 * provide a {@link RouteMatcher} to match HTTP requests to Undead {@link View}s.
 *
 * See {@link MainLayout} for more information on how to customize the default layout.
 *
 */
public class Config {

  public MainLayout mainLayout;
  public WrapperTemplate wrapperTemplate;
  public RouteMatcher routeMatcher;

  public Consumer<String> debug;

  public Config() {
    // use the default main layout
    this.mainLayout = new MainLayout() {
      @Override
      public UndeadTemplate render(PageTitle pageTitle, String csrfToken, UndeadTemplate content) {
        return MainLayout.super.render(pageTitle, csrfToken, content);
      }
    };
  }

}
