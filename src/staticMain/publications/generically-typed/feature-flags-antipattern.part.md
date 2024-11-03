Feature Flags as an Anti-Pattern
================================

Over the years, I've begun to recognize scenarios in which feature flags
have become a bad habit in projects. While there are some good use cases
for feature flags, I have found them to be less useful in practice than
the industry would seem to suggest. At their worst, they can be outright
harmful to a project.

----------

Feature Flags Maximize Work-In-Progress
---------------------------------------

The most problematic habit that I've seen develop in projects using feature
flags is using them as a way to covertly roll over work between development
cycles.

Development cycles, such as Sprints, are a way to minimize the amount of
in-progress work that a team has. Their purpose is to ensure that the
application frequently hits milestones in which the project is *shipable*.
Until a feature is *actually* shipped to the customer, no value has been
delivered, and the work is still accumulating unplanned work and
technical debt.

Feature Flags conveniently enable developers to side-step these processes
by 'shipping' code that is never actually used by customers. That is until
the day the feature flag is enabled and months of work suddenly hit production
at once. Problems quickly arise and bugs flood and slam engineering teams
with unplanned work, grinding development velocity to a halt.

One of my biggest pet peeves in development processes is how often teams
are willing to *lie to themselves* about what they're actually doing.
People love to *say* that they're an agile team, or that they work in a
Scrum process. But, more often than not, what they mean is they scheduled
a stand-up meeting. Because, after all, that was the easiest part of the
process. Agile development is **hard**, and it requires discipline. Feature
flags allow teams to break that discipline. Don't lie to yourself, this
is a **waterfall** process. Meeting every two weeks to discuss how the project
is going doesn't make the team agile.

Feature Flags Won't Save You
----------------------------

One of the arguments I hear in favor of feature flags is the ability to
"roll back" a feature if it isn't working properly. While this can sometimes
be useful, it's not the savior you may think it is.

For many complex features, the ability to roll back a feature needs to be
carefully considered in its design. If a feature modifies data in such a
way that is not backward compatible, disabling the feature flag may actually
*cause errors*.

Additionally, the user experience of having a feature rolled back is quite
poor. In severe cases, it can even be damaging to the integrity of the
application. This can be particularly sensitive for security features.
Imagine an application that rolls out a new feature enabling fingerprint
authentication. Can this feature be rolled back? If a roll back is done
in this scenario, the user will unexpectedly lose a feature they were counting
on for security. This can be very risky.

What the team *actually needs* is the ability to mitigate problems quickly
in an application. In some scenarios, feature flags help with that. However,
this also allows teams to ignore potential improvements to the hotfix and
deployment process.

What are Flags Good For?
------------------------

My favorite argument **for** feature flags is phased rollouts. For these
scenarios, there's really no match. This is particularly useful if the feature
is gated to specific subsets of users. But I want you to ask yourself:
Do you *really need* a phased rollout? If the project is a high-traffic
application and are rolling out a feature in the critical path of
functionality, by all means. But if the product is a consumer application
that only sees casual use, or if the feature is already tucked away in a
non-critical part of the application, you likely don't need a phased rollout,
as the users will self-select into a small but growing group of users.

Another argument for feature flags that I find dubious is experiments.
Feature flags are definitely useful when rolling out variant user experiences
to determine if a change will impact usage as expected. However, I've found
that these cases are quite sparse in practice, and the impact on user
experience that is inconsistent can be costly. It also makes documentation
and support difficult when the experience is materially changed. Use
experiments sparingly.
