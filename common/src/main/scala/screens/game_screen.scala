package my.game.pkg.screens

import my.game.pkg.utils.Utils._
import my.game.pkg.Asteroidsexample
import my.game.pkg.round.{Round, RoundState}
// import my.game.pkg.stage.Stage
import com.badlogic.gdx.scenes.scene2d.{Stage, Actor}

import com.badlogic.gdx.utils.viewport.{Viewport, ScreenViewport}
import com.badlogic.gdx.Screen

class GameScreen(
    stage:Stage = new Stage(new ScreenViewport()),
    game: Asteroidsexample
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
