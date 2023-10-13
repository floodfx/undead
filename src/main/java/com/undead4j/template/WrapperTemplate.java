package com.undead4j.template;

import java.util.Map;

public interface WrapperTemplate{
  public LiveTemplate render(Map sessionData, LiveTemplate content);
}
