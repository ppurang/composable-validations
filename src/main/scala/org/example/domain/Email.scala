package org.example.domain

case class Email(email: String) {
  require(Email.emailRegex.findFirstIn(email).isDefined, "ewwwww")
}

object Email {
  private val emailRegex = """[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,4}""".r
}
