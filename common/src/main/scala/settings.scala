package my.game.pkg

import com.badlogic.gdx.graphics.g2d.freetype._
import com.badlogic.gdx.Gdx

object Settings {

    val generator = new FreeTypeFontGenerator(
        Gdx.files.internal("Orbitron-Medium.ttf"));
    val font_gen = (i:Int) => generator.generateFont(i);
}
