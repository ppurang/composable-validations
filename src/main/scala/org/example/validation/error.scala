package org.example.validation

import argonaut.Argonaut._
import argonaut._
import org.example.domain.CodecHelper._

import scalaz.Maybe

case class ErrorCode(code: String)

object ErrorCode {
  
  implicit val strToAPICode : String => ErrorCode= ErrorCode(_)
  
  implicit val aeccodec: CodecJson[ErrorCode] = codec(
    _.code,
    ErrorCode(_))
}

case class ErrorField(field: String) {
  
  def parent(parent: ErrorField) = ErrorField(s"${parent.field}.${field}")

  def mParent(mp: Maybe[ErrorField]) : ErrorField = mp.cata(
    p => this.parent(p),
    this
  )

  def withIndex(index: Int) = ErrorField(s"${field}[$index]")
}

object ErrorField {
  val emptyField = ErrorField("")

  implicit val strToErrorField : String => ErrorField = ErrorField(_)

  implicit val afcodec: CodecJson[ErrorField] = codec(
    _.field,
    ErrorField(_))

}

case class Error(code: ErrorCode, field: ErrorField, msg: String)

object Error {

  def apply(code: ErrorCode,  msg: String) : Error =  Error(code, ErrorField.emptyField, msg)

  implicit val codec: CodecJson[Error] = casecodec3(Error.apply, Error.unapply)(
    "error_code", "error_field", "error_msg"
  )
}