package org.example.domain

import java.util.Locale

import org.example.validation
import org.example.validation._

import scalaz._, Scalaz._
import common._
import argonaut._, Argonaut._
import CodecHelper._

case class User(id: ID, mfn: Maybe[FirstName], email: Email, addresses: Vector[Address])

object User {
  val idField = "user_id"
  val firstNameField = "user_first_name"
  val emailField = "user_email"
  val addressesField = "user_address"

  implicit val userCodec : CodecJson[User] = casecodec4(User.apply, User.unapply)(idField, firstNameField, emailField, addressesField)


  def validateUser(user: User)(implicit mparent: Maybe[ErrorField] = ErrorField("user").just) : ValidationNel[Error, User @@ Valid] = (validate(idField)(user.id) |@| validateMaybe(firstNameField)(user.mfn) |@| validate(emailField)(user.email) |@| validateMultipleAccumulate(addressesField)(user.addresses) )((_,_,_,_) => Tag(user) )

}


case class FirstName(fn: String)

object FirstName {
  val fnErrorCode: ErrorCode = "invalid-first-name"

  implicit val fnCodec: CodecJson[FirstName] = codec(
    _.fn,
    FirstName(_))

  implicit val equals = Equal.equalA[FirstName]

  implicit def validate: FirstName => \/[String, FirstName] =
    validation.minLength1[FirstName]("first-name can't be empty")(_.fn)(FirstName(_))

  implicit def validate2: FirstName => \/[(ErrorCode, String), FirstName] = fn =>
    validate(fn).leftMap((fnErrorCode, _))

}

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

  implicit val emailCodec: CodecJson[Email] = codec(
    _.email,
    Email(_))

  val emailErrorCode = "invalid-email"

  implicit def validate2 : Email => \/[(ErrorCode, String), Email] = validate(_).leftMap((emailErrorCode, _))

}

case class ID(id: String)

object ID {
  implicit def validate: ID => String or ID = id => (id.id.length > 0).fold(
    id.right,
    s"IDs can't be empty: '${id.id}'".left
  )

  implicit val idCodec: CodecJson[ID] = codec(
    _.id,
    ID(_))

  val idErrorCode: ErrorCode = "invalid-id"

  implicit def validate2 : ID => \/[(ErrorCode, String), ID] = validate(_).leftMap((idErrorCode, _))

}

case class Address(id: ID, addressedTo: Maybe[String], street: String, city: String, country: Country)

object Address {
  val idField = "address_id"
  val addressedToField = "address_to"
  val streetField = "address_street"
  val cityField = "address_city"
  val countryField = "address_country"

  val addressedToInvalidErrorCode = "addressed-to-invalid"
  val streetInvalidErrorCode = "street-invalid"
  val cityInvalidErrorCode = "city-invalid"

  implicit def validate2: Maybe[ErrorField] => Address => ValidationNel[Error, Address] = implicit parent => ad => (
    validation.validate(idField)(ad.id) |@|
      validation.validateMaybeString(addressedToInvalidErrorCode, addressedToField, s"Addressed-to '${ad.addressedTo}' is empty.")(ad.addressedTo)() |@|
      validation.validateString(streetInvalidErrorCode, streetField, s"Street '${ad.street}' is empty.")(ad.street)() |@|
      validation.validateString(streetInvalidErrorCode, cityField, s"City '${ad.city}' is empty.")(ad.city)() |@|
      validation.validate(countryField)(ad.country))(
      (_, _, _, _, _) => ad
    )

  implicit val addressCodec : CodecJson[Address] = casecodec5(Address.apply, Address.unapply)(idField, addressedToField, streetField, cityField, countryField)

}


case class Country(c: String)

object Country {
  private def exists(country: Country): Boolean = Locale.getISOCountries.exists(_ === country.c)

  val countryErrorCode : ErrorCode = "country-invalid"

  implicit def validate: Country => \/[String, Country] = cn =>
    if (exists(cn)) cn.right
    else s"invalid country ${cn.c}".left


  implicit def validate2: Country => \/[(ErrorCode, String), Country] = validate(_).leftMap((countryErrorCode, _))

  implicit val countryCodec: CodecJson[Country] = codec(_.c, Country(_))

  implicit val equals = Equal.equalA[Country]
}
