package modeling

object Main {

  class UserC(name: String, age: Int) {}
  case class UserCC(name: String, age: Int)

  sealed trait Country
  case class USA() extends Country
  case class Other() extends Country

  sealed trait Contact
  case class Phone(country: Country, number: Long) extends Contact
  case class Email(address: String) extends Contact

  trait Unsealed
  case class A() extends Unsealed
  case class B() extends Unsealed

  sealed trait HttpResult[A]
  case class Success[A](value: A) extends HttpResult[A]
  case class Timeout[A]() extends HttpResult[A]
  case class HttpError[A](code: Int) extends HttpResult[A]

  trait Weather

  type WeatherResponse = HttpResult[Weather]

  def main(args: Array[String]): Unit = {

    val johnC = new UserC("John", 18)
    println(johnC)

    val johnCC = UserCC("John", 18)

    val olderJohn = johnCC.copy(age = 19)

    println(johnCC)

    val contact : Contact = Phone(USA(), 1)

    val instruction = contact match {
      case Phone(USA(), number) => s"call $number locally"
      case Phone(_, number) => s"call $number"
      case Email(email) => s"send an email to $email"
    }

    println(instruction)

    val un = A() : Unsealed

    // fails at runtime
    val z = un match {
      case B() => "a"
    }
  }
}
