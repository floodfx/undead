package run.undead.javalin.example.view.tmpl;

import run.undead.template.Undead;
import run.undead.template.UndeadTemplate;

public class Tmpl {
  public static final UndeadTemplate volumeTemplate(Integer volume) {
    return Undead.HTML."""
        <div class="flex flex-col space-y-4 p-4 w-1/2">
            <h1 class="text-2xl">🎧 Volume Control</h1>

            <div class="alert alert-info">
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" class="stroke-current shrink-0 w-6 h-6"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
              <span>Use the keyboard arrow keys to control the volume.</span>
            </div>

            <div class="flex items-center border rounded-lg p-4 space-x-2">
              <progress class="progress progress-primary w-full h-[20px]" value="\{volume}" max="100"></progress><div>\{volume}%</div>
            </div>


            <div class="flex flex-col space-y-4">
              <h2 class="text-xl">Keyboard Controls</h2>
              <div class="flex items-center space-x-4" ud-window-keydown="key_update" ud-key="ArrowUp">
                <kbd class="kbd text-lg">&uarr;</kbd>
                <span>Max volume</span>
              </div>
              <div class="flex items-center space-x-4" ud-window-keydown="key_update" ud-key="ArrowDown">
                <kbd class="kbd text-lg">&darr;</kbd>
                <span>Silence</span>
              </div>
              <div class="flex items-center space-x-4" ud-window-keydown="key_update" ud-key="ArrowRight">
                <kbd class="kbd text-lg">&rarr;</kbd>
                <span>Step up</span>
              </div>
              <div class="flex items-center space-x-4" ud-window-keydown="key_update" ud-key="ArrowLeft">
                <kbd class="kbd text-lg">&larr;</kbd>
                <span>Step down</span>
              </div>
            </div>
          </div>
        """;
  }
}
