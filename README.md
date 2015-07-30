# Meetup Validations Demo

This demo-code is part of the material presented at: http://www.meetup.com/Scala-Berlin-Brandenburg/events/223960785/

## REPL

Launch sbt and at the sbt REPL

```scala

> console

[info] Starting scala interpreter...
[info]
import org.example.domain._
import scalaz._
import Scalaz._
import argonaut._
import Argonaut._
vid: org.example.domain.ID = ID(u-1)
vaddress_id: org.example.domain.ID = ID(add-1)
vemail: org.example.domain.Email = Email(abcd@abcd.com)
vcountry: org.example.domain.Country = Country(DE)
vaddressedTo: String = to Mr. Valid
vstreet: String = validity street
vcity: String = Vcity
vaddress: org.example.domain.Address = Address(ID(add-1),Just(to Mr. Valid),validity street,Vcity,Country(DE))
ivid: org.example.domain.ID = ID()
iaddress_id: org.example.domain.ID = ID()
ivemail: org.example.domain.Email = Email(hi)
icountry: org.example.domain.Country = Country(NEC)
iaddressedTo: String = ""
istreet: String = ""
icity: String = ""
Welcome to Scala version 2.11.7 (Java HotSpot(TM) 64-Bit Server VM, Java 1.8.0_31).
Type in expressions to have them evaluated.
Type :help for more information.

scala> validateUser(user).fold(_.asJson.spaces2, Tag.unwrap(_).asJson.spaces2)
res11: String =
{
  "user_id" : "u-1",
  "user_email" : "abcd@abcd.com",
  "user_default_address" : "add-1",
  "user_address" : [
    {
      "address_city" : "Vcity",
      "address_id" : "add-1",
      "address_street" : "validity street",
      "address_country" : "DE",
      "address_to" : "to Mr. Valid"
    }
  ],
  "user_first_name" : "first-name"
}

scala> validateUser(nuser).fold(_.asJson.spaces2, Tag.unwrap(_).asJson.spaces2)
res12: String =
[
  {
    "error_code" : "invalid-id",
    "error_field" : "user.user_id",
    "error_msg" : "IDs can't be empty: ''"
  },
  {
    "error_code" : "invalid-first-name",
    "error_field" : "user.user_first_name",
    "error_msg" : "first-name can't be empty"
  },
  {
    "error_code" : "invalid-email",
    "error_field" : "user.user_email",
    "error_msg" : "Not a valid email Email(hi)"
  },
  {
    "error_code" : "invalid-id",
    "error_field" : "user.user_address[0].address_id",
    "error_msg" : "IDs can't be empty: ''"
  },
  {
    "error_code" : "addressed-to-invalid",
    "error_field" : "user.user_address[0].address_to",
    "error_msg" : "Addressed-to 'Just()' is empty."
  },
  {
    "error_code" : "street-invalid",
    "error_field" : "user.user_address[0].addr...

scala>


```


## Code

Demo code is in the package `org.example.domain`. Validation related code is in the package `org.example.validation`.


## Questions/Clarifications

Do open an issue in case you'd like to know something more or find something that doesn't work as expected.



## License


Licensed under MIT License -- please look at the LICENSE.txt for the compete text.
