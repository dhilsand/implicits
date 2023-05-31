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

  // Funtion that takes an orange.
  def func(orange: Orange): Unit = {
    println(
      "We're passing Apple to a function that expects an Orange, " +
      "and somehow magically it works."
    )
  }

  // We pass an Apple to a function that takes an orange. The Apple is implicitly converted to Orange.
  func(
    Apple(
      weightInGrams = 300,
      color = "red"
    )
  )

  // Takes an Apple and produces an Orange. Marked implicit.
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

object AdhocPolymorphism extends App {

  trait Printable[A] {
    def print(a: A): Unit
  }

  // Implementation of print defined for String type.
  implicit val stringPrintable: Printable[String] = new Printable[String] {
    def print(a: String): Unit = println(s"String: $a")
  }

  // Implementation of print defined for Int type.
  implicit val intPrintable: Printable[Int] = new Printable[Int] {
    def print(a: Int): Unit = println(s"Int as String: ${a.toString}")
  }

  // The correct implementation is implicitly injected based on the type parameter.
  def printIt[A](a: A)(implicit printable: Printable[A]): Unit = {
    printable.print(a)
  }

  // This is another way for writing the printIt method.
  // The syntax [A: Printable] is a context bound in Scala. 
  // It's a shorthand that says "for some type A, given that A has an implicit value of type Printable[A] in scope". 
  // It's a shorter way of expressing that an implicit Printable[A] is required.
  def printIt2[A: Printable](a: A): Unit = {
    val instance = implicitly[Printable[A]]
    instance.print(a)
  }

  // -------------
  // Type classes 
  // -------------
  // In Scala, a context bound is a type constraint that is expressed 
  // with the syntax [A: B] in a type parameter list. It requires that 
  // an implicit value of type B[A] is available.
  // Context bounds are a syntactic sugar that effectively represents the 
  // need for evidence of a type class for a certain type. They are often 
  // used in combination with the implicitly function to retrieve the 
  // implicit value, which provides the functionality associated with the type class.
  

  // In Scala, a type class is a pattern that allows you to add new behavior to 
  // existing classes without modifying them, and without needing to control their 
  // source code. It's a form of ad-hoc polymorphism.
  // In the context of type classes, "evidence" usually means an instance of a type 
  // class for a specific type. So, "evidence of a Show[A] type class for a certain type A" 
  // would mean an implicit value of type Show[A].
  // So, when we say that a context bound represents the need for evidence of a type class 
  // for a certain type, we mean that it requires the presence of a specific type class 
  // instance for a given type. It's called "evidence" because the presence of this instance 
  // proves to the compiler that the necessary behavior (as defined by the type class) exists 
  // for the given type.

  printIt(123)
  printIt("str")

  printIt2(123)
  printIt2("str")

}
