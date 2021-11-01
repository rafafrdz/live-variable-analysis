package mf.aarr.ast.liveVariableAnalysis.api

import mf.aarr.ast.liveVariableAnalysis.adt._
import mf.aarr.ast.liveVariableAnalysis.analysis.AnalysisFunction.{end, out}
import mf.aarr.ast.liveVariableAnalysis.analysis.Graph
import mf.aarr.ast.liveVariableAnalysis.api.ParserJsonCaseClass._

object ParserControlFlowGraph {
  def toBlock(block: BlockParse): Block = {
    block.contents match {
      case ContentsAssigment(lhs, rhs) => AssignmentBlock(block.label, Assignment(Var(lhs), rhs.options))
      case ContentsCondition(condition) => ConditionalBlock(block.label, condition.options)
    }
  }

  def succ(block: BlockParse): Option[Set[Int]] = {
    val xs: List[Int] = block.succ.labels
    if (xs.isEmpty) end else out(xs: _*)
  }

  def toRef(block: BlockParse): (Int, Option[Set[Int]]) = block.label -> succ(block)

  def toControlFlowGraph(process: List[BlockParse]): CFG = {
    val blcks: Array[Block] = process.map(toBlock).toArray
    val ref: Graph[Int] = process.map(toRef).toMap
    CFG.build(ref, blcks)
  }

  val test: List[BlockParse] =
    List(
      BlockParse(ContentsAssigment("c", ExpressionHS("exp", "literal", 2, Const(10.0))), "assignment", 1, Succ(List(2)))
      , BlockParse(ContentsAssigment("y", ExpressionHS("exp", "literal", 3, Const(1.0))), "assignment", 2, Succ(List(3)))
      , BlockParse(ContentsCondition(ExpressionHS("exp", "and", 4, And(Leq(Const(1.0), Var("c")), Leq(Const(1.0), Var("c"))))), "condition", 3, Succ(List(4, 6)))
      , BlockParse(ContentsAssigment("y", ExpressionHS("exp", "eq", 5, Eq(Var("y"), Var("c")))), "assignment", 4, Succ(List(5)))
      , BlockParse(ContentsAssigment("c", ExpressionHS("exp", "sub", 6, Sub(Var("c"), Const(1.0)))), "assignment", 5, Succ(List(3)))
      , BlockParse(ContentsAssigment("res", ExpressionHS("exp", "variable", 8, Var("y"))), "assignment", 6, Succ(List())))

  def main(args: Array[String]): Unit = {
    val cfg: CFG = toControlFlowGraph(test)

    cfg.blocks foreach println
    cfg.graph foreach println
  }
}
