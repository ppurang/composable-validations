package org.example

import scalaz._, Scalaz._

package object validation {

  trait Valid

  def validate[A, B](field: ErrorField)(a: A)(implicit parent: Maybe[ErrorField], v: A => \/[(ErrorCode, String), B]) : ValidationNel[Error, B] =  {
    v(a).leftMap {
      case (code, msg) => Error(code, field.mParent(parent), msg)
    }.validation.toValidationNel
  }

  def validateAccumulate[A, B](field: ErrorField)(a: A)(implicit parent: Maybe[ErrorField], v: Maybe[ErrorField] => A => ValidationNel[Error, B]): ValidationNel[Error, B] = {
    v(parent.map(field.parent))(a)
  }

  def validateMaybe[A, B](field: ErrorField)(a: Maybe[A])(implicit parent: Maybe[ErrorField], v: A => \/[(ErrorCode, String), B]): ValidationNel[Error, Maybe[B]] = a.traverseU(
    v(_).leftMap {
      case (code, msg) => Error(code, field.mParent(parent), msg)
    }.validation.toValidationNel
  )

  def validateMaybeAccumulate[A, B](field: ErrorField)(a: Maybe[A])(implicit parent: Maybe[ErrorField], v: Maybe[ErrorField] => A => ValidationNel[Error, B]): ValidationNel[Error, Maybe[B]] = a.traverseU(v(parent.map(field.parent))(_))

  def validateMultipleAccumulate[A, B](field: ErrorField)(as: Vector[A])(implicit parent: Maybe[ErrorField], v: Maybe[ErrorField] => A => ValidationNel[Error, B]): ValidationNel[Error, Vector[B]] = {
    as.zipWithIndex.map(t => v(parent.map(field.parent(_).withIndex(t._2)))(t._1)).sequenceU
  }

  def validateMultiple[A, B](field: ErrorField)(as: Vector[A])(implicit parent: Maybe[ErrorField], v: A => \/[(ErrorCode, String), B]): ValidationNel[Error, Vector[B]] = as.zipWithIndex.map(t => validate(field.withIndex(t._2))(t._1)).sequenceU


  def validateString(code: ErrorCode, field: ErrorField, msg: String)(str: String)(p: String => Boolean = _.trim.nonEmpty)(implicit parent: Maybe[ErrorField]) : ValidationNel[Error, String]
  = validate(field)(str)(parent, s => p(s).fold(s.right, (code, msg).left))

  def validateMaybeString(code: ErrorCode, field: ErrorField, msg: String)(str: Maybe[String])(p: String => Boolean = _.trim.nonEmpty)(implicit parent: Maybe[ErrorField]) : ValidationNel[Error, Maybe[String]]
  = validate(field)(str)(parent, s => s.map(p(_).fold(s.right, (code, msg).left)).cata(identity, Maybe.empty.right)) //todo awkward .. missing something?

  //min1 works on containers of strings ..
  def minLength1[A] = minLengthN[A](1)

  //minn works on containers of strings ..
  def minLengthN[A]: Int => String => (A => String) => (String => A) => A => \/[String, A] = i => vc => to => from => value => {
    val tv = to(value).trim
    (tv.length < i).fold(
      vc.left,
      value.right
    )
  }

  def nonEmpty[A](code: ErrorCode, field: ErrorField, msg: String)(as: Seq[A])(implicit parent: Maybe[ErrorField]): ValidationNel[Error, Seq[A]] =
    (if (as.isEmpty) {
      Error(code, field.mParent(parent), msg).left
    } else {
      as.right
    }).validation.toValidationNel

}



