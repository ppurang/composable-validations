package org.example

import scalaz.{Tag, \/, @@}

import scalaz.syntax.either._

package object validation {

  type Validate[A] = A => \/[String, A @@ Valid]

  def stringMinNLength(n: Int)(ctxt: String): Validate[String] = candidate =>
    if (candidate.size < n) s"$ctxt: '$candidate' is less that $n in length.".left else Tag[String, Valid](candidate).right
}
