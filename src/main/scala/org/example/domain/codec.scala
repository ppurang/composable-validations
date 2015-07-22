package org.example.domain

import argonaut._, Argonaut._
import scalaz.Maybe


object CodecHelper {

  def codec[A](to: A => String, from: String => A): CodecJson[A] = CodecJson(
    a => jString(to(a)),
    c => c.as[String].flatMap(s => DecodeResult.ok(from(s)))
  )

  def codecMaybe[A](to: A => String, from: String => Maybe[A], context: String): CodecJson[A] = CodecJson(
    a => jString(to(a)),
    c => c.as[String].flatMap(s => from(s).cata(
      DecodeResult.ok,
      DecodeResult.fail(s"Not a valid $context: '$s'", c.history))
    )
  )
}