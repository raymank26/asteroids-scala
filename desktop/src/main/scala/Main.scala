package my.game.pkg

import com.badlogic.gdx.backends.lwjgl._

object Main extends App {
    val cfg = new LwjglApplicationConfiguration
    cfg.title = "asteroids-example"
    cfg.height = 480
    cfg.width = 320
    // cfg.useGL20 = true
    cfg.forceExit = false
    new LwjglApplication(new Asteroidsexample, cfg)
}
