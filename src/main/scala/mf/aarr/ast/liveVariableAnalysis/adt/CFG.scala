package mf.aarr.ast.liveVariableAnalysis.adt

import mf.aarr.ast.liveVariableAnalysis.analysis.Graph

/** Control Flow Graph system trait */
trait CFG {
  val blocks: Array[Block]
  val graph: Graph[Int]
  lazy val ref: Map[Int, Block] = blocks.map(block => block.label -> block).toMap

}

object CFG {

  /** Set an initial array with LiveVariable(label, empty, empty) */
  def initialInOut(proces: CFG): Array[LiveVariable] = LiveVariable.initial(proces.blocks)

  /** Control Flow Graph factory method */
  def build(grph: Graph[Int], blcks: Array[Block]): CFG = new CFG {
    val blocks: Array[Block] = blcks
    val graph: Graph[Int] = grph
  }
}