package my.game.pkg.round
import my.game.pkg.asteroid.Asteroid
import my.game.pkg.ship.{Bullet, Ship}

import com.badlogic.gdx.scenes.scene2d.{Stage, Actor}
import com.badlogic.gdx.math.Vector2

import scala.util.Random

class Round(number:Int, score:Int, stage:Stage) {

    def start() {
        val ship = new Ship()
        val viewport = stage.getViewport()
        ship.setX(viewport.getViewportWidth() / 2)
        ship.setY(viewport.getViewportHeight() / 2)
        stage.addActor(ship)
        spawn
        stage.setKeyboardFocus(ship)
    }

    def spawn() {
        val r = new Random()
        val viewport = stage.getViewport()
        val width = viewport.getViewportWidth()
        val height = viewport.getViewportHeight()
        var x:Float = 0
        var y:Float = 0

        for(i <- 1 to 5) {
            x = r.nextInt(width)
            y = r.nextInt(height)
            var velocity = (math.abs(r.nextInt() % 2),
                    math.abs(r.nextInt() %2)) match {
                case (0, 0) => new Vector2(r.nextFloat(), r.nextFloat()).nor()
                case (0, 1) => new Vector2(r.nextFloat(), -r.nextFloat()).nor()
                case (1, 0) => new Vector2(-r.nextFloat(), r.nextFloat()).nor()
                case (1, 1) => new Vector2(-r.nextFloat(), -r.nextFloat()).nor()
            }
            var temp = new Asteroid(x, y, 1)
            var asteroid = new Asteroid(x, y, 1)
            asteroid.setVelocity(velocity)
            stage.addActor(asteroid)

        }
    }

}
