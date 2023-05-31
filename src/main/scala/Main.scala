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
