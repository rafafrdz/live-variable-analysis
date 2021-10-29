package mf.aarr.ast.liveVariableAnalysis.adt

/** Language of all Expressions */
sealed trait Exp

/** AExp is the Algebraic Expressions syntax */
sealed trait AExp extends Exp

/** n constant */
case class Const(n: Double) extends AExp

/** x variable */
case class Var(v: String) extends AExp

/** e1 + e2 */
case class Add(e1: AExp, e2: AExp) extends AExp

/** e1 - e2 */
case class Sub(e1: AExp, e2: AExp) extends AExp

/** e1 * e2 */
case class Mul(e1: AExp, e2: AExp) extends AExp

/** BExp is the Boolean Expressions syntax */
sealed trait BExp extends Exp

/** e1 <= e2 */
case class Leq(e1: AExp, e2: AExp) extends BExp

/** e1 == e2 */
case class Eq(e1: AExp, e2: AExp) extends BExp

/** b1 and b2 */
case class And(b1: BExp, b2: BExp) extends BExp

