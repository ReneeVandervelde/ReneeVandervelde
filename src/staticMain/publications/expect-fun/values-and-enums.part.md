Values and Enums in Kotlin
==========================

A while back I [wrote][1] about why I use value classes in place of enums in
Kotlin. I still follow this practice, but have some updated notes that
I want to share, as well as providing some additional pros for each strategy.

[1]: https://medium.com/@ReneeVandervelde/values-not-enums-1223f85c7f7

The Problem With Plain Values
-----------------------------

Many applications rely on plain values for options. In some applications,
this may take the place of a string, and in others an integer. On Android,
for example, we frequently see Integer constants:

```kotlin
intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
```

Where the constant is defined in Android as:

```java
public static final int FLAG_ACTIVITY_NEW_TASK = 268435456;
```

The benefit of this strategy is partially for performance. While the
performance benefit of an int over an enum is marginal at best, platforms
such as Android try not to make assumptions about the constraints on your
application, and bias towards the more performant variant.

Another benefit I don't often hear talked about is API stability. The Int
type is relatively universal and unbound. Since we're not fixed to a single
set of supported values, the platform can easily change the value sets without
needing careful alignment between libraries. This will become more clear when
digging into using enums.

The downside of using plain values is that it's fairly ambiguous. In addition
to passing the pre-defined constants in, we could also pass
*just about anything* as one of these options. We also lose auto-complete
features much of the time.

Though Android has addressed some of these deficiencies with annotations
such as ``@IntDef`, not all platforms are targeting Android and will rely
on these. In particular, Kotlin Multiplatform projects often won't include
these.

The Problem With Enums
----------------------

Enums are frequently used as a solution to the deficiencies of plain values.
At first glance, they seem like everything you want. They provide a constant
set of type-safe values. And Sealed classes offer a similar set of values
while allowing for multiple instances of each type.

In addition to type safety, both Enums and sealed classes, we also gain
exhaustive when statements, which are enforced for all sealed/enum statements
as of [Kotlin 1.6]:

```kotlin
enum class LightType {
  LightBulb,
  LightStrip,
  FloodLight,
}

val example = when (lightType) {
  LightBulb -> ...
  LightStrip -> ...
  FloodLight -> ...
}
```

However, this comes with a major downside: Breaking Changes.
While having a bound set of values forces consumers of the API to update
their code to match the new set of values, this is not always desirable.
Consider the above set of types in an SDK. Would we really want to make
a new Major/breaking release just to add a new product type? Probably not.
Even internally, if your codebase is large enough, migrating all of your
existing code may not be desirable; it may add unnecessary instability and
a large burden to your project.

Sometimes we *want* the values to be unbounded, or unstable. Enums and
Sealed Classes cannot provide this functionality.

[Kotlin 1.6]: https://kotlinlang.org/docs/whatsnew16.html

The Value of Classes
--------------------

When you want something that is unbounded like a plain value, but with the
type-safety of a class, Kotlin has a construct aptly named: [value classes].

Formerly called 'inline classes', value classes wrap a plain value, and
can be optimized by the compiler to have similar performance characteristics
of a plain value. Because of this, these types also do not have the exhaustive
requirements that sealed types and enums have.

We can construct a value structure similar to an enum:

```kotlin
@JvmInline
value class LightType private constructor(
  private val type: String,
) {
  companion object {
    val LightBulb = LightType("light_bulb")
    val LightStrip = LightType("light_strip")
    val FloodLight = LightType("flood_light")
  }
}
```

Now, when we handle that data, the type defers its identity to the underlying
type, in this case a `String`. This means that Kotlin's compiler will require
an `else` statement in exhaustive situations:

```kotlin
val example = when (myLight) {
  LightBulb -> TODO()
  LightStrip -> TODO()
  FloodLight -> TODO()
  else -> TODO() // This else is *required* now
}
```

I want to note that this isn't *inherently desirable* either. Sometimes
you *want* to provide a sealed type, allowing the consumer to rely on a
fixed set of outputs. The distinction you should think about is whether
your data type is stable or unbounded.

[value classes]: https://kotlinlang.org/docs/inline-classes.html

Simple Mistakes
---------------

### Closed vs Open

When I originally started using value classes like this, my instinct was
to provide somewhat similar utility functions as the ones found in an
enum class. for example, a `values` method with the known values as a collection.

For example:

```kotlin
@JvmInline
value class LightType private constructor(
  private val type: String,
) {
  companion object {
    val LightBulb = LightType("light_bulb")
    val LightStrip = LightType("light_strip")
    val FloodLight = LightType("flood_light")

    fun values() = arrayOf(LightBulb, LightStrip, FloodLight)
  }
}
```

Something that's important to note about this, is that it only works well
if your code *owns the data* that the value class provides. In this sense,
the data is **closed**.

It would be a mistake to add this method if the value is originating from
an API, for example. The distinction here is that not only is the data
unbounded, but it is also **open**, meaning new values may appear in the
future from the API, and we should wrap that value in this value class.

In this sense, the provided `val` definitions within our companion merely
provide a convenient list of the current known values, but the values are
open for new extensions. For this type of data, I also now recommend opening
the constructor and key parameter:

```kotlin
@JvmInline
value class LightType(
    val type: String,
) {
  companion object {
    val LightBulb = LightType("light_bulb")
    val LightStrip = LightType("light_strip")
    val FloodLight = LightType("flood_light")
  }
}
```

This allows the same type-safety, while not causing problems when new values
are added to the API.
