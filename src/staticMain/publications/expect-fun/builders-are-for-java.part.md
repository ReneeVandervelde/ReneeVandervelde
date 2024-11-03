Builders are for Java
=====================

Are Builder classes necessary when writing in Kotlin? In a language like
Java, builder classes are ubiquitous. Kotlin brings with it new data structures
and methods for handling data and state. Do [Data Classes] replace the need
for Builders when using Kotlin?

[Data Classes]: https://kotlinlang.org/docs/reference/data-classes.html

----------

Why use a Builder at all?
-------------------------

Consider a simple Data Class in Kotlin:

```kotlin
data class ConnectionConfig(
    val url: String,
    val useHttps: Boolean = true,
    val cacheResults: Boolean = false,
)
```

Creating new instances of this configuration works great in Kotlin thanks
to named and default arguments:

```kotlin
val myConfig = ConnectionConfig(
    url = "example.com",
    cacheResults = true,
)
```

This structure has several advantages:

 - The end result is effectively immutable, containing only read-only properties.
 - Defaults can be provided, where applicable, to the data.
 - Any number of values can be specified at creation time.
 - It is clear which parameter is being set, thanks to named arguments.

It seems there is no strong need for a builder here. Why use a builder at
all? Adding a builder would only add complexity while making the API less
concise. That is, assuming the caller is using Kotlin.

But what if the class is being used in a Java file? For example, in a
mixed-language project, or more likely, in an SDK. What would this class
be like to use when the consumer is using Java?

When that same call is with Java, the structure is quite problematic:

```java
ConnectionConfig myConfig = new ConnectionConfig(
    "example.com",
    true, // Must also be specified to specify `cacheResults`
    true,
);
```

Due to the way telescoping methods work in java, `cacheResults` can't be
specified on its own anymore. Even when the method is annotated with
`@JvmOverloads`, the previous arguments always need to be specified.

To make matters worse, the primitive arguments can easily be confused since
they are not named. When reading the code, it's no longer clear which boolean
belongs to which parameter.

The result is an API that's tedious and error-prone. This was the problem
that Builder classes were meant to solve. If the class is expected to be
used by a Java caller, builders can still provide value for Kotlin.

Solving With a Builder
----------------------

Builders are well suited to solve these problems for Java. A minimal
implementation of a builder for this class looks like this:

```kotlin
data class ConnectionConfig(
    val url: String,
    val useHttps: Boolean,
    val cacheResults: Boolean,
) {
    class Builder(
        var url: String,
    ) {
        var useHttps: Boolean = true
        var cacheResults: Boolean = false

        fun build() = ConnectionConfig(
            url = url,
            useHttps = useHttps,
            cacheResults = cacheResults,
        )
    }
}
```

Now even the Java interface has a similar analogue to named and optional
parameters:

```java
ConnectionConfig.Builder builder = new ConnectionConfig.Builder("example.com")
builder.setCacheResults(false)
ConnectionConfig myConfig = builder.build();
```

### Adding Chaining

The first way the builder can be improved is via the setter methods.
Because Java lacks an `apply {}` block like Kotlin has, setting multiple
properties can be made easier by introducing chaining into our methods.
This can be done by hiding the default property setter and creating a function
that looks more like a traditional setter method while returning `this` as
the return type by using an `apply` in our Kotlin code. This also provides
an opportunity to introduce more readable names to the setter methods.

```kotlin
class Builder(
    url: String,
) {
    var url: String = url
        private set // Hide the default setters from public
    var useHttps: Boolean = true
        private set
    var cacheResults: Boolean = false
        private set

    fun setUrl(value: String): Builder = apply {
        url = value
    }

    fun useHttps(value: Boolean): Builder = apply {
        useHttps = value
    }

    fun cacheResults(value: Boolean): Builder = apply {
        cacheResults = value
    }

    fun build() = ConnectionConfig(
        url = url,
        useHttps = useHttps,
        cacheResults = cacheResults,
    )
}
```

Now the Java calls are much cleaner, thanks to the chaining:

```java
ConnectionConfig myConfig = new ConnectionConfig.Builder("example.com")
    .setCacheResults(false)
    .build();
```

