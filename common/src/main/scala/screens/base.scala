package my.game.pkg.screens

import com.badlogic.gdx.Screen
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20


abstract class BaseScreen(stage: Stage) extends Screen {


    def render(delta: Float) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw()
        stage.act(delta);
    }

    def resize(width:Int, height:Int) {
         stage.getViewport().update(width, height, true);
    }

    def pause {}

    def show {
        Gdx.input.setInputProcessor(stage);
    }

    def hide {}

    def dispose {}

    def resume {}

}
