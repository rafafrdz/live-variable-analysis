package mf.aarr.ast.liveVariableAnalysis

import mf.aarr.ast.liveVariableAnalysis.adt.Exp

import scala.language.implicitConversions

package object api {
  private[api] implicit def to[T <: Exp](exp: Exp): T = exp.asInstanceOf[T]
}
