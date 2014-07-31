package my.game.pkg.actors

// import my.game.pkg.utils.Utils._
import my.game.pkg.utils.Implicits._

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.{Animation, TextureRegion, Batch}
import com.badlogic.gdx.graphics.{Texture}
// import com.badlogic.gdx.utils.{Array => GArray}

class Bang private(val animation: Animation) extends Actor {
    var stateTime = 0f

    override def draw(batch:Batch, alpha:Float) {
        val frame = animation.getKeyFrame(stateTime)
        if(animation.isAnimationFinished(stateTime)) {
            remove()
        }
        else {
            stateTime += alpha
            batch.draw(frame,this.getX(),getY())
        }
    }

}

object Bang {

    def apply(x: Float, y:Float ) = {
        val texture = new Texture(Gdx.files.internal("bang.png"))
        val frames = TextureRegion.split(texture, 32, 32).flatten
        val animation = new Animation(3f, frames)
        val b = new Bang(animation)
        b.setX(x)
        b.setY(y)
        b

    }
}
