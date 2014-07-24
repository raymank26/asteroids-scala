package my.game.pkg.stage

import my.game.pkg.asteroid.Asteroid
import my.game.pkg.round.Round
import my.game.pkg.ship.{Bullet, Ship}
import my.game.pkg.utils.Utils._

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport
import com.badlogic.gdx.scenes.scene2d.{Stage => S, Actor}
import com.badlogic.gdx.scenes.scene2d.ui.{Table, Label, Skin, TextButton}
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent
import com.badlogic.gdx.graphics.{Color, Texture, Pixmap}
import com.badlogic.gdx.graphics.Pixmap.Format

import com.badlogic.gdx.graphics.g2d.freetype._

import scala.util.Random
import scala.math
import scala.language.implicitConversions._
import scala.collection.JavaConversions._


class Stage(viewport: Viewport) extends S(viewport) {

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

    def createMenu() {
        val skin = new Skin()
        skin.add("default", new BitmapFont())
        val pixmap = new Pixmap(1, 1, Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));
        // label = new Label("New game")
        // label = new Label("New game")
        // label = new Label("New game")
        // label = new Label("New game")
        val textButtonStyle = new TextButtonStyle()
        textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY)
        textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY)
        textButtonStyle.checked = skin.newDrawable("white", Color.BLUE)
        textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY)
        // textButtonStyle.font = skin.getFont("default")
        textButtonStyle.font = font12
        skin.add("default", textButtonStyle)

        val table = new Table()
        table.setFillParent(true)
        addActor(table)
        val start_game = new TextButton("Start game", skin);
        val hall_of_fame = new TextButton("Hall of Fame", skin);
        table.add(start_game).spaceBottom(10);
        table.row()
        table.add(hall_of_fame)

        start_game.addListener(new ChangeListener() {
            def changed(event:ChangeEvent, actor:Actor) {
                println("Game should start here")
                startGame()
            }
            })
        hall_of_fame.addListener(new ChangeListener() {
            def changed(event: ChangeEvent, actor:Actor) {
                println("Hall of Fame should start here")

            }
            })
    }

    def startGame() {
        clear()
        val ship = new Ship()
        val round = new Round(1, 0, this)
        round.start()
    }

    val generator = new FreeTypeFontGenerator(Gdx.files.internal("Orbitron-Medium.ttf"));
    val font12 = generator.generateFont(20);


}
