package my.game.pkg

import my.game.pkg.Settings
// import dispatch._
// import dispatch.Defaults._

import scalaj.http.{Http => HttpJ}
// import org.json4s._
// import org.json4s.native.JsonMethods._
// import org.json4s.Formats
// import JsonDSL._
import scalaj.http.HttpException
import argonaut._, Argonaut._

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global
// import ExecutionContext.Implicits.global

case class BadRequest(path: List[String], message: String) extends Exception

object Backend {
    // implicit lazy val formats = DefaultFormats
    implicit class StringWithSlash(a:String) {
        def /(appended: String) = {
            a + "/" + appended
        }
    }
    var key: Option[String] = None
    def submit_score(score:Int) = {
        key.map { (token:String) =>
            Future {
                val response = HttpJ
                    .post(Settings.backend_host_new / "scores" / "submit" / "")
                    .param("score", score.toString)
                    .header("Authentication", token).asString
                Some(response)
            }
        }
    }
    def fetch_scores = {
        Future {
            val response = HttpJ(Settings.backend_host_new / "scores" / "top10" / "").asString
            response.decodeOption[List[Map[String, String]]].getOrElse(List())
        }
    }
    def authenticate(name: String, password:String) = {
        Future {
            val response = HttpJ.post(Settings.backend_host_new / "users" / "authenticate")
                .method("PUT")
                .params("username" -> name, "password" -> password)
                .asString
            key = for {
                value <- response.decodeOption[Map[String, String]]
            } yield value("token")
            // val res = parse(response).extract[Map[String, String]]
            // key = Some(res("token"))
        }
    }
    def isAuthenticated() = {
        key match {
            case Some(_) => true
            case None => false
        }
    }
    def register(name: String, password1: String, password2: String) = {
        println("In register")
        val f = Future {
            val response = HttpJ.post(Settings.backend_host_new / "users" / "")
                .params("username" -> name, "password1" -> password1,
                    "password2" -> password2)
            response.asString
        }
        f
        f recover { // process json
            case ex:HttpException => {
                val errorLn = jObjectPL >=> jsonObjectPL("errors") >=> jStringPL
                val errorListLn = jObjectPL >=> jsonObjectPL("path") >=> jArrayPL
                val json = ex.body.parseOption
                val message = for {
                    j <- json
                    m <- errorLn.get(j)
                } yield m
                val errors = for {
                    j <- json
                    errors <- errorListLn.get(j)
                } yield for {
                    error <- errors
                } yield error.stringOr("")
                throw new BadRequest(errors.getOrElse(List()), message.getOrElse(""))
            }
        }
    }
}
