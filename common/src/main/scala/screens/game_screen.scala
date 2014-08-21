package my.game.pkg.screens

import my.game.pkg.utils.Utils._
import my.game.pkg.utils.Implicits._
import my.game.pkg.Asteroidsexample
import my.game.pkg.round.{Round, RoundState}
import my.game.pkg.{Settings, Backend}

import com.badlogic.gdx.scenes.scene2d.{Stage, Actor}
import com.badlogic.gdx.scenes.scene2d.ui._

import com.badlogic.gdx.utils.viewport.{Viewport, ScreenViewport}
import com.badlogic.gdx.Screen
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent

import scala.concurrent.ExecutionContext.Implicits.global

class GameScreen(
    stage:Stage = new Stage(new ScreenViewport()),
    val game: Asteroidsexample
    ) extends BaseScreen(stage) {


    val initState = new RoundState(0, 3, 1)
    var current_round = new Round(initState, this, stage)

    val background_sound = Settings.sounds("background")

    def start () {
        current_round.splash()
        background_sound.loop()
    }

    override def render(delta: Float) {
        current_round.act
        super.render(delta)
    }

    override def hide() {
        background_sound.stop()
    }
}


class GameOverScreen(
    stage:Stage = new Stage(new ScreenViewport()),
    val game: Asteroidsexample
    ) extends BaseScreen(stage) {

    private var _score = 0

    val skin = Settings.skin

    val table = new Table()
    table.setFillParent(true)
    stage.addActor(table)

    val score_text = StringContext("Your score is ", "")
    val score_label = new Label(score_text.s(_score), skin, "default")
    val nameText = new TextField("", skin);
    val passwordText = new TextField("", skin);
    val submit = new TextButton("Submit", skin)
    val back = new TextButton("Back to menu", skin)

    passwordText.setPasswordMode(true)
    passwordText.setPasswordCharacter('*');

    table.add(new Label("GAME OVER", skin, "big")).colspan(2).padBottom(10).row()
    table.add(score_label).colspan(2).spaceBottom(20).row()

    table.add(new Label("Name", skin, "default")).spaceRight(5)
    table.add(nameText).spaceBottom(10)
    table.row()
    table.add(new Label("Password", skin, "default")).spaceRight(5)
    table.add(passwordText).row()
    table.add(submit).colspan(2).right().spaceBottom(20).row()
    table.add(back).colspan(2)

    // submit.addListener(
    //     new ChangeListener() {
    //         def changed(event: ChangeEvent, actor:Actor): Unit = {
    //             println("HERE")
    //             1 / 0
    //         }
    //     }
    // )
    submit.addListener{() => {
        if(!Backend.isAuthenticated()) {
            Backend.authenticate(nameText.getText, passwordText.getText)
                .map(result => Backend.submit_score(500))
        }
        else {
            Backend.submit_score(100)
        }
        println("HERE")
        // Unit
    }
    }


    def score = _score

    def score_=(sc:Int):Unit = {
        _score = sc
        score_label.setText(score_text.s(_score))
    }
}
