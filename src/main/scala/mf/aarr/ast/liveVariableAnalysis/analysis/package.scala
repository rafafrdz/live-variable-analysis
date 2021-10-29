package mf.aarr.ast.liveVariableAnalysis

package object analysis {
  type Graph[T] = Map[Int, Option[Set[T]]]
}
