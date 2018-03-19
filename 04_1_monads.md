# Syntax

## Monads and for comprehensions

In this section we'll use a narrow (and thus incomplete) definition of monad, in the context
of functional programming languages. Readers are encouraged to explore further.
This section is a pre-requisite for the section on side-effects.

A monad is an interface that a type satisfies. For a type `M` with a type parameter `A`,
we can say that `M` is a monad if it has a method
`def flatMap[B](f: A => M[B]): M[B]`. Let's see some examples of types that are monads:

- List: flatMap[B](f: A => List[B]): List[B]

Given a `User` with a list of possible `Contacts`:
```scala
case class User(name: String, contacts: List[Contact])

val allUsers : List[User] = getUsers()

val allContacts : List[Contact] = allUsers
  .flatMap(user => user.contacts)
```
we can get a list of all the contacts (for all users) by using `flatMap`.

- Option: flatMap[B](f: A => Option[B]): Option[B]

Given an `Account` which may or may not have an `Address`:
```
case class Account(id: String, address: Option[Address])

val account : Option[Account] = getAccount(id)
val accountAddress : Option[Address] = account
  .flatMap(acc => acc.address)
```

We can get an address of a given account, if both the account and the address
exist.

- Try: flatMap[B](f: A => Try[B]): Try[B]

Given two strings of user input, which we want to parse as integers:
```scala

val parsed1: Try[Int] = parseInt(userInput1)
val parsed2: Try[Int] = parseInt(userInput2)

val sum : Try[Int] = parsed1
  .flatMap(v1 =>
    parsed2
      .map(v2 => v1 + v2))
```
We can add the two numbers together, or fail if any of the inputs
is not a valid integer.

The unifying perspective is that `flatMap` let's us work withing a context (e.g. List, Option, Try)
by chaining computations that depend on each other.

If we look back at the last example we might find it a bit clumsy, and we're only working with values,
imagine trying to add 5 of those inputs! Fortunately Scala provides better
syntax for working with monads, that example can be written as:

```scala
val sum2 : Try[Int] = for {
  v1 <- parsed1
  v2 <- parsed2
} yield v1 + v2
```
Which is equivalent and much less noisy. This is called a for comprehension,
further details on which types can use this syntax can be found [here](https://docs.scala-lang.org/tour/for-comprehensions.html).

One does not really need to know that `Option` is a monad in order to use flatMap.
In fact, Option would still be a very useful structure if it did not have a `flatMap`
method. But it is interesting to see that a lot of seemingly unrelated types have this
common structure, and it is useful to have a name for it.
Moreover, there are abstractions that can work with any monad, without knowing
the specific type.
We'll discuss some in the section on side-effects.
