Becoming a 10x software engineer by ignoring LLMs
=================================================

At first, I thought that everyone else must have a different version of these
LLM tools than I do. I’ve used Github Copilot, ChatGPT, Claude, Cursor and
Goose. The hype around these products is so high, that I often fear expressing
any dissent about them. People will literally get mad at me when I tell
them that ChatGPT couldn’t answer my question. These products are talked
about as if they are the future of every industry, and they’re getting
better at a rate we’ve never seen before. Is any of that really true,
or are we all just caught up in a bubble of hyping up the
“next big thing” in tech?

LLMs save me a lot of time
--------------------------

I don’t want to start off with the impression that I think LLM tools are
useless; they’re not. There are plenty of “LLM-shaped problems” where
these products can save tons of time. It’s really impressive to see
a tool that can synthesize an entire world of information and be able to
provide coherent output to effectively any question.

The problem I have is that blinding optimism about the technology leads
people to assume these tools can be used to solve any problem; they can’t.
Forcing LLMs into every problem space is not productive, and a behavior
that I believe the industry will inevitably grow out of.

LLMs waste more of my time than any tool I’ve ever used in my entire career and it’s not even close
---------------------------------------------------------------------------------------------------

When I think of tools that waste my time in engineering, I used to think of
bloated ticketing software or unnecessary processes like timesheets.
But these types of inefficiencies aren’t even close to the waste that
I see and have experienced with LLM tools.

The pressure to use an LLM to solve problems has become so intense that
hours will be spent just trying to steer the LLM into the solution you know
is correct. I write, re-write and re-try prompts, adding additional qualifiers
and instructions each time trying to find the right input to summon the
output I’m expecting. I often find myself arguing with an LLM when I know
that it’s wrong, asking it to check its work or point out why its
solution won’t work.

All of this to solve what would have otherwise been a 15-minute task.
Frequently, after all of that effort, the result is giving up on the LLM
and doing the task manually anyway. Before using an LLM to solve a problem,
I now find myself getting exhausted, wondering if this is going to be solved
in one prompt or thirty.

## LLMs can totally replace a Jr Engineer

Something that time in the industry has taught me is that coding is
the **easiest** part of software engineering. I’ve never really been
interested in toy problems — the kind that you see in interviews.
It can sometimes be an interesting exercise to work on efficient raytracing
or sorting algorithms. More practically, It’s an interesting thought experiment
to talk about architecture patterns. But I wouldn’t consider any of these
problems to be material to my actual job as a software engineer.

That’s because software engineering *isn’t the same* as coding.
That’s what people imagine it to be, either from the outside or in their
early years as an engineer. The hard part of engineering is figuring
out a solution to a problem and working with the different stakeholders
of a product to get to a solution that meets everyone’s ideal goals
in a practical way.

Sure, you want the code to be performant, but you also want it to be
maintainable and well-structured; what balance do you strive for?
Your design team wants a new widget, but it doesn’t quite work with
the other ones in the product; can you work out the details to make it work?
Product wants a feature launched by the holidays, but doesn’t want to
sacrifice any functionality to get it done; can you find some shortcuts
that won’t reduce the product quality? These kinds of problems are a
material amount of software engineering. LLMs can’t help with these problems,
because the solutions to these problems will define what your
product is, and what makes it different.

This is what confuses people when they see LLM tools like cursor
build out an entire project with a couple of prompts.
“Look, it made an entire Tetris game in python with a single prompt!”.
That’s great for it. But you’re not hiring engineers to just to spit-out
code for solved problems. You’re trying to create a product that solves
new problems, or at the very least solves problems in some novel way.

Jr engineers focus primarily on coding as they build up the experience
to be able to solve these more complex problems. It’s possible that someday
the demand for this type of skill will be reduced by LLM tools.
The primary thing this will change about software engineering is
making it more difficult and less approachable to gain that necessary
experience.

## How I use LLMs when coding

I have a philosophy for when to use an LLM that I have been happy with
for some time now: \
*Only ask an LLM questions that you *know* the answer to.*

