# funky-struct

This is an exploration of data structures from Chris Okasaki's
[Purely Functional Data Structures](http://www.amazon.com/Purely-Functional-Structures-Chris-Okasaki/dp/0521663504)
implemented in Clojure.  The goal is to implement a **subset** of the data structures 
that are presented in the text.

## Usage

Each data structure that has been selected conforms (more or less) both to the
signature provided by Okasaki and a Clojure collection.  The choice of Clojure
collection depends on the data structure.

### BatchedQueue

The BatchedQueue data structure implements the `Queue` signature as well conforms to 
the `IPersistentList` protocol.  Some of these ideas come from Rich Hickey's
`PersistentQueue` Java class in clojure.core, but this is implemented in pure
Clojure.  BatchedQueue consists of a list and a vector.  Either can be `nil` but the
vector must be `nil` if the list is `nil`.  As with `PersistentQueue`, the vector
does not have to be reversed but is converted into a list.

To create a BatchedQueue, it is best to start with the `EMPTY` queue:

```clojure
(use 'funky-struct.queue)
;; -> nil
(def q EMPTY)
;; -> #'user/q
q
;; -> #queue{:front nil :rear nil}
```

From there more data can be added, either by the `.snoc` method:

```clojure
(-> EMPTY (.snoc :a) (.snoc :b) (.snoc :c))
;; -> #queue{:front (:a) :rear [:b :c]}
```

or the `cons` method (used here by `into`):

```clojure
(into EMPTY (range 5))
;; -> #queue{:front (0) :rear [1 2 3 4]}
```

The heads and tails can be found in the Okasaki or Clojure style:

```clojure
(def q (into EMPTY (range 5)))
;; -> #'user/q
(.head q)
;; -> 0
(peek q)
;; -> 0
(.tail q)
;; -> #queue{:front (1 2 3 4) :rear nil}
(pop q)
;; -> #queue{:front (1 2 3 4) :rear nil}
```

This implementation is faster than a list for retrieving data
from the opposite end that it is added (aka, a queue).  However, the 
vector has about the same performance even if the input is coerced
from a list to a vector.

## TO-DO

Several more of Okasaki's data structures will be implemented.

## License

Copyright © 2015 Andrew T. Flower

Distributed under the Eclipse Public License version 1.0.
