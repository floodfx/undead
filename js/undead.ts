import { Socket } from "phoenix";
import { LiveSocket } from "phoenix_live_view";
import topbar from "topbar";

// LiveView
let csrfToken = document.querySelector("meta[name='csrf-token']")?.getAttribute("content");
let liveSocket = new LiveSocket("/live", Socket, {
  params: { _csrf_token: csrfToken },
  bindingPrefix: "ud-"
});

// Show progress bar on live navigation and form submits
topbar.config({ barColors: { 0: "#FF0000" }, shadowColor: "rgba(0, 0, 0, .3)" });
window.addEventListener("phx:page-loading-start", () => topbar.show(200));
window.addEventListener("phx:page-loading-stop", () => topbar.hide());

// connect if there are any LiveViews on the page
liveSocket.connect();

// add event listener for generic js-exec events from server
// this works by adding a data attribute to the element with the js to execute
// and then triggering a custom event with the selector to find the element
// see: https://fly.io/phoenix-files/server-triggered-js/
window.addEventListener("phx:js-exec", (e: Event) => {
  const detail = (e as CustomEvent).detail;
  document.querySelectorAll(detail.to).forEach(el => {
      liveSocket.execJS(el, el.getAttribute(detail.attr))
  })
})

// expose liveSocket on window for web console debug logs and latency simulation:
// >> liveSocket.enableDebug()
// >> liveSocket.enableLatencySim(1000)  // enabled for duration of browser session
// >> liveSocket.disableLatencySim()
// @ts-ignore
export { liveSocket };
