package my.game.pkg.screens

import my.game.pkg.utils.Utils._
import my.game.pkg.Asteroidsexample
import my.game.pkg.round.{Round, RoundState}
import com.badlogic.gdx.scenes.scene2d.{Stage, Actor}
import com.badlogic.gdx.scenes.scene2d.ui.{Table, Label, Skin, TextButton}
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.graphics.g2d.BitmapFont

import com.badlogic.gdx.utils.viewport.{Viewport, ScreenViewport}
import com.badlogic.gdx.Screen

class GameScreen(
    stage:Stage = new Stage(new ScreenViewport()),
    val game: Asteroidsexample
    ) extends BaseScreen(stage) {


    val initState = new RoundState(0, 3, 1)
    var current_round = new Round(initState, this, stage)

    def start () {
        current_round.splash()
    }

    override def render(delta: Float) {
        current_round.act
        super.render(delta)
    }
}


class GameOverScreen(
    stage:Stage = new Stage(new ScreenViewport()),
    val game: Asteroidsexample
    ) extends BaseScreen(stage) {

    // val skin = new Skin()
    // skin.add("default", new BitmapFont())
    // val pixmap = new Pixmap(1, 1, Format.RGBA8888);
    // pixmap.setColor(Color.WHITE);
    // pixmap.fill();
    // skin.add("white", new Texture(pixmap));
    // val textButtonStyle = new TextButtonStyle()
    // textButtonStyle.checked = skin.newDrawable("white", Color.BLUE)
    // textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY)
    // textButtonStyle.font = font_gen(20)
    // skin.add("default", textButtonStyle)

    // val label_style = new Label.LabelStyle(font_gen(40), Color.WHITE)
    // skin.add("game_name", label_style)

    // val table = new Table()
    // table.setFillParent(true)
    // stage.addActor(table)
    // val game_name = new Label("ASTEROIDS", skin, "game_name")
    // val start_game = new TextButton("Start game", skin);
    // val hall_of_fame = new TextButton("Hall of Fame", skin);
    // table.add(game_name).spaceBottom(30).row()
    // table.add(start_game).spaceBottom(20);
    // table.row()
    // table.add(hall_of_fame)

}
