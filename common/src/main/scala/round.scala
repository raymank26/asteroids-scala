package my.game.pkg.round
import my.game.pkg.asteroid.Asteroid
import my.game.pkg.ship.{Bullet, Ship}
import my.game.pkg.stage.Stage
import my.game.pkg.base_actor.ActorInView
import my.game.pkg.utils.Implicits._

import com.badlogic.gdx.scenes.scene2d.{Actor}
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.graphics.{Color, Texture}
import com.badlogic.gdx.utils.Timer


import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.{Table, Label, Skin, TextButton, Image}

import scala.util.Random

case class RoundState(val score:Int, val lives:Int, val number:Int)

class Round(state:RoundState, stage:Stage) {

    def splash() {
        start()
        return;
        val labelStyle = new LabelStyle(stage.font_gen(40), Color.WHITE)
        val viewport = stage.getViewport()
        val welcome = new Label("ROUND 1", labelStyle)
        stage.addActor(welcome)
        welcome.setX(viewport.getViewportWidth() / 2 - welcome.getWidth() / 2)
        welcome.setY(viewport.getViewportHeight() / 2)

        Timer.schedule(new Timer.Task {
            override def run () {
                start()
            }
            }, 2)

    }

    def start() {
        stage.clear()
        val ship = new Ship()
        val viewport = stage.getViewport()
        val width = viewport.getViewportWidth()
        val height = viewport.getViewportHeight()

        ship.setX(width / 2)
        ship.setY(height / 2)

        stage.addActor(ship)
        spawn
        stage.setKeyboardFocus(ship)

        val labelStyle = new LabelStyle(stage.font_gen(20), Color.WHITE)
        val score = new Label("10000", labelStyle)
        val skin = new Skin()
        skin.add("heart", new Texture("heart.png"))
        val dashboard = new Table()

        val heart = new Image(skin.getDrawable("heart"))
        val lives = new Label("3", labelStyle)
        dashboard.setFillParent(true);

        dashboard.add(heart)
        dashboard.add(lives)
        dashboard.add(score).expandX().right()
        dashboard.top().left()


        stage.addActor(dashboard)



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
