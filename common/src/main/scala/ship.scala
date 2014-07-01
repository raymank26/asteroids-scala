package my.game.pkg.ship

import my.game.pkg.utils.Implicits._
import my.game.pkg.utils.Utils._
import my.game.pkg.base_actor.{ActorInView, AcceleratableActor}

import com.badlogic.gdx.Gdx

import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.Action
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.math.Vector2

import scala.language.implicitConversions._

import scala.collection.JavaConversions._

import scala.math


class ShipMovingAction(val acc:Float, rotation: AcceleratableActor => Float) extends Action {
    override def act(delta: Float):Boolean = {
        def proj(angle:Float, value:Float): (Float, Float) = {
            (value * math.cos(angle), value * math.sin(angle))
        }

        val ship = actor match {
            case ship: AcceleratableActor => ship
            case _ => throw new ClassCastException
        }
        val old_velocity = ship.velocity
        if(acc < 0 && math.abs(old_velocity.x) < 0.01 && math.abs(old_velocity.y) < 0.01) {
            return true
        }
        val proj_a = proj(rotation(ship) * math.Pi / 180, acc)
        val new_velocity = new Vector2(old_velocity.x + proj_a._1 * delta,
            old_velocity.y + proj_a._2 * delta)

        val s_x = old_velocity.x * delta + (proj_a._1 * delta * delta) / 2
        val s_y = old_velocity.y * delta + (proj_a._2 * delta * delta) / 2
        ship.velocity = new_velocity
        ship.addAction(Actions.moveBy(-s_x, -s_y))
        true
    }
}

class Ship extends AcceleratableActor("ship.png") {

    val ANGLE: Float = 10
    val ACC: Float = 2.0f

    val actions = Array(
        Actions.forever(Actions.rotateBy(ANGLE)),
        Actions.forever(Actions.rotateBy(-ANGLE)),
        Actions.forever(new ShipMovingAction(ACC, ship => ship.getRotation))
    )
    def resetState() = {
        // def getAngle(ship: Ship):Float = {
        //     val l = ship.velocity.len()
        //     val angle = math.acos(ship.velocity.x.toFloat / l) * 180 / math.Pi
        //     println(angle)
        //     angle
        // }
        // addAction(new ShipMovingAction(-0.5f, getAngle))
    }

    resetState()

    addListener(new InputListener {
        override def keyDown(event: InputEvent, keycode: Int):Boolean = {
            // actions.map(_.reset())
            movement(keycode,
                makeBullet(),
                addAction(actions(0)),
                addAction(actions(1)),
                addAction(actions(2))
            )
            true
        }
        override def keyUp(event: InputEvent, keycode: Int):Boolean = {
            def replace_and_delete_action(i:Int) = {
                removeAction(actions(i))
                actions(i) = i match {
                    case 0 => Actions.forever(Actions.rotateBy(ANGLE))
                    case 1 => Actions.forever(Actions.rotateBy(-ANGLE))
                    case 2 => Actions.forever(new ShipMovingAction(ACC, ship => ship.getRotation))
                }
            }
            movement(keycode,
                (),
                replace_and_delete_action(0),
                replace_and_delete_action(1),
                replace_and_delete_action(2)
            )
            true
        }

    })

    def movement(keycode:Int, space: => Unit, left: => Unit, right: => Unit, up: => Unit) = {
        keycode match {
            case 21 => left
            case 22=> right
            case 19 => up
            case 62 => space
            case _ =>
        }
    }
    // override def act(delta: Float) {
    //     for(i <- getActions()) {
    //         i.act(delta)
    //     }
    //     super.act(delta)
    // }

    def makeBullet() {
        val stage = getStage()
        val velocity = new Vector2(-10, 0)
        velocity.rotate(degreesToRadians(getRotation))
        val bullet = new Bullet(velocity.rotate(getRotation), (this.getX(), this.getY()))
        stage.addActor(bullet)
    }
}


class Bullet(val velocity: Vector2, from: (Float, Float)) extends ActorInView("bullet.png") {
    val v1 = new Vector2(from._1, from._2)
    val v2 = new Vector2(0, 0)
    var overall:Float = 0

    setPosition(v1.x, v1.y)

    override def act(delta: Float) {
        // setPosition(v1.x, v1.y)
        moveBy(velocity.x, velocity.y)
        v2.x = getX()
        v2.y = getY()
        overall += velocity.len
        if(overall > 200) {
            remove()
            return
        }
        super.act(delta)

    }
}

