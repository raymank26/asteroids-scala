package my.game.pkg

import my.game.pkg.Settings
// import dispatch._
// import dispatch.Defaults._

import scalaj.http.{Http => HttpJ}
import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s.Formats
import JsonDSL._

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global
// import ExecutionContext.Implicits.global


object Backend {
    implicit lazy val formats = DefaultFormats
    implicit class StringWithSlash(a:String) {
        def /(appended: String) = {
            a + "/" + appended
        }
    }
    var key: Option[String] = None
    // def submit_score(score:Int) = {
    //     key.map { (token:String) =>
    //         val requestBuilder = Settings.backend_host / "scores" / "submit" / "" POST
    //         val url = requestBuilder << Map("score" -> score.toString) <:< Map("Authentication" -> token)
    //         val response = Http(url OK as.json4s.Json)
    //         Some(response)
    //     }
    // }
    def fetch_scores = {
        Future {
            val response = HttpJ(Settings.backend_host_new / "scores" / "top10" / "").asString
            parse(response).extract[List[Map[String,String]]]

        }
    }
    def authenticate(name: String, password:String) = {
        Future {
            val response = HttpJ.post(Settings.backend_host_new / "users" / "authenticate")
                .method("PUT")
                .params("username" -> name, "password" -> password)
                .asString
            val res = parse(response).extract[Map[String, String]]
            res("token")
        }
    }
    def isAuthenticated() = {
        key match {
            case Some(_) => true
            case None => false
        }
    }
}
