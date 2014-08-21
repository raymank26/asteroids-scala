package my.game.pkg

import com.badlogic.gdx.graphics.g2d.freetype.{FreeTypeFontGenerator => FreeGen}
import com.badlogic.gdx.Gdx

import com.badlogic.gdx.scenes.scene2d.ui._
import com.badlogic.gdx.graphics.{Color, Texture, Pixmap}
import com.badlogic.gdx.graphics.g2d._
import com.badlogic.gdx.scenes.scene2d.utils._
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureAtlas
// import dispatch._

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
        "die" -> Gdx.audio.newSound(Gdx.files.internal("sounds/die.wav")),
        "saucerBig" -> Gdx.audio.newSound(Gdx.files.internal("sounds/saucerBig.wav"))
        )

    // val backend_host = host("localhost", 5000)
    val backend_host_new = "http://localhost:5000"
    lazy val atlas = new TextureAtlas(Gdx.files.internal("result.atlas"))

    val skin = {
        val skin = new Skin(atlas)
        skin.add("font20", font_gen(20))
        skin.add("font30", font_gen(30))
        skin.add("font40", font_gen(40))

        val pixmap = new Pixmap(1, 1, Pixmap.Format.RGB888)
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white_pixmap", new Texture(pixmap))

        skin.load(Gdx.files.internal("skin.json"))

        skin
    }


}
