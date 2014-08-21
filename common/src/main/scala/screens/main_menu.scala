package my.game.pkg.screens

// import my.game.pkg.screens.BaseScreen

import my.game.pkg.utils.Utils._
import my.game.pkg.Asteroidsexample
import my.game.pkg.Settings
import my.game.pkg.utils.Implicits._

import com.badlogic.gdx.Game
import com.badlogic.gdx.scenes.scene2d.{Stage, Actor}
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.viewport.{Viewport, ScreenViewport}
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


class MainMenu(
    stage:Stage = new Stage(new ScreenViewport()),
    game: Asteroidsexample
    ) extends BaseScreen(stage) {

    val skin = Settings.skin

    val table = new Table()
    table.setFillParent(true)
    stage.addActor(table)
    val game_name = new Label("ASTEROIDS", skin, "game_name")
    val start_game = new TextButton("Start game", skin);
    val hall_of_fame = new TextButton("Hall of Fame", skin);
    table.add(game_name).spaceBottom(30).row()
    table.add(start_game).spaceBottom(20);
    table.row()
    table.add(hall_of_fame)

    start_game.addListener(game.setGame())
    hall_of_fame.addListener(game.setHallOfFame())
}



