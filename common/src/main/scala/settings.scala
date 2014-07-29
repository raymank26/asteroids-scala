package my.game.pkg

import com.badlogic.gdx.graphics.g2d.freetype.{FreeTypeFontGenerator => FreeGen}
import com.badlogic.gdx.Gdx


object Settings {

    private val generator = new FreeGen(
        Gdx.files.internal("Orbitron-Medium.ttf"));

    def font_gen(size:Int) = {
        val par = new FreeGen.FreeTypeFontParameter()
        par.size = size
        generator.generateFont(par)
    }

    lazy val sounds = Map(
        "fire" -> Gdx.audio.newSound(Gdx.files.internal("sounds/fire.wav")),
        "bangLarge" -> Gdx.audio.newSound(Gdx.files.internal("sounds/bangLarge.wav")),
        "bangMedium" -> Gdx.audio.newSound(Gdx.files.internal("sounds/bangMedium.wav")),
        "bangSmall" -> Gdx.audio.newSound(Gdx.files.internal("sounds/bangSmall.wav")),
        "background" -> Gdx.audio.newSound(Gdx.files.internal("sounds/background.wav")),
        "die" -> Gdx.audio.newSound(Gdx.files.internal("sounds/die.wav"))
        )
}
