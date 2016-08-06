# ListQuery

[![Build Status](https://travis-ci.org/crehn/list-query.svg?branch=master)](https://travis-ci.org/crehn/list-query)
[![Dependency Status](https://www.versioneye.com/user/projects/57a59d126725bd470b300341//badge.svg?style=flat-square)](https://www.versioneye.com/user/projects/57a59d126725bd470b300341/)

Java 8 finally instroduced lambdas and streams. Streams offer great flexibility but can be a bit verbose. This library simplifies using them in those 80% of the cases where the full flexibility is not needed and you rather want to optimize readability.

## What ListQuery is

In most of the cases you use streams like this:

```Java
names.stream()
    .map(name -> new Customer(name.getFirstName(), name.getLastName()))
    .collect(Collectors.toList());
```
or
```Java
names.stream()
    .filter(name -> name.getFirstName().startsWith("A"))
    .collect(Collectors.toList());
```
or
```Java
names.stream()
    .filter(name -> name.getFirstName().startsWith("A"))
    .sorted((n1, n2) -> n1.getFirstName().compareTo(n2.getFirstName()))
    .collect(Collectors.toList());
```

You create a stream from a list, use map and/or filter and collect the result into a list again. The invocations of``stream()`` and ``collect()`` are mere ceremony and carry no specific meaning in most of the cases. When reading such code they are rather haystack than needle.

ListQuery lets you do the following instead:
```Java
from(names).select(name -> new Customer(name.getFirstName(), name.getLastName()));
```
```Java
from(names).where(name -> name.getFirstName().startsWith("A")).select();
```
```Java
from(names)
    .where(name -> name.getFirstName().startsWith("A"))
    .orderBy(Name::getLastName)
    .select(e -> e);
```

## What ListQuery is not

* ListQuery may be inspired by [LINQ](https://en.wikipedia.org/wiki/Language_Integrated_Query) but it is not "LINQ for Java"
    * You cannot query anything besides lists. No databases, no XML files, etc. Just lists.
    * Have a look at this [Stackoverflow question](http://stackoverflow.com/questions/1217228/what-is-the-java-equivalent-for-linq)
* ListQuery is not a replacement for streams
    * Streams are more powerful and sometimes you will need the expressiveness of streams. Go and use them. They are great. ListQuery is just for optimizing readability in the simple cases.
* ListQuery is not performance-optimized
    * Although there shouldn't be a noticeable overhead compared to plain streams, ListQuery is not meant to be used in extreamly time-crtitical contexts.

## Usage

See [ListQueryTest](/src/test/java/com/github/crehn/listquery/ListQueryTest.java)
