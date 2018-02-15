# Syntax

## Classes and Singleton Objects

As a language with an object-oriented language foundation, Scala has classes, which
are instantiated by objects.
A class can be declared as:

``` scala
class Hello(a: Int, b: Boolean) {
    def doThings() : Double = ???
}
```
which is a class with two members: `a` and `b` and a method `doThings` which returns
a `Double`.
To create a value (object) of this class (type) we simply call:
``` scala
val value = new Hello(1, false)
```
In contrast with Java, the type of `value` is inferred. If we want to make it explicit
we could write:

``` scala
val value1 : Hello = new Hello(1, false)
```
But this is often not necessary. The annotation of the return type for methods,
e.g. `Double` in `def doThings() : Double = ???` is also optional (the compiler
will try to infer it) but should always be explicitly declared. This is because it becomes
easy to change the effective type of a method by mistake, and cause seemingly
unrelated compiler errors.

We can also see that Scala expressions and statements don't need to be terminated with a
semicolon.

There are no static methods in Scala, to declare methods that don't depend on the
state of a class instance we can use singleton objects:

``` scala
object Singleton {
    val someConstant: String = "HELLO"
    def hello(): String = someConstant
}
```
Like the name implies, there only exists one instance of a singleton object.
They can be used like this:
``` scala
Singleton.someConstant
Singleton.hello()
```

## Methods
Scala favors expressions over statements, so we get a cleaner syntax when the method
is a single expression. All these definitions are equivalent:

``` scala
def avg2(a: Double, b:Double) : Double = {
  val sum = a + b
  return sum / 2
}

def avg2(a: Double, b:Double) : Double = {
  val sum = a + b
  sum / 2
}

def avg2(a: Double, b:Double) : Double = (a + b) / 2
```
Notice that `return` is optional, the value of the last expression is the
result of the method. Additionally, the last example does not need brackets.

Like in Java, Scala methods have access to the objects scope.

Using methods is very similar to Java, although the `.` can be omitted:
``` scala
obj.method()
obj method()
```
This second form of syntax should probably not be used, for consistency.

It is possible to define methods inside other methods, this often helps
clarity and cleans up the namespace. The canonical factorial function
can be written like this:

``` scala
def factorial(x: Int): Int = {
    def fact(x: Int, accumulator: Int): Int = {
        if (x <= 1) accumulator
        else fact(x - 1, x * accumulator)
    }
    fact(x, 1)
}
```

### Anonymous functions (lambdas)
As with Java, Scala allows defining functions that can be passed as values:
``` scala
val increment               = (x: Int) => x + 1
val increment: (Int => Int) = x        => x + 1
val increment: (Int => Int) =             _ + 1
```
These are all the same function, we just needed to give some hints
to the compiler so that it could infer the correct type. Doing this might
be necessary if we're just declaring the `increment` value, but using it
anonymously won't usually require it:

``` scala
val someList : List[Int] = ???

someList.map(_ + 1) == someList.map(x => x + 1)
```
Notice that the underscore syntax for anonymous functions uses the arguments positionally, so
the method:
``` scala
def add3(a: Int, b:Int, c:Int) : Int = a + b + c
```
is equivalent to:
``` scala
val add3: ((Int, Int, Int) => Int) = _ + _ + _
```
However, this means that the same parameter cannot be used twice, so:
``` scala
val double = (x: Int) = x = x + x
val double = (x: Int) = _ + _
```
the second example won't compile.

### `apply`
In our `add3` we defined a function as a value, how do we use it? Precisely the same
way use the method:

``` scala
def add3M(a: Int, b:Int, c:Int) : Int = a + b + c
val add3F: ((Int, Int, Int) => Int) = _ + _ + _

add3F(1, 2, 3) == add3M(1, 2, 3)
```
This is because `add3F` is actually a value of type `Function3`, which has
an `apply` method. The compiler provides some syntactic sugar so that we don't
need to write `apply` and so:
``` scala
add3F.apply(1, 2, 3) == add3F(1, 2, 3)
```

This can be useful for some APIs besides `Function`, for instance,
Scala does not have array indexing syntax like Java (e.g.: `arr[3]`),
but the `List` type uses `apply` for this, so we can write `list(3)`
to access a position.

### Companion object
TODO

### Default parameters
Methods can have default parameters (function values can't):
```scala
def defaultParam(mandatoryInt: Int, defaultStr: String = "hello"): Double = ???
```
Which means we can omit parameters at the call site:

``` scala
defaultParam(1)
defaultParam(1, "a")
```

### Named arguments
TODO

### Currying, argument lists
TODO

## Operators
TODO

## Packages, imports
TODO
