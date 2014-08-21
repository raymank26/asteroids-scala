package my.game.pkg

import my.game.pkg.Settings
import dispatch._, Defaults._
import org.json4s._
import JsonDSL._

object Backend {
    var key: Option[String] = None
    def submit_score(score:Int) = {

    }
    def show_scores {

    }

    def authenticate(name: String, password:String) = {
        val requestBuilder = Settings.backend_host / "users" / "authenticate" PUT
        val url = requestBuilder << Map("username" -> name, "password" -> password)
        val response = Http(url OK as.json4s.Json )
        response onSuccess {
            case a:JObject => {
                a \\ "token" match {
                    case str:JString => {
                        key = Some(str.s)
                    }

                }
            }
        }
        response
    }
}
