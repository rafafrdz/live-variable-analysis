package mf.aarr.ast.liveVariableAnalysis.adt

/** Block Language syntax */
sealed trait Block {
  val label: Int
}

/** skip process */
case class SkipBlock(label: Int) extends Block

/** x := e */
case class AssignmentBlock(label: Int, ass: Assignment) extends Block

/** b in BExp */
case class ConditionalBlock(label: Int, condition: BExp) extends Block

/** v := exp */
case class Assignment(v: Var, exp: Exp)

