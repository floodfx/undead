package com.undead4j.view;

import com.undead4j.template.LiveTemplate;

public interface View<Context> {
  public default void mount() {}
  public default void handleEvent(){}
  public default void handleParams(){}
  public default void handleInfo(){}
  public LiveTemplate render(Context ctx, Meta meta);
  public default void shutdown(){}
}

