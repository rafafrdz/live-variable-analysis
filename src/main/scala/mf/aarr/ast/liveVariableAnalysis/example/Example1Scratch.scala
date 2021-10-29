package mf.aarr.ast.liveVariableAnalysis.example

import mf.aarr.ast.liveVariableAnalysis.adt._
import mf.aarr.ast.liveVariableAnalysis.analysis.AnalysisFunction.{analysis, end, out}
import mf.aarr.ast.liveVariableAnalysis.analysis.Graph

/** ************************************
 * DataFlow Analysys for Live Variables
 * *************************************
 * Example from scratch
 * Representation in Example1.pdf */

object Example1Scratch {

  /** Array with all blocks-process */
  val blocks: Array[Block] = Array(
    AssignmentBlock(1, Assignment(Var("x"), Const(10)))
    , ConditionalBlock(2, Leq(Const(0), Var("x")))
    , AssignmentBlock(3, Assignment(Var("x"), Sub(Var("x"), Const(-1))))
    , AssignmentBlock(4, Assignment(Var("y"), Add(Var("y"), Var("x"))))
    , AssignmentBlock(5, Assignment(Var("z"), Const(2)))
    , AssignmentBlock(6, Assignment(Var("z"), Mul(Var("y"), Var("z"))))
  )

  /** Map datatype for block-process representation */
  val graph: Graph[Int] = Map(
    1 -> out(2)
    , 2 -> out(3, 5)
    , 3 -> out(4)
    , 4 -> out(2)
    , 5 -> out(6)
    , 6 -> end
  )

  implicit val process: CFG = CFG.build(graph, blocks)

  def main(args: Array[String]): Unit = {
    analysis foreach println
  }

}
