# Markdown is Ruining the Web

Engineers love [Markdown]. At first, the reasons for this are obvious.
Markdown provides a syntax that's easier to read than traditional languages
like HTML, while still providing the formatting necessary for most rich-text
communication. But markdown isn't a good tool for everything.
Despite this, markdown has become ubiquitous, and my peers frequently cite
the lack of markdown support as a reason for not wanting to use a tool,
even when better alternatives are available. The result is that the editor
support in many applications has rotted away from the core, either by barely
 supporting formatting or by making a custom variant of markdown that's
 antithetical to the strengths of markdown itself.

[Markdown]: https://daringfireball.net/projects/markdown/

## The Good Parts

Markdown has, largely, one core strength: it is **readable**. It is more
or less the same syntax that people have grown to use to represent formatting
when rich-text is not available. This can be seen in many older emails
and mailing lists where text-only formatting is supported. Markdown took these
existing patterns and formalized them so they could be interpreted and
formatted as rich-text when desired. The best of both worlds, if you will.

This has proven very useful. This format doesn't require supporting a visual
editor, meaning it can be quickly added to any text. It can even be added
retroactively, where users may already be using the syntax in plain-text
format. Engineers also like this format for its durability when used with
source control. The lack of structured formatting allows for fewer conflicts.

Markdown, in general, is **easy**. It's easy to read. It's easy to support.
But this doesn't mean you should use markdown.

## Taking it too far

Markdown starts to fall apart very quickly outside of its niche.
One immediate drawback is that it's not very consistent. There's an original
specification for markdown, but it's not the same as the syntax you'd find
on GitHub, which isn't the same as the syntax used in Slack, which isn't
the same as the syntax used in your notes app. They're all some variant
of markdown, but you don't generally know what to expect when something
supports markdown. This makes the ergonomics of markdown generally miserable
for users, even when they're familiar with markdown.

Markdown's ergonomics are also fairly unpleasant to work with. It's hard
for some engineers to accept this, but most people just don't want to deal
with syntax while they're writing. Most people don't want to
write code — even if that code is simple — and compare what they see in
some preview box. They just want a rich-text editor. Many applications today
are released without a good rich-text editing experience, including
applications that desperately warrant a high-quality editor.

Markdown also holds back the editing experience. The intent of markdown
is to keep the syntax simple. But this is also markdown's biggest constraint.
Moving beyond rudimentary text formatting starts to reveal the cracks in
markdown's design. Without a more structured syntax, creating more advanced
elements becomes completely unintuitive. You can see this start to happen
immediately with an element as simple as an image. Absolutely nobody would
intuit this syntax, and it does not follow pre-existing patterns.
Its distinction from the link syntax exists only for the benefit of the
parser, not the user. There are even simple structures that most markdown
parsers don't support at all. Extremely common formatting like centered text,
subscript and underlining is typically missing.

This ought to rule out markdown as a solution for an entire swath of
use-cases. Instead, many applications will either use markdown as an excuse
to leave out core functionality (*What do you mean* my notes app doesn't
support underlines?!) or they'll waste their time re-inventing
mark-**up** *inside* of markdown and leave it to the user to decipher their
chosen syntax.

Markdown certainly isn't useless. I use it often. But we have to accept
that it's not the solution to everything. Its use-cases are actually
quite niche. Your applications deserve a better editor.

----------

P.S. I wrote this in markdown. Just for you 💕
