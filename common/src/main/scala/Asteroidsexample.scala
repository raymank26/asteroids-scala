package my.game.pkg

import my.game.pkg.ship.Ship
import my.game.pkg.stage.Stage

import scala.math

import com.badlogic.gdx.Game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.viewport.ScreenViewport

class Asteroidsexample extends Game {
    private var stage: Stage = _
    override def create() {
        stage = new Stage(new ScreenViewport())
        val ship = new Ship()
        val viewport = stage.getViewport()

        ship.setX(viewport.getViewportWidth() / 2)
        ship.setY(viewport.getViewportHeight() / 2)
        stage.createMenu
        // stage.addActor(ship)
        // stage.spawn


        Gdx.input.setInputProcessor(stage);
        // stage.setKeyboardFocus(ship)
    }

    override def render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw()
        stage.act(Gdx.graphics.getDeltaTime());

    }
    override def resize(width:Int, height:Int) {
         stage.getViewport().update(width, height, true);
    }
}
