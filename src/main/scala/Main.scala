package implicits

/**
  * Example of Implicit Implicit.
  * Implicit conversions like this are frowned upon.
  *
  * When they get applied, you don't really see them.
  */
object Implicits extends App {

  final case class Apple(weightInGrams: Int, color: String)
  final case class Orange(weightInGrams: Int)

  def func(orange: Orange): Unit = {
    println(
      "We're passing Apple to a function that expects an Orange, " +
      "and somehow magically it works."
    )
  }

  func(
    Apple(
      weightInGrams = 300,
      color = "red"
    )
  )

  implicit def AppleCanBeUsedAsOrange(apple: Apple): Orange =
    Orange(apple.weightInGrams)

}

/**
  * Extension Methods are used to create powerful DSLs.
  * Libraries like ScalaTest won't be possible without them.
  */
object ExtensionMethods extends App {

  final case class Apple(weightInGrams: Int, color: String)
  final case class Orange(weightInGrams: Int)

  def func(orange: Orange): Unit = {
    println(
      "We're passing Apple to a function that expects an Orange, " +
      "and somehow magically it works."
    )
  }

  func(
    Apple(
      weightInGrams = 300,
      color = "red"
    ).toOrange // now it seems like Apple has a new method called toOrange.
  )

  // It produces an AppleWrapper instead of and Orange
//   implicit def AppleWrapper(apple: Apple): AppleWrapper = new AppleWrapper(apple)

  // It's common to put these in package objects.
  final implicit class AppleOps(private val self: Apple) extends AnyVal {
    def toOrange: Orange = Orange(self.weightInGrams)
  }

  // Extension method is a pattern that's used in many languages.

}

object ImplicitHarmful extends App {

  // Only methods that end with : are right-associative.
  println("1" +: Seq("2", "3"))
  // which is same as
  println(Seq("2", "3").+:("1"))

  println("1" + Seq("2", "3"))

  println(Seq("2", "3") :+ "4")

  // If you accidentally miss : before the +, a different implicit method
  // would kick-in that would do string concatenation.
  println(Seq("2", "3") + "4")
}

object ImplicitParamsDangerous extends App {

  def method(a: Int)(implicit b: Int, c: Int): Int = {
    return a + b + c
  }

  implicit val whatever: Int = 4

  println(method(1)(4, 4)) // produces 9
  println(method(1))       // produces 9

}

object ImplicitParamsSafe extends App {

  def method(a: Int)(implicit b: First, c: Second): Int = {
    return a + b.value + c.value
  }

  final case class First(value: Int)
  final case class Second(value: Int)

  implicit val whatever: First   = First(4)
  implicit val whatever2: Second = Second(4)

  println(method(1)(First(4), Second(4))) // produces 9
  println(method(1))                      // produces 9

  // Same function as the `implicitly` function in Predef.
  def getIt[It](implicit it: It): It = it
  println(getIt[First])

  // Using `implicitly` directly.
  println(implicitly[First])

}
