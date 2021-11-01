package mf.aarr.ast.liveVariableAnalysis.api

import mf.aarr.ast.liveVariableAnalysis.example.WhileAPIExample.test
import org.json4s._
import org.json4s.native.JsonMethods._
import scalaj.http.{Http, HttpOptions, HttpResponse}

object RequestWhileParser {

  private val PROTOCOL: String = "http://"
  val SERVER_NAME: String = "dalila.sip.ucm.es:4000"
  val API_URL: String = "/while_parser/api/parse"
  val URL: String = PROTOCOL + SERVER_NAME + API_URL

  lazy val httpRequestPost: (String, Boolean) => HttpResponse[String] =
    (data: String, cfg: Boolean) =>
      Http(URL)
        .postData(data)
        .header("Content-type", "application/x-www-form-urlencoded")
        .header("Accept", "application/json")
        .param("while_code", data)
        .param("cfg", cfg.toString)
        .option(HttpOptions.readTimeout(10000)).asString

  def post(whileCode: String, cfg: Boolean = true): HttpResponse[String] =
    httpRequestPost(whileCode, cfg)

  def body(request: String): Option[List[JValue]] =
    parse(request) \ "body" match {
      case JArray(arr) => Option(arr)
      case _ => None
    }

  def body(request: HttpResponse[String]): Option[List[JValue]] = body(request.body)


  /** Test */

  val request: HttpResponse[String] = post(test)
  val testJson: Option[List[JValue]] = body(request)

  def main(args: Array[String]): Unit = {
    println(testJson)
  }

}
