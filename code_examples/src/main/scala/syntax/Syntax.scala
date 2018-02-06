package syntax

object Syntax {

  class Hello(a: Int, b: Boolean) {
    def doThings(): Double = ???
  }

  def add3M(a: Int, b: Int, c: Int): Int = a + b + c

  def defaultParam(mandatoryInt: Int, defaultStr: String = "hello"): Double = ???

  object Singleton {
    val someConstant: String = "HELLO"
    def hello(): String = someConstant
  }

  def main(args: Array[String]): Unit = {

    val value = new Hello(1, false)
    val value1: Hello = new Hello(1, false)

    val increment0 = (x: Int) => x + 1
    val increment1: (Int => Int) = x => x + 1
    val increment2: (Int => Int) = _ + 1


    val l = List(0)

    assert(l.map(increment0) == List(1))
    assert(l.map(increment1) == List(1))
    assert(l.map(increment2) == List(1))
    assert(l.map(x => x + 1) == List(1))
    assert(l.map(_ + 1) == List(1))
    assert(l.map(1 + _) == List(1))

    val add3F: ((Int, Int, Int) => Int) = _ + _ + _

    println(add3F(1, 2, 3) == add3M(1, 2, 3))
    println(add3F.apply(1, 2, 3) == add3M(1, 2, 3))

    val add3MF: ((Int, Int, Int) => Int) = add3M

    println(add3MF(1, 2, 3) == add3M(1, 2, 3))

    println(avg2(0, 1))
  }

  def avg2(a: Double, b:Double) : Double = (a + b) / 2

  def a(): Unit = {
    val x = Singleton.someConstant
    Singleton.hello()
    defaultParam(1)
    defaultParam(1, "a")
  }
}
