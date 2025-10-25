Update of Ionspin’s KMP-Libsodium Audit
=======================================

There's been a relevant update to [Ugljesa Jovanovic]'s 
[kotlin-multiplatform-libsodium] library in the last week. This audit report
is an extension of the [previous audit] posted last year.


[kotlin-multiplatform-libsodium]: https://github.com/ionspin/kotlin-multiplatform-libsodium
[Ugljesa Jovanovic]: https://github.com/ionspin
[previous audit]: https://reneevandervelde.com/publications/expect-fun/audit-ionspin-kmp-libsodium.html

----------

Summary of Findings
-------------------

The goal of this audit was to follow-up on the issues highlighted in the 
[previous audit] of this library, as well as analyze new contributions
to the repository.

No new major issues were found, and several were resolved or improved.
New, but minimal, functionality was added to the library, making this 
technically a minor-update, not just a patch release in function.

### Versions Audited

 - [0.9.4] (`00ba667a91c37d91a054bee3be7e95b9637489d6`)
 - [0.9.3] (`2264eed65f28899aac97d9dcbfdbc60abe5b0d6f`)
 - [0.9.2] (`509d22ed0552188ced522c7304186b33fdbb8332`) (prior audit)

[0.9.4]: https://github.com/ionspin/kotlin-multiplatform-libsodium/releases/tag/0.9.4
[0.9.3]: https://github.com/ionspin/kotlin-multiplatform-libsodium/releases/tag/0.9.3
[0.9.2]: https://github.com/ionspin/kotlin-multiplatform-libsodium/releases/tag/0.9.2



Scope and Methodology
---------------------

The commits added between versions `0.9.2` and `0.9.4` of the repository
were analyzed manually.

This audit included manual review of the changed files, checking the 
source code changes for obvious errors, and analyzing changes in 
build dependencies.

During the course of this audit, LLM tools were used to assist in the analysis
of the repository. However, this was not the primary method of analysis.

This is not an audit of Libsodium itself. This library has already been
[audited][1] by engineers much more qualified than me.

For the purposes of this audit, the sample project included with the repository
has not been reviewed.

[1]: https://www.privateinternetaccess.com/blog/libsodium-audit-results/

Resolved Issues
---------------

### Return Codes - Improved

The prior audit of this library showed multiple strategies for asserting
return codes after calls to the native library. As of `0.9.3` this strategy
seems to have been greatly improved by relying primarily on the 
`isLibsodiumSuccessCode` helper method for assertions. No vulnerabilities
were noticed in this migration, and this improvement should help prevent
errors in future versions.

### Unmerged Libsodium Commits - Resolved

The prior audit of this library showed an additional commit not included
in the Libsodium repo ([49c37e8]). This commit is no longer present, and
the Libsodium version is built of a direct commit ([3eabeb5]) from the 
upstream repository.

[49c37e8]: https://github.com/ionspin/libsodium/commit/49c37e8f604b493af51d05721e4ac69f180cf925
[3eabeb5]: https://github.com/ionspin/libsodium/commit/3eabeb547fd7f91f4f8a15885050eb2a8e0272d5

Ongoing Issues
--------------

### Unreleased Libsodium Commits

The underlying libsodium commit used in this version of the 
library ([3eabeb5]) has not been included in an official release of Libsodium.
While this is an official commit, this still lacks the wider release testing
provided by an official release commit. This inclusion is necessary in order
to fix build issues and an [Android][2] compatibility issue. Libsodium has
not had an official release with these fixes included as of the publishing
of this analysis, so this is the only option to fix these issues.

[2]: https://github.com/ionspin/kotlin-multiplatform-libsodium/issues/59

Findings - Code Analysis
------------------------

The primary functionality change in this release is the addition of Ristretto
support. The implementation of this functionality includes some manipulation
of data to support byte-order, but no additional functionality that appeared
particularly brittle or warranting additional security analysis.

Conclusion
----------

This appears to be a straightforward improvement to the library as claimed.
The update reinforces that this library is being actively maintained, and
resolves issues that have come up in recent months.
