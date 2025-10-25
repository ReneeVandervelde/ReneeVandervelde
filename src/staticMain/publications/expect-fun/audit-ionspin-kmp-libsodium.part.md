Independent Audit of Ionspin’s KMP-Libsodium
=============================================

The [kotlin-multiplatform-libsodium] library from developer
[Ugljesa Jovanovic], aka ionspin, is a Kotlin Multiplatform library that
gives access to the many cryptographic APIs of [Libsodium].
It currently supports most, but not all, of the platform targets offered by Kotlin.

This library claims to be “just a wrapper around the well known Libsodium library”.
We’re going to test that!

[kotlin-multiplatform-libsodium]: https://github.com/ionspin/kotlin-multiplatform-libsodium
[Ugljesa Jovanovic]: https://github.com/ionspin
[Libsodium]: https://libsodium.org/

----------

Summary of Findings
-------------------

The goal of this audit was to verify the functionality in the
kotlin-multiplatform-libsodium library.
During the course of this audit, no major concerns were found. While there
were some areas for improvement and monitoring in future releases, this
library appears to do as claimed and provide access to Libsodium on Kotlin
Multiplatform.

### Updates

 - Newer Versions `0.9.3`-`0.9.4` have been audited in a [follow-up report]

### Versions Audited

 - [0.9.2] (`509d22ed0552188ced522c7304186b33fdbb8332`)

[0.9.2]: https://github.com/ionspin/kotlin-multiplatform-libsodium/releases/tag/0.9.2
[follow-up report]: https://reneevandervelde.com/publications/expect-fun/audit-ionspin-kmp-libsodium-094-update.html

About the Auditor
-----------------

As a disclaimer up-front: This audit was done by myself in my personal time
and was not done in a professional context. While I do work with some
cryptography professionally, this audit was not done within the scope or
course of my employment, and does not reflect the views of my employer
in any way.

For my personal experience: I have been developing software for over 15 years.
I have experience with many of the cryptographic APIs that Libsodium uses
from a practical context. However, I am not a security analyst by trade,
nor have I made material contributions to Libsodium or related cryptography
libraries at the time of writing. I do have extensive experience with Kotlin
Multiplatform code, making this wrapping library a good candidate for me
to audit, since it is expected to be primarily a consumer of the existing
Libsodium APIs adapted to Kotlin Multiplatform.

Scope and Methodology
---------------------

For this audit I have looked at the [0.9.2] tag of the
kotlin-multiplatform-libsodium library. I will update this article if and
when future audits are conducted.

This audit focused primarily on manual review of files, checking the source
code for obvious errors, validating the claim that this is merely a wrapper
of the Libsodium library, analyzing dependencies, and verifying the build
pipeline for any suspect operations.

This audit does not put the library through any automated security analysis
tools.

This is not an audit of Libsodium itself. This library has already been
[audited][1] by engineers much more qualified than me.

For the purposes of this audit, the sample project included with the repository
has not been reviewed.

[1]: https://www.privateinternetaccess.com/blog/libsodium-audit-results/

Findings - Code Analysis
------------------------

A wrapper library should be very straightforward to review. Ideally, the
library would do little more than proxy calls to the underlying library.
This can be more complex in practicality. The primary concerns this code
analysis is focused on are:
 - Is the logic implemented in this library warranted?
 - Does the library implement parts of Libsodium on its own rather than
   deferring to the underlying library?

The Libsodium methods do appear to be proxied directly to the underlying
library as advertised. Little is done beyond transforming the data from a
type convenient for use in Kotlin into the arguments needed to be provided
to Libsodium.

There are, however, extra utilities provided in this library that are not
provided by Libsodium. Examples of this can include simple utility methods,
such as the byte array extensions found in [StringUtil.kt] as well as
supplemental properties such as transforming results into readable strings
found in [ArgonResult]. While this is additional functionality on top of the
wrapping library, these methods seem largely limited to transforming data for
common use cases in Kotlin. These utilities aren’t typically used in the library
itself, so they can be avoided if desired without affecting the functionality.
I do not see any reason these should raise concern.

One utility method of dubious quality appears in the common code as an
extension on `UByteArray` called
`fromLittleEndianUByteArrayToBigEndianUByteArray`. This method simply reverses
the array. This is not sufficient to convert the array as claimed. This
method should be avoided. This method does not appear to be used in the library
itself and is only provided for convenience.

### Area for Concern - Return Codes

My primary area of concern for this library is the way return codes from
Libsodium are currently handled. While I did not immediately find any errors,
the strategy here seems highly prone to error, and appears very brittle.
This area deserves the maintainer's focus.

