Extension Variables with state in Kotlin
========================================

Why doesn't this work?

```kotlin
class Foo {}

var Foo.bar: String = "hello world"
```

The code above will give you an error about the extension property not
having a backing field. Essentially, all extensions are static methods.
Since the variable being declared isn't actually part of the class, it
can't store our variable as part of the class. This makes sense, but is
there any way around it?

----------

What you're meant to use extension variables for is static getter/setters.
Something like this:

```kotlin
class Foo {
    var name: String? = null
}

var Foo.bar: String
  get() = name ?: "N/A"
  set(value) { name = value }
```

That works great if you want to create some sort of facade or derived 
property based on the class's existing data. But it can't be used to store
new data.

The extension has access to public properties on the extended object, but
nothing private. Just like a static method. This is why it can't store any
state on the class, we're not actually changing the parent object, we're
just defining a global static method.

Crimes and Hashmaps
-------------------

If you want to store data per-class as an extension, it can be tricky.
But we can get something similar working by storing the data elsewhere,
like in a map associated with the class instance.

Ex:

```kotlin
private val fooBars = Map<Foo, String>()

var Foo.bar: String
  get() = fooBars[this] ?: "N/A"
  set(value) { fooBars[this] = value }
```

That works great! We've made a synthetic extension variable on our class!
No problem.

.. Okay, one problem: the code above creates a **memory leak**. Since the
code above is a statically defined global variable that holds a reference 
to the `Foo` object, the garbage collector will never collect it! If your 
application is short lived, or uses only a few of these objects, this could
be fine to ignore. But this is why this isn't a first-class feature in Kotlin.

We can improve the above code slightly by keeping an indirect reference
to the `Foo` class. Since Kotlin Multiplatform doesn't have a `WeakReference`
object, let's look at how to do that with just a hashcode:

Ex:

```kotlin
private val fooBars = Map<Int, String>()

var Foo.bar: String
  get() = fooBars[hashCode()] ?: "N/A"
  set(value) { fooBars[hashCode()] = value }
```

By changing the key to an `Int` and using the object's hashcode, the garbage
collector can now destroy the `Foo` object when appropriate. But this still
leaves the tombstone in our map, along with the data we're storing. In this
example it's just storing a `String`, but if that value was something more
complex, it would be a similar memory leak still.

If you're only running in the JVM, you can use a `WeakHashMap` to solve
this problem, and the values will eventually get garbage collected as well

Ex:

```kotlin
private val fooBars = WeakHashMap<Foo, String>()

var Foo.bar: String
  get() = fooBars[this] ?: "N/A"
  set(value) { fooBars[this] = value }
```

If Kotlin ever gets a proper `WeakReference` implementation in common code,
the above solution would work well on all platforms.
