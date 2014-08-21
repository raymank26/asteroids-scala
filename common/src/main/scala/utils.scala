package my.game.pkg.utils

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import scala.language.implicitConversions
import com.badlogic.gdx.utils.{Timer, Array => GArray}
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent

import scala.math

class SVector2(x:Float, y:Float) extends Vector2(x, y) {

    def +(b:Vector2):Vector2 = {
        add(b)
    }

    def *(b:Float):Vector2 = {
        scl(b)

    }
    def *(b:Vector2):Vector2 = {
        scl(b)
    }
}

object SVector2 {
    implicit def Vector2SVector(that:Vector2):SVector2 = new SVector2(that.x, that.y)
}


object Implicits {
    implicit def doubleToFloat(d: Double): Float = d.toFloat

    implicit def runMethodToTask(r: => Unit): Timer.Task = {
        new Timer.Task {
            override def run () = r
        }
    }

    implicit def scalaArrayToGdxArray[T](a:Array[T]): GArray[T] = {
        val res = new GArray[T]()
        for(element <- a) {
            res.add(element)
        }
        res
    }

    implicit def listenerMethod(r: => Unit): ChangeListener = {
        new ChangeListener() {
            def changed(event: ChangeEvent, actor:Actor) {
                r
            }
        }
    }


}

object Utils {
    def degreesToRadians(d:Float) = d * math.Pi / 180
    def radiansToDegrees(d:Float) = d * 180 / math.Pi

    def makeCollinear(a: Vector2) = {
        var res1 = new Vector2()
        var res2 = new Vector2()
        (a.x, a.y) match {
            case (_, 0) => {
                res1.set(0, 1)
                res2.set(0, -1)
            }
            case (0, _) => {
                res1.set(1, 0)
                res2.set(-1, 0)
            }
            case (_, _) => {
                res1.set(1, -a.x / a.y)
                res2.set(-1, a.x / a.y)
            }
        }
        (res1.nor(), res2.nor())
    }
}
