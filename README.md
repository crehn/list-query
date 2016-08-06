# ListQuery

[![Build Status](https://travis-ci.org/crehn/list-query.svg?branch=master)](https://travis-ci.org/crehn/list-query)
[![Dependency Status](https://www.versioneye.com/user/projects/57a59d126725bd470b300341//badge.svg?style=flat-square)](https://www.versioneye.com/user/projects/57a59d126725bd470b300341/)

Java 8 finally instroduced lambdas and streams. Streams offer great flexibility but can be a bit verbose. This library simplifies using them in those 80% of the cases where the full flexibility is not needed and you rather want to optimize readability.

## What ListQuery is

In most of the cases you use streams like this:

```Java
namesList.stream()
    .map(name -> new Customer(name))
    .collect(Collectors.toList());
```
or
```Java
customerList.stream()
    .filter(c -> c.getName().startsWith("A"))
    .collect(Collectors.toList());
```

You create a stream from a list, use map and/or filter and collect the result into a list again. The invocations of``stream()`` and ``collect()`` are mere ceremony and carry no specific meaning in most of the cases. When reading such code they are rather haystack than needle.

ListQuery lets you do the following instead:
```Java
from(namesList).select(name -> new Customer(name));
```
```Java
from(customerList).where(c -> c.getName().startsWith("A")).select();
```

## What ListQuery is not

* ListQuery may be inspired by LINQ but it is not "LINQ for Java"
    * You cannot query anything besides lists. No databases, no XML files, etc. Just lists.
    * Have a look at this [Stackoverflow question](http://stackoverflow.com/questions/1217228/what-is-the-java-equivalent-for-linq)
* ListQuery is not a replacement for streams
    * Streams are more powerful and sometimes you will need the expressiveness of streams. Go and use them. They are great. ListQuery is just for optimizing readability in the simple cases.
* ListQuery is not performance-optimized
    * Although there shouldn't be a noticeable overhead compared to plain streams, ListQuery is not meant to be used in extreamly time-crtitical contexts.

## Usage

See [ListQueryTest](/src/test/java/com/github/crehn/listquery/ListQueryTest.java)
