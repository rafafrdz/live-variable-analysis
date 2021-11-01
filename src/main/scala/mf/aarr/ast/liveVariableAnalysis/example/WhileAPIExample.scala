package mf.aarr.ast.liveVariableAnalysis.example

import mf.aarr.ast.liveVariableAnalysis.api.WhileControlFlowGraph.analysis

/** Example of Live Variable analysis using while code and while parser api from scratch */
object WhileAPIExample {

  val test: String =
    """
      |c := 10;
      |    y := 1;
      |    while 1 <= c do
      |      y := y * c;
      |      c := c - 1;
      |    end;
      |    res := y;
                         """.stripMargin

  val test2: String =
    """
      |c := 3;
      |    y := 1;
      |    while c <= y do
      |      c := c - 1;
      |    end;
      |    res := c;
                         """.stripMargin

  def main(args: Array[String]): Unit = {
    analysis(test) foreach println
    analysis(test2) foreach println
  }
}
