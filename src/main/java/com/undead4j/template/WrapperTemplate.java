package com.undead4j.template;

import java.util.Map;

public interface WrapperTemplate {
  LiveTemplate render(Map sessionData, LiveTemplate content);
}
