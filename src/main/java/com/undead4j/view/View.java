package com.undead4j.view;

import com.undead4j.event.UndeadEvent;
import com.undead4j.event.UndeadInfo;
import com.undead4j.socket.Socket;
import com.undead4j.template.LiveTemplate;

import java.util.Map;

public interface View {
  public default void mount(Socket socket, Map sessionData, Map params) {
    // empty implementation is ok
  }
  public default void handleEvent(Socket socket, UndeadEvent event){
    // if we get an event, tell the developer they need to implement this
    throw new RuntimeException("Implement handleEvent in your view");
  }
  public default void handleParams(Socket socket){
    // empty implementation is ok
  }
  public default void handleInfo(Socket socket, UndeadInfo info) {
    // if we get an info, tell the developer they need to implement this
    throw new RuntimeException("Implement handleInfo in your view");
  }
  public LiveTemplate render(Meta meta);
  public default void shutdown(){
    // empty implementation is ok
  }
}

