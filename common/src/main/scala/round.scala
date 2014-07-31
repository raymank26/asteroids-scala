package my.game.pkg.round

import my.game.pkg.screens.GameScreen
import my.game.pkg.actors.{Asteroid, Bullet, Ship, Bang, Ufo, UfoBullet}
import my.game.pkg.actors.AsteroidSize._
import my.game.pkg.base_actor.ActorInView
import my.game.pkg.utils.Implicits._
import my.game.pkg.Settings
import my.game.pkg.utils.Utils._

import com.badlogic.gdx.scenes.scene2d.{Actor, Stage}
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.graphics.{Color, Texture}
import com.badlogic.gdx.utils.Timer


import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.{Table, Label, Skin, TextButton, Image}

import scala.util.Random

import scala.language.implicitConversions._
import scala.collection.JavaConversions._

case class RoundState(var score:Int, var hearts:Int, var number:Int)

class Round(state:RoundState, screen:GameScreen, stage: Stage) {
    private var score: Label = _
    private var hearts: Label = _
    private var ship: Ship = _

    private var in_game = false
    private var isUfoExist = false


    def cleanupRound() = {
        Timer.instance.clear
        in_game = false
        isUfoExist = false
        stage.clear()
        Settings.sounds("saucerBig").stop()

    }

    def splash() {
        cleanupRound()
        val labelStyle = new LabelStyle(Settings.font_gen(40), Color.WHITE)
        val viewport = stage.getViewport()
        val welcome = new Label(s"ROUND ${state.number}", labelStyle)
        stage.addActor(welcome)
        welcome.setX(viewport.getViewportWidth() / 2 - welcome.getWidth() / 2)
        welcome.setY(viewport.getViewportHeight() / 2)

        Timer.schedule(start, 2)
        Timer.schedule(isEnd, 3, 3)

    }
    def makeUfo() {
        if(!in_game && isUfoExist) {
            return
        }
        if(isUfoExist == false) { // will UFO be first at stage?
            Settings.sounds("saucerBig").loop()
        }
        isUfoExist = true
        val velocity = new Vector2(1f, 1f).rotate(Random.nextInt() % 360)
        val ufo = new Ufo(velocity)
        val viewport = stage.getViewport()
        val (width, height) = (viewport.getViewportWidth(), viewport.getViewportHeight())
        ufo.setX(Random.nextInt() % width)
        ufo.setY(Random.nextInt() % height)
        stage.addActor(ufo)

    }

    def start() {
        stage.clear()
        in_game = true
        ship = new Ship()
        val viewport = stage.getViewport()
        val width = viewport.getViewportWidth()
        val height = viewport.getViewportHeight()

        ship.setX(width / 2)
        ship.setY(height / 2)

        stage.addActor(ship)
        spawn_asteroids
        stage.setKeyboardFocus(ship)

        val labelStyle = new LabelStyle(Settings.font_gen(20), Color.WHITE)
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
        Timer.schedule(makeUfo, 10, 10)



    }

    def spawn_asteroids() {
        val r = new Random()
        val viewport = stage.getViewport()
        val width = viewport.getViewportWidth()
        val height = viewport.getViewportHeight()
        var x:Float = 0
        var y:Float = 0

        for(i <- 1 to state.number) {
            x = r.nextInt(width)
            y = r.nextInt(height)
            var velocity = (math.abs(r.nextInt() % 2),
                    math.abs(r.nextInt() %2)) match {
                case (0, 0) => new Vector2(r.nextFloat(), r.nextFloat()).nor()
                case (0, 1) => new Vector2(r.nextFloat(), -r.nextFloat()).nor()
                case (1, 0) => new Vector2(-r.nextFloat(), r.nextFloat()).nor()
                case (1, 1) => new Vector2(-r.nextFloat(), -r.nextFloat()).nor()
            }
            var asteroid = new Asteroid(x, y, Big)
            asteroid.setVelocity(velocity)
            stage.addActor(asteroid)

        }
    }
    def incrementScore[T](value: T) = {
        state.score += (value match {
            case a:Asteroid => a.getScore()
            case a:RoundState => a.number * 1000
            case a:Ufo => 1000
            case _ => 0 // TODO: make exception
        })
        score.setText(state.score.toString)
    }

