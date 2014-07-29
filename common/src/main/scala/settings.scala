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
}
