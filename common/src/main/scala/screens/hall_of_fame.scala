package my.game.pkg.screens

import my.game.pkg.{Settings, Backend, BadRequest}
import my.game.pkg.Asteroidsexample
import my.game.pkg.utils.Implicits._

import com.badlogic.gdx.scenes.scene2d.{Stage, Actor}
import com.badlogic.gdx.scenes.scene2d.ui.{Table, Label, Skin, TextButton, TextField}
import com.badlogic.gdx.utils.viewport.{Viewport, ScreenViewport}
import com.badlogic.gdx.graphics.Color

import scalaj.http.HttpException

import scala.concurrent.ExecutionContext.Implicits.global
// import scala.concurrent._
import scala.util._

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
    val passwordConfirmationText = new TextField("", skin);
    val errorText = new Label("", skin)
    errorText.setColor(Color.RED)
    val submit = new TextButton("Register", skin);
    val back_to_menu = new TextButton("Back to menu", skin);

    passwordText.setPasswordMode(true)
    passwordText.setPasswordCharacter('*');
    passwordConfirmationText.setPasswordMode(true)
    passwordConfirmationText.setPasswordCharacter('*');

    table.add(new Label("Registration", skin, "game_name"))
        .colspan(2)
        .spaceBottom(20)
        .row()
    table.add(new Label("Name", skin, "default")).spaceRight(5)
    table.add(nameText).spaceBottom(10)
    table.row()
    table.add(new Label("Password", skin, "default")).spaceRight(5)
    table.add(passwordText)
    table.row()
    table.add(new Label("Confirm\npassword", skin)).spaceRight(5)
    table.add(passwordConfirmationText)
    table.row()
    table.add(errorText).colspan(2).row()
    table.add(submit).spaceTop(20).colspan(2).row()
    table.add(back_to_menu).colspan(2)

    submit.addListener{() =>
        Backend.register(nameText.getText, passwordText.getText,
            passwordConfirmationText.getText) onComplete {
            case Success(_) => {
                clearColors
                errorText.setText("success")
            }
            case Failure(e) => e match {
                case BadRequest(path, message) => {
                    for(entry <- path) {
                        entry match {
                            case "username" => nameText.setColor(Color.RED)
                            case "password1" => passwordText.setColor(Color.RED)
                            case "password2" => passwordConfirmationText.setColor(Color.RED)
                        }
                    }
                    println(s"message ${message}")
                    println(s"path ${path}")
                    errorText.setText(message)
                }
                case _ =>
            }

        }
    }
    back_to_menu.addListener{() =>
        game.setMainMenu()


    }
    def clearColors():Unit = {
        val defaultColor = Color.WHITE
        nameText.setColor(defaultColor)
        passwordText.setColor(defaultColor)
        passwordConfirmationText.setColor(defaultColor)

    }

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

class HallOfFameScreen (
    stage:Stage = new Stage(new ScreenViewport()),
    val game: Asteroidsexample
    ) extends BaseScreen(stage) {

    val skin = Settings.skin
    var _score = 0

    val table = new Table
    table.setFillParent(true)
    stage.addActor(table)

    def score_(value:Int) = _score = value

    override def show {
        super.show
        val response = Backend.fetch_scores
        response onSuccess {
            case scores => {
                table.clearChildren
                val back_to_menu = new TextButton("Back to menu", skin)
                table.add(new Label("Hall of Fame", skin, "game_name"))
                    .colspan(2)
                    .spaceBottom(20)
                    .row()
                back_to_menu.addListener {() =>
                    game.setMainMenu()
                }
                for(score <- scores) {
                    table.add(new Label(score("username"), skin))
                    table.add(new Label(score("value"), skin))
                    table.row()
                }
                table.add(back_to_menu).colspan(2).spaceTop(20).row()
            }
        }
    }

}
