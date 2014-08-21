package my.game.pkg

import my.game.pkg.Settings
import dispatch._
import dispatch.Defaults._

import org.json4s._
import org.json4s.Formats
import JsonDSL._

object Backend {
    var key: Option[String] = None
    def submit_score(score:Int) = {
        key.map { (token:String) =>
            val requestBuilder = Settings.backend_host / "scores" / "submit" / "" POST
            val url = requestBuilder << Map("score" -> score.toString) <:< Map("Authentication" -> token)
            val response = Http(url OK as.json4s.Json)
            Some(response)
        }
    }
    def fetch_scores = {
        implicit lazy val formats = DefaultFormats
        val url = Settings.backend_host / "scores" / "top10" / "" GET
        val response = Http(url OK as.json4s.Json)
        response map { json => json.extract[List[Map[String,String]]] }
        // response


    }
    def authenticate(name: String, password:String) = {
        val requestBuilder = Settings.backend_host / "users" / "authenticate" PUT
        val url = requestBuilder << Map("username" -> name, "password" -> password)
        val response = Http(url OK as.json4s.Json )
        response onSuccess {
            case a:JObject => {
                a \ "token" match {
                    case str:JString => {
                        key = Some(str.s)
                    }
                }
            }
        }
        response
    }
    def isAuthenticated() = {
        key match {
            case Some(_) => true
            case None => false
        }
    }
}
