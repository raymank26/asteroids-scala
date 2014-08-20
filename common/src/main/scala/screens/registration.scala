package my.game.pkg.screens

import my.game.pkg.Settings
import my.game.pkg.Asteroidsexample
import com.badlogic.gdx.scenes.scene2d.{Stage, Actor}
import com.badlogic.gdx.scenes.scene2d.ui.{Table, Label, Skin, TextButton, TextField}
import com.badlogic.gdx.utils.viewport.{Viewport, ScreenViewport}

class RegistrationScreen (
    stage:Stage = new Stage(new ScreenViewport()),
    val game: Asteroidsexample
    ) extends BaseScreen(stage) {

    val skin = Settings.skin

    val table = new Table()
    table.setFillParent(true)
    stage.addActor(table)

    val nameText = new TextField("", skin);
    val passwordText = new TextField("", skin);
    passwordText.setPasswordMode(true)
    passwordText.setPasswordCharacter('*');

    table.add(new Label("Registration", skin, "game_name")).colspan(2).spaceBottom(10).row()
    table.add(new Label("Name", skin, "default"))
    table.add(nameText)
    table.row()
    table.add(new Label("Password", skin, "default"))
    table.add(passwordText)

}
