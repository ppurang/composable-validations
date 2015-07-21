package org.example.domain

import scalaz.\/
import scalaz.syntax.either._
import scalaz.syntax.std.boolean._

case class Email(email: String)

object Email {
  private val emailRegex = {
    """[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,4}""".r
  }

  // \/[String, Email] is the same as  'String OR Email'
  implicit def validate: Email => \/[String, Email] = email => {
    emailRegex.findFirstIn(email.email) match {
      case Some(_) => email.right
      case _ => s"Not a valid email $email".left
    }
  }
}

case class ID(id: String)

object ID {

  type or[+A, +B] = \/[A, B]

  implicit def validate: ID => String or ID = id => (id.id.length > 0).fold(
    id.right,
    s"IDs can't be empty: '${id.id}'".left
  )

}