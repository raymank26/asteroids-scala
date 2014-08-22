package my.game.pkg

// import my.game.pkg.stage.Stage
import my.game.pkg.screens._
import my.game.pkg.Backend
import com.badlogic.gdx.tools.texturepacker.TexturePacker

import scala.math

import com.badlogic.gdx.Game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.viewport.ScreenViewport
import scala.concurrent.ExecutionContext.Implicits.global

class Asteroidsexample extends Game {

    var main_menu: MainMenu = _
    var game_screen: GameScreen = _
    var gameover_screen: GameOverScreen = _
    var registration : RegistrationScreen = _
    var submit_screen: SubmitScreen = _
    var hall_of_fame: HallOfFameScreen = _

    def create () {
        // TexturePacker.process(
        //     "/home/rayman/workspace/scala/asteroids-example/common/assets/images/",
        //     "/home/rayman/workspace/scala/asteroids-example/common/assets/", "result")
        main_menu = new MainMenu(game=this)
        game_screen = new GameScreen(game=this)
        gameover_screen = new GameOverScreen(game=this)
        registration = new RegistrationScreen(game=this)
        submit_screen = new SubmitScreen(game=this)
        hall_of_fame = new HallOfFameScreen(game=this)
        // Backend.authenticate("anton", "123") onSuccess {
        //     case value => value
        // }

        setScreen(main_menu)
        // setScreen(registration)
        // setScreen(gameover_screen)
        // setScreen(submit_screen)
        // setScreen(hall_of_fame)


        // gameover_screen.score = (10000)
        // setScreen(gameover_screen)

    }

    def setGame () {
        setScreen(game_screen)
        game_screen.start()

    }
    def setHallOfFame() {
        setScreen(hall_of_fame)
    }
    def setRegistration() {
        setScreen(registration)
    }

    def setMainMenu() {
        setScreen(main_menu)
    }
    def showGameOver(score:Int) {
        gameover_screen.score = score
        setScreen(gameover_screen)
    }

    // override def create() {
    //     stage = new Stage(new ScreenViewport())
    //     val ship = new Ship()
    //     val viewport = stage.getViewport()

    //     ship.setX(viewport.getViewportWidth() / 2)
    //     ship.setY(viewport.getViewportHeight() / 2)
    //     stage.createMenu
    //     // stage.addActor(ship)
    //     // stage.spawn


    //     Gdx.input.setInputProcessor(stage);
    //     // stage.setKeyboardFocus(ship)
    // }

    // override def render() {
    //     Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    //     stage.draw()
    //     stage.act(Gdx.graphics.getDeltaTime());

    // }
    // override def resize(width:Int, height:Int) {
    //      stage.getViewport().update(width, height, true);
    // }
}
