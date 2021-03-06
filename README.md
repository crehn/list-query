# ListQuery
[![Build Status](https://travis-ci.org/crehn/list-query.svg?branch=master)](https://travis-ci.org/crehn/list-query)
[![Dependency Status](https://www.versioneye.com/user/projects/57a59d126725bd470b300341//badge.svg?style=flat-square)](https://www.versioneye.com/user/projects/57a59d126725bd470b300341/)
[ ![Download](https://api.bintray.com/packages/crehn/maven/list-query/images/download.svg) ](https://bintray.com/crehn/maven/list-query/_latestVersion)

Java 8 finally instroduced lambdas and streams. Streams offer great flexibility but can be a bit verbose. This library simplifies using them in those 80% of the cases where the full flexibility is not needed and you rather want to optimize readability.

## What ListQuery is

Typically you use streams like this:

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
    .sorted(Comparator.comparing(Name::getLastName))
    .collect(Collectors.toList());
```

You create a stream from a list, use map and/or filter and collect the result into a list again. The invocations of `stream()` and `collect()` are mere ceremony and carry no specific meaning in most of the cases. When reading such code they are rather haystack than needle.

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
    * You cannot query anything besides lists (or rather collections). No databases, no XML files, etc. Just lists.
    * Have a look at this [Stackoverflow question](http://stackoverflow.com/questions/1217228/what-is-the-java-equivalent-for-linq)
* ListQuery is not a replacement for streams
    * Streams are more powerful and sometimes you will need the expressiveness of streams. Go and use them. They are great. ListQuery is just for optimizing readability in the simple cases.
* ListQuery is not performance-optimized
    * Although there shouldn't be a noticeable overhead compared to plain streams, ListQuery is not meant to be used in extreamly time-crtitical contexts.

## Usage

See [ListQueryTest](/src/test/java/com/github/crehn/listquery/ListQueryTest.java)

## Grammar

The following grammar specifies valid queries:

```
ListQuery ::= FROM + [WHERE] + [".ordered()"] + [SPECIAL] + SELECT                          
            | FROM + [WHERE] +    ORDER_BY    + [SPECIAL] + MAP_SELECT ;                    
FROM ::= "from(collection)" ;                                                               
WHERE ::= ".where(predicate)" + [AND_OR] ;                                                  
AND_OR ::= { ".and(predicate)" | ".or(predicate)" } ;                                       
ORDER_BY ::= ".orderBy(comparator)" | ".orderBy(getter)" ;                                  
SPECIAL ::= { [".limit(limit)"] + [".distinct()"] } ;                                       
SELECT ::= MAP_SELECT | IDENTITY_SELECT ;                                                   
IDENTITY_SELECT ::= ".select()" | ".select(paging)" | ".selectFirst()" ;                    
MAP_SELECT ::= ".select(mapper)" | ".select(mapper, paging)" | ".selectFirst(mapper)" ;     
```

## Just

There are even simpler cases. Sometimes you just want to map or filter something. A typical example would be to convert a list of database entities to DTOs. 

Using Streams you would have to call `stream()` and `collect(Collectors.toList())`. Using `ListQuery` you'd use something like
```Java
from(entities).select(Entity::toApi);
```

An alternative would be to use `Just.map`:
```Java
map(entities, Entity::toApi)
```

See [JustTest](/src/test/java/com/github/crehn/listquery/JustTest.java)
