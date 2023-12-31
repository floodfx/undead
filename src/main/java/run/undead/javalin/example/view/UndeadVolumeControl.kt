package run.undead.javalin.example.view

import run.undead.context.Context
import run.undead.event.UndeadEvent
import run.undead.javalin.example.view.tmpl.Tmpl
import run.undead.template.UndeadTemplate
import run.undead.view.Meta
import run.undead.view.View
import kotlin.math.max
import kotlin.math.min

/**
 * UndeadVolumeControl is an Undead View that renders a volume control that updates
 * the volume when the user presses the arrow keys.
 */
class UndeadVolumeControl : View {
    var volume: Int = 30

    override fun handleEvent(context: Context?, event: UndeadEvent?) {
        when (event?.type()) {
            "key_update" -> {
                val key = event.data()["key"]
                when (key) {
                    "ArrowUp" -> volume = 100
                    "ArrowDown" -> volume = 0
                    "ArrowLeft" -> volume = max(0, volume - 10)
                    "ArrowRight" -> volume = min(100, volume + 10)
                }
            }
        }
    }

    // Can't use Java StringTemplates in Kotlin so load this from a Java class
    override fun render(meta: Meta?): UndeadTemplate {
        return Tmpl.volumeTemplate(volume)
    }
}