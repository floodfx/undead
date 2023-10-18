package com.undead4j.template;

import java.util.Map;

public interface WrapperTemplate {
  UndeadTemplate render(Map sessionData, UndeadTemplate content);
}
