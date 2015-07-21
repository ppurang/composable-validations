name := "validations"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
   "org.scalaz" %% "scalaz-core" % "7.1.3"
)

initialCommands in console :=
  """
    |import org.example.domain._
    |import User._
    |
    |val vid = ID("1")
    |val vemail = Email("abcd@abcd.com")
    |
    |val ivid = ID("")
    |val ivemail = Email("hi")
    |
    |val user = User(vid, vemail)
    |val nuser = User(ivid, ivemail)
    |
  """.stripMargin





