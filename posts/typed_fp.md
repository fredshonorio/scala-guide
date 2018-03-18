---
title: Typed Functional Programming Principles
date: 2018-03-16
---

## Referential transparency
Referential transparency (RT) is the idea that a function produces no observable effects
other than its return value. This implies that the function always returns the same
output if given the same input, and that there is no problem (or side-effect) with calling the
function multiple times. Functions that are RT are also called pure.

Since pure functions do nothing more than produce an output, they can be
composed with other functions, we only have to ensure that the types align
(in our case, the compiler does this check).

Here are some things that are not referentially transparent:

* Writing to a global variable
* Taking a value from a shared mutable queue
* Printing a string to standard out
* Writing to a log file
* Reading bytes from a socket

There's a distinction to be drawn for these examples. Some have an effect on the outside
world, and others don't. We call the former IO (input/output). Both of them are
considered side-effects, as the result of the operation is not available in
the return value of the function.

It seems that we can't do anything useful with pure functions, a program seems to be pointless
if it can't interact with the real word. In a strict sense this is true,
and while there are pure ways of performing input/output (IO), we don't even need
to employ them to have many benefits of functional programming.

But let's first try to explore why side-effects are problematic.
Writing a test for a pure function is relatively simple, we provide the arguments
and test that the output is the one we expect. Since the function is pure, the test gives us
complete assurance that the function works for the given input. The behavior cannot
differ from one run to another (unless the code is changed).
Testing a function with side-effects requires that we capture the effect in some way, and test
that it was matches our expectations. To test that we have written a file, we have to read it,
to test that we made an HTTP call, we have to setup something to call on the other side,
to test that a mutable queue was written to, we have to read it, etc.
This appears to be always harder than testing a function without side-effects, where
you just look at the return value of the function. This alone makes pure functions
easier to maintain.

Hopefully we have made a persuasive case for pure functions. But we still have the nagging
problem that IO is useful and necessary, and so we start by cleanly separating pure computation
from side-effects. Let's start with a demonstrative example:
``` scala
def printGreeting(user: User): Unit = {
  println(s"Hello ${user.name}")
}
```
There is a pure computation that computes a greeting, mixed with the side-effect of printing it.
If we separate them:
``` scala
def greet(user: User): String =
  s"Hello ${user.name}"

def printGreeting(user: User): Unit = {
  val greeting = greet(user)
  println(greeting)
}
```
we have a cleaner separation of concerns. It might not be obvious why this is better
given such a trivial example, but it is an exercise worth doing with some familiar
piece of code. This separation between business logic and side-effects means that the logic
can be tested without testing the effect.
It can be seen that a function that calls an impure sub-function becomes impure, regardless
of whatever else it does. Given than pure functions are easier to test, we have
another incentive to separate pure from impure code as much as we can.

## Immutability
Immutability is the idea that values don't change over the course of the program.
If we want RT, it's useful that when we implement a function we have no risk of
mistankenly modifying our inputs. So it's easy to see how immutability is nice in a global sense:
we don't have to worry about modifying the inputs we are given and we don't have
to worry that the values we use as arguments to functions we call can be modified.
This is another advantage of using pure functions, it is less frequent that we have
to load a large chunk of the program into our brains to understand a small fragment of code. This property
is called local reasoning.
From a perspective of maintenance is useful to know that parts of the code won't
interact in less obvious ways, by mutating state along the way.
If they do we make this mutation explicit, by declaring the input and the "modified"
output.

One of the common cases where mutability seems necessary is to deal with collections.
In most cases we can better express our intent with methods like `filter`, `find`,
`map`, `head` etc, which produce immutable results.
This often communicates the intent better at the cost of imposing
knowledge of the API to the reader. These operations are so frequent that it's likely
a good trade-off.

Immutability also forces us to better describe dependencies between data. Since we can't have
partially initialized objects we are forced to separate the descriptions of our domain.
For instance, we have a `User` which has an `Address` and many other fields.
Most data comes from a service, but the address comes from a different service. We might
be tempted to create a user instance without initializing the address, but that would
mean that every function that takes a `User` must be valid when the `Address` is not
set. We can create a `PartialUser` that has no address, to represent this incomplete state,
and define useful functions that don't require the address. This also effectively
solves every case where the developer forgets to set a value, because it is impossible
to create a value without every argument that it requires. More generally, where
we might find it easy to use the same type to describe concepts that are semantically
distinct, we are encouraged to make that distinction apparent, in part because
of immutability.

Since immutable data can be shared freely, it can be uses in parallel
computation without having to worry about locks or race conditions.

## Describing computation with types
The "typed" part of typed FP denotes an approach that relies on the type-system
to describe our problem. We'll start with example were we don't rely sufficiently on the type-system,
in Java.

``` java
List<Character> ints = //...
ints.get(3); // Can throw IndexOutOfBoundsException
ints.indexOf('A'); // Can return -1
Map<String, Integer> m = //...
m.get("hello"); // Can return null
```

If we look at the signatures of these functions, we see that they are omitting some information:

* In List: ```T get(int index)``` does not describe what happens when the user
attempts to read a value at an index that is not defined
* Also in List: ```int indexOf(E value)``` - the function might return `-1`, a value that is a valid
integer, but does not describe a position, potentially causing obscured errors when using
the index for arithmetic
* In Map: ```T get(K key)``` - the function might return a `null`, which might be mistakenly
passed to a function that does not handle nulls

In every case, the API is using special cases of values that the user must handle
but such cases are not explicit in the type, and thus the compiler can't help us
check that we have handled them.

For these specific cases we can use an ```Option``` type, which is essentially
a "list" with either 0 or 1 values, where we describe the possible state of having
no value. We are then encouraged to deal with both cases.

There also other cases where an `Option` is insufficient, for instance when we want
an informative error message. Alternatives would be `Either` or `Try`.

The main takeaway is that we should endeavor to be precise about the types in our
programs because not doing so forces us to keep those semantics in our head (e.g. `foo` throws, but `bar` doesn't)
where the compiler would be perfectly glad to help.

This helps maintenance because it makes it easier to describe contracts and
semantics of our code, which allows us to refactor without fear of breaking
functionality. "Breaking" code simply wouldn't compile.

Making sure that incorrect programs won't compile is our goal, likely one that
we never reach completely but aim to get closer and closer. We describe several techniques
for this in later chapters.
