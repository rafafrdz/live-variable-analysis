package mf.aarr.ast.liveVariableAnalysis.api

import mf.aarr.ast.liveVariableAnalysis.adt.{CFG, LiveVariable}
import mf.aarr.ast.liveVariableAnalysis.analysis.AnalysisFunction
import mf.aarr.ast.liveVariableAnalysis.api.ParserControlFlowGraph.toControlFlowGraph
import mf.aarr.ast.liveVariableAnalysis.api.ParserJsonCaseClass.{BlockParse, getProcess}
import mf.aarr.ast.liveVariableAnalysis.api.RequestWhileParser.{body, post}
import org.json4s.JValue
import scalaj.http.HttpResponse

object WhileControlFlowGraph {

  def request(whileCode: String): CFG = {
    val request: HttpResponse[String] = post(whileCode)
    val data: Option[List[JValue]] = body(request)
    val process: List[BlockParse] = getProcess(data)
    toControlFlowGraph(process)
  }

  def analysis(whileCode: String): Array[LiveVariable] = AnalysisFunction.analysis(request(whileCode))
}
