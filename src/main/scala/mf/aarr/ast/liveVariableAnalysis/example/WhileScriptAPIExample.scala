package mf.aarr.ast.liveVariableAnalysis.example

import mf.aarr.ast.liveVariableAnalysis.io.WhileFile.analysis

object WhileScriptAPIExample {

  val path: String = "src/main/resources/while-code-script.txt"

  def main(args: Array[String]): Unit = {
    analysis(path) foreach println
  }
}
