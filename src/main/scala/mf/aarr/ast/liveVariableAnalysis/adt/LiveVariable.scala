package mf.aarr.ast.liveVariableAnalysis.adt

/** Live Variable datatype */
case class LiveVariable(label: Int, in: Set[Var], out: Set[Var]) {
  override def toString: String = s"block-${label}: lvIn:${in.mkString("{", ",", "}")}, lvOut:${out.mkString("{", ",", "}")}"
}

object LiveVariable {
  private def sil: Set[Var] = Set.empty[Var]

  private def empty(label: Int): LiveVariable = LiveVariable(label, sil, sil)

  def initial(blcks: Array[Block]): Array[LiveVariable] = blcks.map(blck => empty(blck.label))

  def toMap(lvs: Array[LiveVariable]): Map[Int, LiveVariable] = lvs.map(lv => lv.label -> lv).toMap
}

