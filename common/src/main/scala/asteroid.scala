package my.game.pkg.actors

import my.game.pkg.base_actor.AcceleratableActor
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.actions.Actions
// import scala.utils.Random

object AsteroidSize extends Enumeration {
    type AsteroidSize = Value
    val Big, Medium, Small = Value
}
import AsteroidSize._

class Asteroid(val xPos:Float, val yPos:Float, val size:AsteroidSize) extends AcceleratableActor("asteroid") {
    val scale = size match {
        case AsteroidSize.Big => 1.0f
        case AsteroidSize.Medium => 1.0f / 2
        case AsteroidSize.Small => 1.0f / 4
    }

    val SCALE_FACTOR = 2
    setPosition(xPos, yPos)
    val adjusted_scale = scale * SCALE_FACTOR
    setScale(adjusted_scale)
    // updateBounds(0, 0, 100, 100)

    addAction(Actions.forever(Actions.rotateBy(1)))
    override def act(delta:Float) {
        super.act(delta)
    }

    def getScore() = (100 * scale).toInt

}
