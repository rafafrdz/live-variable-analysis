package mf.aarr.ast.liveVariableAnalysis.analysis

import mf.aarr.ast.liveVariableAnalysis.adt._

import scala.annotation.tailrec

object AnalysisFunction {

  def out[T](i: T*): Option[Set[T]] = Option(Set(i: _*))

  def end[T]: Option[Set[T]] = Option.empty[Set[T]]

  /** Given an expression `e` return all variable `Var` ocurring in it */
  def variables(e: Exp): Set[Var] =
    e match {
      case exp: AExp => exp match {
        case Const(_) => Set.empty[Var]
        case v@Var(_) => Set(v)
        case Add(e1, e2) => variables(e1) ++ variables(e2)
        case Sub(e1, e2) => variables(e1) ++ variables(e2)
        case Mul(e1, e2) => variables(e1) ++ variables(e2)
      }
      case exp: BExp => exp match {
        case Leq(e1, e2) => variables(e1) ++ variables(e2)
        case Eq(e1, e2) => variables(e1) ++ variables(e2)
        case And(b1, b2) => variables(b1) ++ variables(b2)
      }
    }

  private def kill(block: Block): Set[Var] = {
    block match {
      case AssignmentBlock(_, ass) => Set(ass.v)
      case _ => Set.empty[Var]
    }
  }

  private def gen(block: Block): Set[Var] = {
    block match {
      case ConditionalBlock(_, condition) => variables(condition)
      case AssignmentBlock(_, ass) => variables(ass.exp)
      case SkipBlock(_) => Set.empty[Var]
    }
  }

  /** Given a block of the GFC and the set of live variables at the exit of that block
   * return the set of live variables at the entrance of that block.
   *
   * LVIn_k = (LVOut_k - Kill_k) U Gen_k
   *
   * */
  def transfer(block: Block, lvOut: Set[Var]): Set[Var] =
    lvOut.diff(kill(block)) ++ gen(block)

  private def liveIn(inOut: LiveVariable)(implicit prcss: CFG): LiveVariable =
    inOut.copy(in = transfer(prcss.ref(inOut.label), inOut.out))

  private def liveOut(inOut: LiveVariable, analysed: Map[Int, LiveVariable])(implicit prcss: CFG): LiveVariable = {
    val out: Set[Var] =
      prcss.graph(inOut.label) match {
        case Some(nexts) => nexts.map(i => analysed(i).in).reduce(_.union(_))
        case None => Set.empty[Var]
      }
    analysed(inOut.label).copy(out = out)
  }

  /** ******************************
   * Live Variable Analysys method
   * *******************************
   * Given a Control Flow Graph return the result of the live variable analysis
   * */

  def analysis(implicit prcss: CFG): Array[LiveVariable] = {
    val initial: Array[LiveVariable] = CFG.initialInOut(prcss)

    @tailrec
    def run(input: Array[LiveVariable]): Array[LiveVariable] = {
      val liveIn: Array[LiveVariable] = liveInAux(Array.empty[LiveVariable], input)
      val refLVs: Map[Int, LiveVariable] = LiveVariable.toMap(liveIn)
      val output: Array[LiveVariable] = liveOutAux(Array.empty[LiveVariable], liveIn, refLVs)
      if (input sameElements output) output else run(output)
    }

    @tailrec
    def liveInAux(analysed: Array[LiveVariable], rest: Array[LiveVariable]): Array[LiveVariable] = {
      if (rest.isEmpty) analysed
      else {
        val (inout, tail) = (rest.head, rest.tail)
        val inoutIn: LiveVariable = liveIn(inout)
        liveInAux(analysed :+ inoutIn, tail)
      }
    }

    @tailrec
    def liveOutAux(analysed: Array[LiveVariable], rest: Array[LiveVariable], all: Map[Int, LiveVariable]): Array[LiveVariable] = {
      if (rest.isEmpty) analysed
      else {
        val (inout, tail) = (rest.head, rest.tail)
        val inoutOut: LiveVariable = liveOut(inout, all)
        liveOutAux(analysed :+ inoutOut, tail, all)
      }
    }

    run(initial)
  }

}
