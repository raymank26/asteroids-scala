package my.game.pkg.round

import my.game.pkg.screens.GameScreen
import my.game.pkg.asteroid.Asteroid
import my.game.pkg.ship.{Bullet, Ship}
import my.game.pkg.base_actor.ActorInView
import my.game.pkg.utils.Implicits._
import my.game.pkg.Settings.font_gen
import my.game.pkg.utils.Utils._

import com.badlogic.gdx.scenes.scene2d.{Actor, Stage}
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.graphics.{Color, Texture}
import com.badlogic.gdx.utils.Timer


import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.{Table, Label, Skin, TextButton, Image}

import scala.util.Random

case class RoundState(var score:Int, var hearts:Int, var number:Int)

class Round(state:RoundState, screen:GameScreen, stage: Stage) {
    private var score: Label = _
    private var hearts: Label = _
    private var ship: Ship = _

    def splash() {
        // start()
        // return;
        val labelStyle = new LabelStyle(font_gen(40), Color.WHITE)
        val viewport = stage.getViewport()
        val welcome = new Label("ROUND 1", labelStyle)
        stage.addActor(welcome)
        welcome.setX(viewport.getViewportWidth() / 2 - welcome.getWidth() / 2)
        welcome.setY(viewport.getViewportHeight() / 2)

        Timer.schedule(start, 2)

    }

    def start() {
        stage.clear()
        ship = new Ship()
        val viewport = stage.getViewport()
        val width = viewport.getViewportWidth()
        val height = viewport.getViewportHeight()

        ship.setX(width / 2)
        ship.setY(height / 2)

        stage.addActor(ship)
        spawn_asteroids
        stage.setKeyboardFocus(ship)

        val labelStyle = new LabelStyle(font_gen(20), Color.WHITE)
        score = new Label(state.score.toString, labelStyle)
        val skin = new Skin()
        skin.add("heart", new Texture("heart.png"))
        val dashboard = new Table()

        val heart = new Image(skin.getDrawable("heart"))
        hearts = new Label(state.hearts.toString, labelStyle)
        dashboard.setFillParent(true);

        dashboard.add(heart).padLeft(5).padRight(3)
        dashboard.add(hearts)
        dashboard.add(score).expandX().right().padRight(5)
        dashboard.top().left()


        stage.addActor(dashboard)



    }

    def spawn_asteroids() {
        val r = new Random()
        val viewport = stage.getViewport()
        val width = viewport.getViewportWidth()
        val height = viewport.getViewportHeight()
        var x:Float = 0
        var y:Float = 0

        for(i <- 1 to 2) {
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
    def incrementScore(value: Int) = {
        state.score += value
        score.setText(state.score.toString)
    }

    def place_ship(decrement_heart: Boolean) = {
        if(decrement_heart) {
            state.hearts -= 1
            hearts.setText(state.hearts.toString)
        }
        val viewport = stage.getViewport()
        val width = viewport.getViewportWidth()
        val height = viewport.getViewportHeight()

        ship.setX(width / 2)
        ship.setY(height / 2)
        ship.setImmunity(2)

    }

    def cloneAsteroid(asteroid: Asteroid, bullet:Bullet) {
        val x = asteroid.getX()
        val y = asteroid.getY()
        if(asteroid.adjusted_scale > 1.0/4) {
            val v = makeCollinear(bullet.velocity)
            val scale = asteroid.scale / 2
            var temp1 = new Asteroid(x-10, y-10, scale)
            temp1.setVelocity(v._1.x, v._1.y)
            var temp2 = new Asteroid(x+10, y+10, scale)
            temp2.setVelocity(v._2.x, v._2.y)
            require(temp1.getScaleX() == temp1.getScaleX())
            require(temp2.getScaleY() == temp2.getScaleY())

            stage.addActor(temp1)
            stage.addActor(temp2)

        }
    }

    def act {
        detectCollisions
    }

    def detectCollisions() {
        val actors = stage.getActors()

        var i = 0
        while(i < actors.size - 1) {
            var j = i + 1
            while (j < actors.size) {
                val expr = (actors.get(i), actors.get(j)) match {
                    case (asteroid:Asteroid, bullet:Bullet) => (bullet, asteroid)
                    case (asteroid:Asteroid, ship:Ship) => {
                         (ship, asteroid)
                     }
                    case otherwise => otherwise
                }
                expr match {
                    case (bullet:Bullet, asteroid:Asteroid) if bullet.collide(asteroid) => {
                        cloneAsteroid(asteroid, bullet)
                        incrementScore(asteroid.getScore)
                        bullet.remove()
                        asteroid.remove()
                    }
                    case (ship:Ship, asteroid:Asteroid)
                            if ship.collide(asteroid) && !ship.is_immune => {
                        place_ship(decrement_heart=true)
                    }
                    case _ =>

                }
                j += 1
            }
            i += 1
        }
    }

}
