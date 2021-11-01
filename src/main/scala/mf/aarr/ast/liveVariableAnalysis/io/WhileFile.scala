package mf.aarr.ast.liveVariableAnalysis.io

import mf.aarr.ast.liveVariableAnalysis.adt.LiveVariable
import mf.aarr.ast.liveVariableAnalysis.api.WhileControlFlowGraph

import java.io.File
import scala.io.{BufferedSource, Source}

object WhileFile {

  def from(path: String): String = {
    val file = new File(path)
    val source: BufferedSource = Source.fromFile(file)
    source.mkString("")
  }

  def analysis(path: String): Array[LiveVariable] = WhileControlFlowGraph.analysis(from(path))

}
