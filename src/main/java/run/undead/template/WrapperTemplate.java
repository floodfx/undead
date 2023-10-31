package run.undead.template;

import java.util.Map;

public interface WrapperTemplate {
  UndeadTemplate render(Map sessionData, UndeadTemplate content);
}
