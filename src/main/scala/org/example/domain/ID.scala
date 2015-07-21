package org.example.domain

import org.example.validation.Valid

import scalaz.{Tag, @@, \/, Equal}
import org.example.validation._
case class ID(id: String)


object ID {

  def equal: Equal[ID] = Equal.equalA


  def validate(n: Int): Validate[ID] = id => stringMinNLength(n)("ID")(id.id).map(_ => Tag(id))
}