The return codes for libsodium are fairly straightforward and [documented][2].
However, in ionspin’s wrapper library, I find multiple different strategies
for checking the return codes[^3][3][^4][4][^5][5].
Multiple of these methods are checking for a specific error code, rather
than checking for a success code. Meaning, these methods may incorrectly
pass should an unexpected code be returned. This, combined with the variance
in strategies, makes this code very brittle and susceptible to breakage.

[2]: https://doc.libsodium.org/quickstart#how-do-i-check-if-a-function-call-succeeded
[3]: https://github.com/ionspin/kotlin-multiplatform-libsodium/blob/0.9.2/multiplatform-crypto-libsodium-bindings/src/commonMain/kotlin/com.ionspin.kotlin.crypto/GeneralLibsodiumException.kt
[4]: https://github.com/ionspin/kotlin-multiplatform-libsodium/blob/0.9.2/multiplatform-crypto-libsodium-bindings/src/jvmMain/kotlin/com/ionspin/kotlin/crypto/secretbox/SecretBox.kt#L32-L34
[5]: https://github.com/ionspin/kotlin-multiplatform-libsodium/blob/0.9.2/multiplatform-crypto-libsodium-bindings/src/jvmMain/kotlin/com/ionspin/kotlin/crypto/signature/SignatureJvm.kt#L42-L44
[6]: https://github.com/ionspin/kotlin-multiplatform-libsodium/pull/46
[StringUtil.kt]: https://github.com/ionspin/kotlin-multiplatform-libsodium/blob/0.9.2/multiplatform-crypto-api/src/commonMain/kotlin/com/ionspin/kotlin/crypto/util/StringUtil.kt
[ArgonResult]: https://github.com/ionspin/kotlin-multiplatform-libsodium/blob/0.9.2/multiplatform-crypto-api/src/commonMain/kotlin/com/ionspin/kotlin/crypto/keyderivation/KeyDerivationFunction.kt#L32

Findings - Dependency Analysis
------------------------------

The wrapper library has very few external dependencies. This is great news
for those concerned with supply chain attacks. The library primarily only
depends on language-level jetbrains/kotlin dependencies directly, with a
few exceptions.

The primary dependency of concern is a [resource-loader] library. This library
is used by the wrapper to load the Libsodium `.so` files in Java. While
the use case of this seems reasonable, this library has very few maintainers,
and does not seem particularly widely used.

The second dependency for concern is the most obvious one, Libsodium itself.
The library embeds the Libsodium library directly. However, in many cases
the library is being built from a forked repository of ionspin’s as a submodule
of the repository. This fork appears to be based on the 1.0.19 version of
Libsodium. At the time of writing, this is only one release out of date.
However, the snapshot has some *extra* commits included that are not present
in Libsodium’s 1.0.19 release.

Four of these extra changes
(commits [97f7722], [86a53a9], [0a266e0], [ac6d390])
can be verified as commits in the Libsodium repository, but were made after
the 1.0.19 release, and are likely not verified independently of the subsequent
release of 1.0.20. This is likely to include the last commit ([ac6d390]),
which may fix some platform targets for apple devices. However, this should
be updated to a tagged version of the library as soon as possible.

The last commit included in the Libsodium fork is by ionspin ([49c37e8]),
and appears to disable the Apple Vision platform as a build target. It’s
not clear that this change was entirely necessary. It seems that there would
be a benefit to remove this change so that the library can rely on an unmodified
commit of Libsodium instead. However, the change does not appear to modify
any source files of Libsodium itself.

[resource-loader]: https://github.com/terl/resource-loader
[97f7722]: https://github.com/ionspin/libsodium/commit/97f7722f2c3570842709ffef5aab5e632ff4002c
[86a53a9]: https://github.com/ionspin/libsodium/commit/86a53a901aa434a139fc65a793e4d18c48d979c3
[0a266e0]: https://github.com/ionspin/libsodium/commit/0a266e0a7d28532b6f8d97fb752bbc69ce2e86ed
[ac6d390]: https://github.com/ionspin/libsodium/commit/ac6d3909eb0cf3d211b8137c6408ca64dd7b97b3
[49c37e8]: https://github.com/ionspin/libsodium/commit/49c37e8f604b493af51d05721e4ac69f180cf925

Conclusion
----------

Overall this library appears to do as claimed, and is a great addition to
the Kotlin Multiplatform community. The project is not yet fully stable,
but appears to be actively maintained. During this audit I even managed
to create a [pull-request][6] for a very minor bug, for which the maintainer
was responsive and promptly merged. I hope to contribute more to the project
as I use it more. I plan to keep an eye on future changes to this project,
and will update findings here.
