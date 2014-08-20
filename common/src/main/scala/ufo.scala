package my.game.pkg.actors

import my.game.pkg.base_actor.{ActorInView, AcceleratableActor}
import my.game.pkg.actors.{Bullet, Ship}
import my.game.pkg.utils.Implicits._
import my.game.pkg.Settings
import com.badlogic.gdx.scenes.scene2d.Actor

import com.badlogic.gdx.math.Vector2

class Ufo(val velocity: Vector2) extends ActorInView("ufo") {
    var interval_time = 0f
    val shooting_interval = 1f

    override def act(delta: Float) {
        interval_time += delta
        if(interval_time > shooting_interval) {
            val vector:Vector2 = {
                (getStage().getActors().items.find(((a: Actor) => a match {
                    case _:Ship => true
                    case _ => false
                    }))) match {
                        case Some(ship) => new Vector2(ship.getX() - getX(), ship.getY() - getY()).nor
                        case _ => throw new ClassCastException
                }
            }
            getStage().addActor(new UfoBullet(vector, (getX(), getY())))
            interval_time = 0
            Settings.sounds("fire").play()
        }
        moveBy(velocity.x, velocity.y)
        super.act(delta)
    }

}

class UfoBullet(val v: Vector2, from: (Float, Float)) extends Bullet(v, from)

// object Ufo {
//     def apply() {

//     }
// }