Some problems you know the answer to, but want to validate your thinking
or want alternative solutions to. When coding, you might ask an LLM what
the simplest way to solve some problem is. If the LLM spits out something
that looks like what you were imagining, you know you’re right. If it
spits out something different, you’ve got some more thinking to do.
It’s possible, and depending on the problem even probable, the LLM
is wrong. But you can consider the LLM as a counterpoint. Maybe you agree,
maybe you don’t; that’s up to you.

Other questions you’re able to solve yourself, but would be too tedious
to do step by step. This is the thing I find LLMs most useful at today.
Particularly for coding, I find LLMs to be great at generating tests.
Unit tests in particular are often tedious to write. The inputs and outputs
can be easily defined, but there’s often a lot of boilerplate that you don’t
want to abstract away. I find Github Copilot the best at this.
You can often write a comment that describes the test you want, and it gets
pretty close to the correct test on the first try. Then, once I fix up
the generated code for a single test to my liking, generating variants of
that test appear trivial for the LLM. This is a huge productivity win.

Some questions you don’t immediately know the answer to, but can easily
validate an answer that is presented. You might as an LLM a question like
“How do I get the short git hash of a branch”. The answer to that is easy
to check. There’s some risk of the answer not being the *best* way to do
what you want. Because the LLM doesn’t have the context for what you’re doing,
you risk an [XY problem] with simpler questions of this type.
Often, though, this isn’t a problem.

So, no - I don’t entirely ignore LLMs, but I don’t use an LLM to *replace*
the work that I do; it’s more of a reference.

[XY problem]: http://xyproblem.info

## The future of LLMs

I suspect people are vastly overestimating the future advancement of AI.
Tech industries are used to assuming that development follows exponential
curves like Moore’s law. But practical advancements in AI don’t appear
to follow this curve. The curve these advancements appear to follow is
closer to a logistic curve, or an S curve:

![Logistic Curve](/resources/images/publications/ignoring-llms/curve.svg)

We’ve seen this play out practically already in the industry. Look at
self-driving cars. This technology got really good, really fast.
But how many times have we been promised the technology would,
“at this rate”, be ubiquitous in a matter of months? This is because
machine learning quickly moves through the exponential part of this curve
and onto the slower incremental improvements. That’s the problem with assuming
this technology will continue to progress “at this rate”; the
rate **will** change.

To be honest, I find speculating on the future of AI to be extremely boring.
This isn’t a data-driven well-sourced article.
The graph above isn’t even labeled.
I don’t think the data actually matters here. We don’t know for sure what’s
in the future for this technology. It’s possible this could disrupt more
industries than anyone expects. It’s also possible the technology proves
too expensive to ever find a real market fit and the bubble bursts leading
to mass layoffs at companies that invested in AI.
If I’m betting, it’s somewhere closer to the latter.

This isn’t to say that AI won’t have huge advancements ahead of us.
I’m not willing to guess exactly where we are on that curve, but even if
we’re near the end of steep progress, there’s still plenty more to do.
I’m optimistic about the future of LLM tools. Just not in the way that
the rest of the industry is currently.

I want coding to include AI for reviewing code to reduce the opportunity
for mistakes. AI reviews can also be used to catch security flaws change
requests that might otherwise go unnoticed.
This could be a huge win for open source communities, and help reduce the
risk of supply-chain attacks for everyone.

AI can even be used to automate changes. I can imagine future tools being
used to scan code repositories for potential improvements, or even suggest
fixes to bug reports automatically. These tasks will require more context
than we seem to be able to apply with current tools,
but I think we will get there.

I don’t believe that AI will ever be able to replace the *engineering*
that goes into a product. In the same way that AI can’t replace art,
engineering requires creative problem-solving. You can spend hours convincing
yourself that the AI made these decisions, but it didn’t.
Knowing when to use an LLM and when to solve the problem yourself is the
skill that will really make you more productive.

I'll leave you with this [clip] from earlier this year that I still think is
relevant, from Linus Torvalds.

[clip]: https://x.com/tsarnick/status/1848182122609283439