    def place_ship(decrement_heart: Boolean) = {
        if(decrement_heart) {
            state.hearts -= 1
            hearts.setText(state.hearts.toString)
        }
        if (state.hearts == - 1) {
            screen.game.showGameOver(state.score)
        }
        val viewport = stage.getViewport()
        val width = viewport.getViewportWidth()
        val height = viewport.getViewportHeight()

        ship.setX(width / 2)
        ship.setY(height / 2)
        ship.setImmunity(2)

        Settings.sounds("die").play()

    }

    def bangAsteroid(asteroid: Asteroid, velocity:Vector2) {
        asteroid.size match {
            case Big => Settings.sounds("bangLarge").play()
            case Medium => Settings.sounds("bangMedium").play()
            case Small => Settings.sounds("bangSmall").play()
        }
        val x = asteroid.getX()
        val y = asteroid.getY()
        if(asteroid.size != Small) {
            val v = makeCollinear(velocity)
            val size = asteroid.size match {
                case Big => Medium
                case Medium => Small
            }
            var temp1 = new Asteroid(x-10, y-10, size)
            temp1.setVelocity(v._1.x, v._1.y)
            var temp2 = new Asteroid(x+10, y+10, size)
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

    def isEnd() {
        if(in_game) {
            val asteroids_count = stage.getActors.items.filter((a:Actor) => a match {
                case actor:Asteroid => true
                case _ => false
                }).size
            if(asteroids_count == 0) {
                incrementScore(state)
                state.number += 1
                splash()
            }

        }

    }

    def detectCollisions() {
        val actors = stage.getActors()

        def removeUfo(ufo: Ufo) {
            ufo.remove()
            var ufo_count = stage.getActors().filter(
                (a:Actor) => a match {
                    case ufo: Ufo => true
                    case _ => false
            }).size
            if(ufo_count == 0) {
                Settings.sounds("saucerBig").stop()
            }
            isUfoExist = false

        }

        var i = 0
        while(i < actors.size - 1) {
            var j = i + 1
            while (j < actors.size) {
                val expr = (actors.get(i), actors.get(j)) match {
                    case (asteroid:Asteroid, bullet:Bullet) => (bullet, asteroid)
                    case (asteroid:Asteroid, ship:Ship) => (ship, asteroid)
                    case (a:UfoBullet, b:Ship) => (b, a)
                    case (a:Asteroid, b:Ufo) => (b, a)
                    case (a:Ufo, b:Bullet) => (b, a)
                    case otherwise => otherwise
                }
                expr match {
                    case (bullet:Bullet, asteroid:Asteroid) if bullet.collide(asteroid) => {
                        bangAsteroid(asteroid, bullet.velocity)
                        incrementScore(asteroid)
                        stage.addActor(Bang(asteroid.getX(), asteroid.getY()))
                        bullet.remove()
                        asteroid.remove()
                    }
                    case (ship:Ship, asteroid:Asteroid)
                            if ship.collide(asteroid) && !ship.is_immune => {
                        stage.addActor(Bang(ship.getX(), ship.getY()))
                        place_ship(decrement_heart=true)
                    }
                    case (ship:Ship, bullet: UfoBullet)
                            if ship.collide(bullet) && !ship.is_immune => {
                        stage.addActor(Bang(ship.getX(), ship.getY()))
                        bullet.remove()
                        place_ship(decrement_heart=true)
                    }
                    case (ufo: Ufo, asteroid:Asteroid)
                            if ufo.collide(asteroid) => {
                        removeUfo(ufo)
                        stage.addActor(Bang(ufo.getX(), ufo.getY()))
                        bangAsteroid(asteroid, ufo.velocity)
                        asteroid.remove()
                    }
                    case (bullet: UfoBullet, _) => Unit
                    case (bullet: Bullet, ufo:Ufo) if ufo.collide(bullet) => {
                        Settings.sounds("bangMedium").play()
                        removeUfo(ufo)
                        bullet.remove()
                        stage.addActor(Bang(ufo.getX(), ufo.getY()))
                    }
                    case _ =>

                }
                j += 1
            }
            i += 1
        }
    }

}