### Copy Methods

 Kotlin's data classes also benefit from easy copy modification via generated
 `copy` methods. Interestingly, Java does have access to these methods, but
 it suffers from the same lack of named arguments and optional parameters
 that the constructors have.

Luckily, this can be solved by adding a `toBuilder` method to our class
that transforms the class *back into* a builder class.

```kotlin
data class ConnectionConfig(
    val url: String,
    val useHttps: Boolean,
    val cacheResults: Boolean,
) {
    class Builder(
        var url: String,
    ) { /* ... */ }

    fun toBuilder() = Builder(url)
        .useHttps(useHttps)
        .build()
}
```

Now callers can convert to and from the builder to create new modified
instances of the class.

```java
ConnectionConfig myConfig = new ConnectionConfig.Builder("example.com")
    .setCacheResults(false)
    .build();

ConnectionConfig newConfig = myConfig.newBuilder()
    .setUrl("modified.example.com")
    .build();
```

Pitfalls
--------

### Don't put properties in the builder's constructor

It's tempting to try to use Kotlin's language features to avoid some of
the boilerplate in this builder, such as moving the properties of the builder
class into its default constructor and relying on Kotlin's generated
`set` methods:

```kotlin
class Builder(
    var url: String,
    var useHttps: Boolean = true,
    var cacheResults: Boolean = false,
) { /* ... */ }
```

This is **not recommended** because the java callers would no longer have
access to chaining when calling `set` methods, and would need to provide
multiple arguments at a time, even when not desired.

### Put required arguments in the builders constructors

One common anti-pattern with builders is to instead check requirements at
runtime during the `build()`` call.

```kotlin
class Builder {
    var url: String? = null
        private set

    fun setUrl(value: String) = apply {
        url = value
    }

    fun build(): ConnectionConfig {
        requireNotNull(url) { "URL is required" } // Don't do this

        return ConnectionConfig(url)
    }
}
```

This is **not recommended** because it introduces a *runtime error* for
something that could be caught at compile-time.

While the properties themselves should not be put in the builder's default
constructor. Required *arguments* should be in the default constructor.
In other words, while the constructor for the builder should not contain
`var`/`val` lines, anything required should be specified as a plain argument:

```kotlin
class Builder(
    url: String, // Required argument, not a property.
) {
    var url: String = url // The argument is passed to the property here.
    /* ... */
}
```

### Allow continued modification

 In an application, builders are often passed around as the data is being
 built, being copied and mutated until it is ready to be passed into an API.
 The ability to have a data structure temporarily modifiable, and then
 finalized with a `build` call is one of the arguments to use a builder.

The `set` methods should always be accessible on the builder class, even
if the property was required in the construction of the builder:

```kotlin
class Builder(
    url: String,
) {
    var url: String = url
        private set

    // This method should be defined, even though url was required
    fun setUrl(value: String): Builder = apply {
        url = value
    }

    /* ... */
}
```

This also applies to *optional* properties. A common mistake is to make
the setter for an optional property non-null.

```kotlin
class Builder {
    var exampleOption: String? = null
        private set

    fun setExampleOption(value: String) { // Don't do this
        exampleOption = value
    }
    /* ... */
}
```



The problem with this is that the option can not be set back to `null` after
it is specified. If `null` is a valid option, it should be able to be specified
explicitly. This allows the user to set the parameter dynamically from
another property without requiring an `if` statement to conditionally call
our setter, and break the chaining.

Final Result
------------

All of these strategies brought together results in a bit of boilerplate,
but works well on all platforms:

```kotlin
data class ConnectionConfig(
    val url: String,
    val useHttps: Boolean,
    val cacheResults: Boolean,
) {
    fun toBuilder() = Builder(url)
        .useHttps(useHttps)
        .build()

    class Builder(
        url: String,
    ) {
        var url: String = url
            private set // Hide the default setters from public
        var useHttps: Boolean = true
            private set
        var cacheResults: Boolean = false
            private set

        fun setUrl(value: String): Builder = apply {
            url = value
        }

        fun useHttps(value: Boolean): Builder = apply {
            useHttps = value
        }

        fun cacheResults(value: Boolean): Builder = apply {
            cacheResults = value
        }

        fun build() = ConnectionConfig(
            url = url,
            useHttps = useHttps,
            cacheResults = cacheResults,
        )
    }
}
```
