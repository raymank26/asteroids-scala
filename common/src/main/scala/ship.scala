package my.game.pkg.actors

import my.game.pkg.utils.Implicits._
import my.game.pkg.utils.Utils._
import my.game.pkg.Settings
import my.game.pkg.base_actor.{ActorInView, AcceleratableActor}

import com.badlogic.gdx.Gdx

import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.{Action, InputEvent, InputListener}
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.utils.{Array => GArray}
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.Pixmap.Format.RGB888
import com.badlogic.gdx.utils.Timer
import com.badlogic.gdx.math.Circle

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

class BulletShot extends Action {

    var delta_sum: Float = 0
    var is_first_shot = true
    val delta_accum = 7 // larger will slower
    override def act(delta: Float) = {
        delta_sum += delta
        if(delta_sum > delta * delta_accum || is_first_shot) {
            is_first_shot = false
            delta_sum = 0
            val ship = actor match {
                case ship: Ship => ship
                case _ => throw new ClassCastException
            }
            ship.makeBullet()
            Settings.sounds("fire").play()

        }
        true
    }
}

class Ship extends AcceleratableActor("ship") {

    private val ANGLE: Float = 5
    private val ACC: Float = 2.0f

    private val frames: GArray[TextureRegion] = new GArray()
    private var stateTime = 0f

    frames.add(texture)
    // empty frame
    frames.add(new TextureRegion(new Texture(texture.getRegionWidth(),
        texture.getRegionHeight(),
        RGB888
        )))

    var is_immune = false
    private val inv_animation = new Animation(5f, frames)
    val actions = Array(
        Actions.forever(Actions.rotateBy(ANGLE)),
        Actions.forever(Actions.rotateBy(-ANGLE)),
        Actions.forever(new ShipMovingAction(ACC, ship => ship.getRotation)),
        Actions.forever(new BulletShot())
    )
    setImmunity(2)

    def setImmunity(time: Float) = {
        is_immune = true

        Timer.schedule(is_immune = false, time)


    }

    addListener(new InputListener {
        override def keyDown(event: InputEvent, keycode: Int):Boolean = {
            // actions.map(_.reset())
            movement(keycode,
                space=addAction(actions(3)),
                left=addAction(actions(0)),
                right=addAction(actions(1)),
                up=addAction(actions(2))
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
                    case 3 => Actions.forever(new BulletShot())
                }
            }
            movement(keycode,
                replace_and_delete_action(3),
                replace_and_delete_action(0),
                replace_and_delete_action(1),
                replace_and_delete_action(2)
            )
            true
        }

    })

    override def draw(batch:Batch, alpha:Float) = {
        if(is_immune) {
            stateTime += alpha
            val frame = inv_animation.getKeyFrame(stateTime, true)
            batch.draw(frame,this.getX(),getY(),this.getOriginX(),this.getOriginY(),this.getWidth(),
                this.getHeight(),this.getScaleX(), this.getScaleY(),this.getRotation())
        }
        else {
            super.draw(batch, alpha)
        }

    }

    def movement(keycode:Int, space: => Unit, left: => Unit, right: => Unit, up: => Unit) = {
        keycode match {
            case 21 => left
            case 22=> right
            case 19 => up
            case 62 => space
            case _ =>
        }
    }

    def makeBullet() {
        val stage = getStage()
        val velocity = new Vector2(-10, 0)
        velocity.rotate(getRotation)
        val bullet = new Bullet(velocity, (this.getX(), this.getY()))
        stage.addActor(bullet)
    }

    def drawInvulnerable() = {

    }
}


class Bullet(val velocity: Vector2, from: (Float, Float)) extends ActorInView("bullet") {
    private val v1 = new Vector2(from._1, from._2)
    private val v2 = new Vector2(0, 0)
    private var overall:Float = 0

    setPosition(v1.x, v1.y)

    override def act(delta: Float) {
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

