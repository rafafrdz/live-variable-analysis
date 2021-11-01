package mf.aarr.ast.liveVariableAnalysis.api

import mf.aarr.ast.liveVariableAnalysis.adt._
import mf.aarr.ast.liveVariableAnalysis.api.RequestWhileParser.testJson
import org.json4s.JsonAST.JValue
import org.json4s.{DefaultFormats, Formats}

import scala.language.implicitConversions
import scala.reflect.Manifest
import scala.util.Try

object ParserJsonCaseClass {
  private implicit val formats: Formats = DefaultFormats

  case class BlockParse(contents: Contents, typeP: String, label: Int, succ: Succ)

  sealed trait Contents

  case class ContentsAssigment(lhs: String, rhs: ExpressionHS) extends Contents

  case class ContentsCondition(condition: ExpressionHS) extends Contents

  sealed trait HS

  case class ExpressionHS(category: String, categorySub: String, line: Int, options: Exp) extends HS

  case class ConditionHS(category: String, categorySub: String, line: Int, options: HS) extends HS

  case class OptionHS(lhs: ConditionHS, rhs: ConditionHS) extends HS

  case class SimpleHS[T](value: T) extends HS

  case class Succ(labels: List[Int])

  private def get[T: Manifest](json: JValue): T = json.extract[T]

  /** Block flags */
  private def getSucc(json: JValue): Succ = Succ(Try(get[List[Int]](json \ "succs")).getOrElse(Nil))

  private def getLabel(json: JValue): Int = get[Int](json \ "label")

  private def getType(json: JValue): String = get[String](json \ "type")

  /** Contents */

  /** HS */
  private def getSubCategory(json: JValue): String = get[String](json \ "category_sub")

  private def getCategory(json: JValue): String = get[String](json \ "category")

  private def getLine(json: JValue): Int = get[Int](json \ "line")

  /** option hs */
  private def getOptionJValue(json: JValue): JValue = json \ "options"

  private def getOption(json: JValue, optType: String): HS = {
    val option: JValue = getOptionJValue(json)
    optType match {
      case "literal" => SimpleHS(get[Double](option \ "number"))
      case "variable" => SimpleHS(get[String](option \ "name"))
      case _ =>
        val (lhs, rhs) = (parseHS(option \ "lhs"), parseHS(option \ "rhs"))
        OptionHS(lhs, rhs)
    }
  }


  private def parseHS(json: JValue): ConditionHS = {
    val category: String = getCategory(json)
    val subCategory: String = getSubCategory(json)
    val line: Int = getLine(json)
    val option: HS = getOption(json, subCategory)
    ConditionHS(category, subCategory, line, option)
  }

  private def expression(hs: ConditionHS): ExpressionHS =
    ExpressionHS(hs.category, hs.categorySub, hs.line, exp(hs.options, hs.categorySub))

  private def exp[T <: Exp](hs: HS, optType: String): T = {
    hs match {
      case ExpressionHS(_, _, _, e) => e
      case ConditionHS(_, categorySub, _, options) => exp[Exp](options, categorySub)
      case OptionHS(lhs, rhs) => optType match {
        case "and" => And(exp[BExp](lhs, lhs.categorySub), exp[BExp](rhs, rhs.categorySub))
        case "eq" => Eq(exp[AExp](lhs, lhs.categorySub), exp[AExp](rhs, rhs.categorySub))
        case "leq" => Leq(exp[AExp](lhs, lhs.categorySub), exp[AExp](rhs, rhs.categorySub))
        case "add" => Add(exp[AExp](lhs, lhs.categorySub), exp[AExp](rhs, rhs.categorySub))
        case "sub" => Sub(exp[AExp](lhs, lhs.categorySub), exp[AExp](rhs, rhs.categorySub))
        case "mul" => Mul(exp[AExp](lhs, lhs.categorySub), exp[AExp](rhs, rhs.categorySub))
      }
      case SimpleHS(n: Double) => Const(n)
      case SimpleHS(v: String) => Var(v)
    }

  }

  private def getCondition(json: JValue): ContentsCondition = {
    ContentsCondition(expression(parseHS(json \ "condition")))
  }

  private def getAssignation(json: JValue): ContentsAssigment = {
    val (lhs, rhs): (String, ConditionHS) = (get[String](json \ "lhs"), parseHS(json \ "rhs"))
    ContentsAssigment(lhs, expression(rhs))
  }

  def getBlock(json: JValue): BlockParse = {
    val typeP: String = getType(json)
    val contentsJson: JValue = json \ "contents"
    val contents: Contents = typeP match {
      case "condition" => getCondition(contentsJson)
      case "assignment" => getAssignation(contentsJson)
    }

    val label: Int = getLabel(json)
    val succ: Succ = getSucc(json)
    BlockParse(contents, typeP, label, succ)
  }

  def getProcess(body: Option[List[JValue]]): List[BlockParse] = body match {
    case Some(data) => data.map(getBlock)
    case None => Nil
  }

  def main(args: Array[String]): Unit = {
    println(getProcess(testJson))
  }
}
