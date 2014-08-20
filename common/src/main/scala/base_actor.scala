package my.game.pkg.base_actor

import my.game.pkg.Settings

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.math.Circle
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Pixmap.Format
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Action

import scala.language.implicitConversions._
import my.game.pkg.utils.Implicits._
import scala.collection.JavaConversions._

class ActorInView(val texture_name:String) extends Actor {
    protected val bounds = new Circle()
    // protected val texture = new Texture(Gdx.files.internal(texture_path))
    protected val texture = Settings.atlas.findRegion(texture_name)

    // bounds.set( / 2, texture.getWidth() / 2, texture.getWidth() / 2)

    updateBounds()
    protected var bound_texture = makeBoundTexture()

    setBounds(0,0,texture.getRegionWidth(), texture.getRegionHeight())
    setOrigin(getWidth()/2, getHeight()/2);

    override def act(delta: Float) {
        // updateBounds(getX(),getY(),texture.getWidth() / 2)
        updateBounds()

        bound_texture.dispose()
        bound_texture = makeBoundTexture()

        if(getX() > getStage().getViewport().getViewportWidth()) {
            setX(0)
        }
        if(getX() < 0) {
            setX(getStage().getViewport().getViewportWidth())
        }
        if(getY() < 0) {
            setY(getStage().getViewport().getViewportHeight())
        }
        if(getY() > getStage().getViewport().getViewportHeight()) {
            setY(0)
        }
        for(i <- getActions()) {
            i.act(delta)
        }
    }
    def updateBounds() = {
        val center = localToStageCoordinates(new Vector2(getOriginX(), getOriginY()))
        bounds.set(center.x, center.y, texture.getRegionWidth() / 2 * getScaleX)
    }
    override def draw(batch:Batch, alpha:Float) {
        val bt = bound_texture
        val center = localToStageCoordinates(new Vector2(getOriginX(), getOriginY()))
        // batch.draw(bt,this.getX(),getY(),this.getOriginX(),this.getOriginY(),this.getWidth(),
        //     this.getHeight(),this.getScaleX(), this.getScaleY(),this.getRotation(),0,0,
        //     bt.getWidth(),bt.getHeight(),false,false);

        batch.draw(texture,this.getX(),getY(),this.getOriginX(),this.getOriginY(),this.getWidth(),
            this.getHeight(),this.getScaleX(), this.getScaleY(),this.getRotation())

    }

    def makeBoundTexture() = {
        val size = (bounds.radius * 2).toInt

        val pixmap = new Pixmap(size, size, Format.RGB888)
        pixmap.setColor(0, 255, 0, 100)
        pixmap.drawCircle(size / 2, size / 2, size / 2)
        val tex = new Texture(pixmap)
        pixmap.dispose()
        tex


    }
    def collide(that:ActorInView):Boolean = {
        that.bounds.overlaps(bounds)
    }

}

class AcceleratableActor(val path:String) extends ActorInView(path) {
    var velocity: Vector2 = new Vector2(0f, 0f)

    var movement_action = new Action {
        def act(delta:Float) = {
            moveBy(-velocity.x, -velocity.y)
            true
        }
    }
    addAction(movement_action)

    def setVelocity(x:Float, y:Float) = velocity.set(x, y)
    def setVelocity(a:Vector2) = velocity.set(a)

}
