package mf.aarr.ast.liveVariableAnalysis.example

import mf.aarr.ast.liveVariableAnalysis.adt._
import mf.aarr.ast.liveVariableAnalysis.analysis.AnalysisFunction.variables

object ExpressionScratch {
  val sample: Exp =
    And(
      Leq(
        Add(Const(3), Sub(Mul(Const(2), Var("x")), Var("y"))),
        Add(Const(3), Sub(Mul(Const(2), Var("x")), Var("y")))
      ),
      Eq(Add(Const(3), Sub(Mul(Const(1), Var("x")), Var("y"))),
        Add(Const(-9), Sub(Mul(Const(2), Var("x")), Var("y"))))
    )


  def main(args: Array[String]): Unit = {
    println(variables(sample))
  }
}
