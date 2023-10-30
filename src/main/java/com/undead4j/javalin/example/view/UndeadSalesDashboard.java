package com.undead4j.javalin.example.view;

import com.undead4j.event.SimpleUndeadInfo;
import com.undead4j.event.UndeadEvent;
import com.undead4j.event.UndeadInfo;
import com.undead4j.socket.Socket;
import com.undead4j.template.Directive;
import com.undead4j.template.UndeadTemplate;
import com.undead4j.view.Meta;
import com.undead4j.view.View;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * UndeadSalesDashboard is a simple example of a liveview that displays some stats that
 * refresh every second.
 */
public class UndeadSalesDashboard implements View {
  private int newOrders;
  private BigDecimal salesAmount;
  private int rating;
  private Timer timer;

  public UndeadSalesDashboard() {
    this.doRefresh();
  }

  @Override
  public void mount(Socket socket, Map sessionData, Map params) {
    // start timer task if we're connected (i.e. not http request)
    if (socket.connected()) {
      this.timer = new Timer();
      this.timer.schedule(new TimerTask() {
        public void run() {
          socket.sendInfo(new SimpleUndeadInfo("refresh", null));
        }
      }, 0, 1000);
    }
  }

  @Override
  public void handleInfo(Socket socket, UndeadInfo info) {
    if (info.type().equals("refresh")) {
      this.doRefresh();
    }
  }

  @Override
  public void handleEvent(Socket socket, UndeadEvent event) {
    if (event.type().equals("refresh")) {
      this.doRefresh();
    }
  }

  private void doRefresh() {
    this.newOrders = randomNewOrders();
    this.salesAmount = randomPrice();
    this.rating = randomRating();
  }

  @Override
  public UndeadTemplate render(Meta meta) {
    return Directive.HTML. """
      <div class="flex flex-col mx-4 space-y-4">
        <div class="stats stats-vertical md:stats-horizontal shadow">

          <div class="stat">
            <div class="stat-title">🥡 New Orders</div>
            <div class="stat-value">\{ newOrders }</div>
          </div>

          <div class="stat">
            <div class="stat-title">💰 Sales Amount</div>
            <div class="stat-value">$\{ (salesAmount) }</div>
          </div>

          <div class="stat">
            <div class="stat-title">🌟 Rating</div>
            <div class="stat-value">\{ ratingToStars(rating) }</div>
          </div>

        </div>
        <div>
        <button class="btn btn-primary" ud-click="refresh">↻ Refresh</button>
        </div>
      </div>
        """ ;
  }

  private String ratingToStars(int rating) {
    String stars = "";
    var i = 0;
    for (; i < rating; i++) {
      stars += "⭐";
    }
    for (; i < 5; i++) {
      stars += "☆";
    }
    return stars;
  }

  private int randomNewOrders() {
    return (int) (Math.random() * 100);
  }

  private BigDecimal randomPrice() {
    return BigDecimal.valueOf(Math.random() * 100).setScale(2, RoundingMode.HALF_UP);
  }

  private int randomRating() {
    return (int) (Math.random() * 5);
  }

  @Override
  public void shutdown() {
    this.timer.cancel();
  }
}
