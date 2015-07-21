package org.example.domain

import org.example.validation._

import scalaz.{Tag, @@, Equal}

trait UserId

object UserId {
  def validate: Validate[ID @@ UserId] =  id => stringMinNLength(10)("User-Id")(Tag.unwrap(id).id).map(_ => Tag(id))
}

case class User(id: ID @@ UserId, name: String)

object User {

  def UserId(id: ID) : ID @@ UserId = Tag[ID, UserId](id)

  def equal: Equal[User] = Equal.equalA

}