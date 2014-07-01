package my.game.pkg.stage

import my.game.pkg.asteroid.Asteroid
import my.game.pkg.ship.{Bullet, Ship}
import my.game.pkg.utils.Utils._

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport
import com.badlogic.gdx.scenes.scene2d.{Stage => S, Actor}

import scala.util.Random
import scala.math
import scala.language.implicitConversions._
import scala.collection.JavaConversions._

class Stage(viewport: Viewport) extends S(viewport) {
    spawn()

    def spawn() {
        val r = new Random()
        val width = getViewport.getViewportWidth()
        val height = getViewport.getViewportHeight()
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
            addActor(asteroid)

        }
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

            addActor(temp1)
            addActor(temp2)

        }
    }

    def detectCollisions() {
        val actors = getActors()
        var flag = false

        def checkOnAsteroids(bullet:Bullet, bullet_index:Int) = {
            var i = 0
            while(i < actors.size) {
                actors.get(i) match {
                    case a:Asteroid => if(bullet.collide(a)) {
                        cloneAsteroid(a, bullet)
                        bullet.remove()
                        a.remove()
                        flag = true
                    }
                    case _ => flag = false
                }
                if(flag) {
                    true
                }
                i += 1
            }
            false
        }
        var i = 0
        while(i < actors.size) {
            actors.get(i) match {
                case a:Bullet => if(checkOnAsteroids(a, i)) {
                    i = 0
                }
                case _ =>
            }
            i += 1
        }
    }
    override def act(delta:Float) {
        detectCollisions()
        super.act(delta)
    }
}
