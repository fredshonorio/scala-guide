---
title: Modeling
date: 2018-03-19
---


While developing applications we will want to model entities and behaviors
from our domain.
In Java we do this with classes, which Scala supports, using this [syntax](https://docs.scala-lang.org/tour/classes.html).
Concepts like private members, inheritance/overloading can be expressed
but they not be as useful as they are in Java.
Scala classes are also similar to Java because `toString`, `equals` and `hashCode`
don't have useful implementations by default, and this must be overloaded if
they are required.

## Case classes
Case classes are an alternative that improves over regular classes.
This is an example of a declaration:

``` scala
case class User(name: String, age: Int)
```
Case classes are immutable by default, have valid implementations of
`toString`, `equals` and `hashCode`, and have a `copy` method to
"modify" values (make an updated copy).
Scala has [named arguments](https://docs.scala-lang.org/tour/named-arguments.html) and
[default parameter values](https://docs.scala-lang.org/tour/default-parameter-values.html)
which means we can use `copy` like this:

``` scala
val olderJohn = john.copy(age = 19)
```

## Sealed traits

Case classes allow us to define a type where a value is composed of values of many types
(User is composed of a string and an int) but we can also declare a type whose
values are one of many types. For example, if we need to contact a user, we might
use an email address or a phone number. We'd express this type like so:

``` scala
sealed trait Contact
case class Phone(country: Country, number: Long) extends Contact
case class Email(address: String) extends Contact
```
A `trait` is equivalent to a Java `interface`, in fact this can be translated to

``` java
interface Contact {
    static class Phone extends Contact {...}
    static class Email extends Contact {...}
}

```
where `Phone` is simply a subtype of `Contact`.
There is a big difference in the Scala version, the `sealed` modifier indicates that
there can be no other subclasses of `Contact` (the  hierarchy is closed).
This is useful because we can't do much with a `Contact`, at some point we have to
know which variant it is, well do it like this:

``` scala
val contact : Contact = Email("hello@hel.lo")

val instruction = contact match {
    case Phone(_, number) => s"call $number"
    case Email(email) => s"send an email to $email"
}
```
In this example we "forget" that `contact` is an `Email`, for demonstration. Since
we only know it is an abstract `Contact` we use `match` to declare what to do for each case.
For `Phone(_, number)` we ignore the country (`_` most times means that we know a value should be there, but we don't bind a variable to it),
and bind the phone number to a variable named `number`. On the right side we can use the values we bind.
We can be more specific in our matches:

``` scala
val instruction = contact match {
    case Phone(USA(), number) => s"call $number locally"
    case Phone(_, number) => s"call $number"
    case Email(email) => s"send an email to $email"
}
```
This mechanism is called pattern matching, more on the syntax [here](https://docs.scala-lang.org/tour/pattern-matching.html).

An important part of sealed hierarchies is that failing to match all variants
will cause a compile-time error, if the compiler is properly configured.
From a maintenance perspective, this means we are free to add (and remove!) variants to our types
and we can be sure that the compiler will warn us of every time we forget to handle a case.
This is why this idea is so powerful, if we model our domain (exhaustively we
get a lot of guarantees. Without exhaustive pattern matching the developer is forced
to remember where the different branches occur in the code, risking incorrect behavior
or runtime errors.

## ADTs
Algebraic data types (ADT) are the language-agnostic name for this pattern we've seen with
sealed traits. We have product types: a `User` is a name _AND_ an age, and sum types:
a `Contact` is a phone number _OR_ an email address. ADTs let us mix and match products
and sums to describe the domain. Sealed traits and case classes are a way to encode ADTs
in Scala.

Although our examples used a very high-level domain, it turns out that ADTs are
useful everywhere. In other others, everything is the domain. For instance, if we
need to do HTTP requests and handle timeouts in a particular way, we can use this generic
type:

``` scala
sealed trait HttpResult[A]
case class Success[A](value: A)    extends HttpResult[A]
case class Timeout[A]()            extends HttpResult[A]
case class HttpError[A](code: Int) extends HttpResult[A]
```
which represents the timeout as one of the variants.

Given that ADTs give us a very precise encoding for domain entities we should aim
to use them as much as possible inside our program, and keep looser representations at the edges.
An example of looser representations is using json values directly instead of converting them
to a case class (or similar) or using an string to describe an url. In both cases
we have a general type that can be mapped to a concrete one, but there is a space of
values where the mapping is invalid. By using these loose types at the edges we
get a form of validation. Additionally we get a clean separation between the "transport"
and the data. In the case of json, we can change libraries or even change format completely,
and our core logic would be the same.

A good example of designing with ADTs can be seen in [this video](https://www.youtube.com/watch?v=IcgmSRJHu_8)
(the first 8 minutes should suffice), other articles [here](https://fsharpforfunandprofit.com/posts/designing-with-types-making-illegal-states-unrepresentable/) and
[here](http://blog.jenkster.com/2016/06/functional-mumbo-jumbo-adts.html).

## Aliases
TODO

## Newtypes
TODO
https://github.com/estatico/scala-newtype
https://github.com/milessabin/shapeless/blob/master/examples/src/main/scala/shapeless/examples/newtype.scala
http://eed3si9n.com/learning-scalaz-day3

## Refinement types
TODO
https://github.com/fthomas/refined

## Checked string literals
TODO
https://github.com/propensive/contextual
