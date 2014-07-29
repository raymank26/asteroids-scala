package my.game.pkg.screens

import my.game.pkg.utils.Utils._
import my.game.pkg.Asteroidsexample
import my.game.pkg.round.{Round, RoundState}
import my.game.pkg.Settings.font_gen

import com.badlogic.gdx.scenes.scene2d.{Stage, Actor}
import com.badlogic.gdx.scenes.scene2d.ui.{Table, Label, Skin, TextButton}
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.{Color, Texture, Pixmap}

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

    private var _score = 0

    val skin = new Skin()
    skin.add("default", new BitmapFont())
    val pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
    pixmap.setColor(Color.WHITE);
    pixmap.fill();
    skin.add("white", new Texture(pixmap));

    val gameover_style = new Label.LabelStyle(font_gen(30), Color.WHITE)
    skin.add("gameover_label", gameover_style)
    val score_style = new Label.LabelStyle(font_gen(20), Color.WHITE)
    skin.add("score", score_style)


    val table = new Table()
    table.setFillParent(true)
    stage.addActor(table)
    val gameover_label = new Label("GAME OVER", skin, "gameover_label")

    val score_text = StringContext("Your score is ", "")
    val score_label = new Label(score_text.s(_score), skin, "score")

    table.add(gameover_label).padBottom(10).row()
    table.add(score_label)

    def score = _score

    def score_=(sc:Int):Unit = {
        _score = sc
        score_label.setText(score_text.s(_score))
    }
}
