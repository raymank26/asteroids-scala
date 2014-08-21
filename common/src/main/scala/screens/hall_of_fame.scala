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

    table.add(new Label("Registration", skin, "game_name"))
        .colspan(2)
        .spaceBottom(20)
        .row()
    table.add(new Label("Name", skin, "default")).spaceRight(5)
    table.add(nameText).spaceBottom(10)
    table.row()
    table.add(new Label("Password", skin, "default")).spaceRight(5)
    table.add(passwordText)

}

class SubmitScreen (
    stage:Stage = new Stage(new ScreenViewport()),
    val game: Asteroidsexample
    ) extends BaseScreen(stage) {

    val skin = Settings.skin
    var _score = 0

    val table = new Table
    table.setFillParent(true)
    stage.addActor(table)

    val nameText = new TextField("", skin);
    val passwordText = new TextField("", skin);
    passwordText.setPasswordMode(true)
    passwordText.setPasswordCharacter('*');

    table.add(new Label("Submit score", skin, "game_name"))
        .colspan(2)
        .spaceBottom(20)
        .row()

    table.add(new Label("Name", skin, "default")).spaceRight(5)
    table.add(nameText).spaceBottom(10)
    table.row()
    table.add(new Label("Password", skin, "default")).spaceRight(5)
    table.add(passwordText)

    def score_(value:Int) = _score = value

}
