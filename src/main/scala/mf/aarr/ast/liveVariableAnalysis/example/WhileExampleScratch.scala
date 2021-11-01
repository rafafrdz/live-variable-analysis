package mf.aarr.ast.liveVariableAnalysis.example

import mf.aarr.ast.liveVariableAnalysis.adt._
import mf.aarr.ast.liveVariableAnalysis.analysis.AnalysisFunction.{analysis, end, out}
import mf.aarr.ast.liveVariableAnalysis.analysis.Graph

object WhileExampleScratch {
  /**
   * c := 10;
   * y := 1;
   * while 1 <= c do
   * y := y * c;
   * c := c - 1;
   * end;
   * res := y;
   *
   */

  /** Array with all blocks-process */
  val blocks: Array[Block] = Array(
    AssignmentBlock(1, Assignment(Var("c"), Const(10)))
    , AssignmentBlock(2, Assignment(Var("y"), Const(1)))
    , ConditionalBlock(3, Leq(Const(1), Var("c")))
    , AssignmentBlock(4, Assignment(Var("y"), Mul(Var("y"), Var("c"))))
    , AssignmentBlock(5, Assignment(Var("c"), Sub(Var("c"), Const(1))))
    , AssignmentBlock(6, Assignment(Var("res"), Var("y")))
  )

  /** Map datatype for block-process representation */
  val graph: Graph[Int] = Map(
    1 -> out(2)
    , 2 -> out(3)
    , 3 -> out(4)
    , 4 -> out(5)
    , 5 -> out(3, 4)
    , 6 -> end
  )

  implicit val process: CFG = CFG.build(graph, blocks)

  def main(args: Array[String]): Unit = {
    analysis foreach println
  }
}
