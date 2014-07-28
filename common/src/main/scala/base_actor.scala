package my.game.pkg.base_actor
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.math.Rectangle
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

class ActorInView(val texture_path:String) extends Actor {
    protected val bounds = new Rectangle()
    protected val texture = new Texture(Gdx.files.internal(texture_path))

    updateBounds(getX(), getY(), texture.getWidth(), texture.getHeight())
    protected var bound_texture = makeBoundTexture()

    setBounds(0,0,texture.getWidth(), texture.getHeight())
    setOrigin(getWidth()/2, getHeight()/2);

    override def act(delta: Float) {
        updateBounds(getX(),getY(),texture.getWidth() * getScaleX,
            texture.getHeight() * getScaleY)

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
    def updateBounds(x1:Float, y1:Float, x2:Float, y2:Float) = {
        bounds.set(x1, y1, x2, y2)
    }
    override def draw(batch:Batch, alpha:Float) {
        val bt = bound_texture
        // batch.draw(bt,this.getX(),getY(),this.getOriginX(),this.getOriginY(),this.getWidth(),
        //     this.getHeight(),this.getScaleX(), this.getScaleY(),this.getRotation(),0,0,
        //     bt.getWidth(),bt.getHeight(),false,false);

        batch.draw(texture,this.getX(),getY(),this.getOriginX(),this.getOriginY(),this.getWidth(),
            this.getHeight(),this.getScaleX(), this.getScaleY(),this.getRotation(),0,0,
            texture.getWidth(),texture.getHeight(),false,false);
    }

    def makeBoundTexture() = {

        val pixmap = new Pixmap(bounds.width.toInt, bounds.height.toInt,
            Format.RGB888)
        pixmap.setColor(0, 255, 0, 100)
        pixmap.drawRectangle(0, 0, pixmap.getWidth, pixmap.getHeight)
        pixmap.drawCircle(0, 0, 15)
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
