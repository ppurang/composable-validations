name := "validations"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
   "org.scalaz" %% "scalaz-core" % "7.1.7",
   "io.argonaut" %% "argonaut" % "6.1"
)

initialCommands in console :=
  """
    |import org.example.domain._
    |import scalaz._, Scalaz._
    |import argonaut._, Argonaut._
    |import org.example.validation._
    |import User._
    |
    |val vid = ID("u-1")
    |val vaddress_id = ID("add-1")
    |val vemail = Email("abcd@abcd.com")
    |val vcountry = Country("DE")
    |val vaddressedTo = "to Mr. Valid"
    |val vstreet = "validity street"
    |val vcity = "Vcity"
    |val vaddress = Address(vaddress_id, vaddressedTo.just, vstreet, vcity, vcountry)
    |
    |val ivid = ID("")
    |val iaddress_id = ID("")
    |val ivemail = Email("hi")
    |val icountry = Country("NEC")
    |val iaddressedTo = ""
    |val istreet = ""
    |val icity = ""
    |val iaddress = Address(iaddress_id, iaddressedTo.just, istreet, icity, icountry)
    |
    |
    |val user = User(vid, FirstName("first-name").just, vemail, vaddress_id.just, Vector(vaddress))
    |val userIDA = User(vid, FirstName("first-name").just, vemail, ID("invalid-address-id").just, Vector(vaddress))
    |val userIAddresses = User(vid, FirstName("first-name").just, vemail, ID("invalid-address-id").just, Vector(vaddress, vaddress.copy(id = ID(""))))
    |val nuser = User(ivid, FirstName("").just, ivemail, iaddress_id.just, Vector(iaddress))
    |
    |
    |
  """.stripMargin





