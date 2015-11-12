(ns funky-struct.queue-test
  (:use expectations
        funky-struct.queue))

; Check constuctor
(expect (more-> '(:a) .front
                [:b :c] .rear)
        (queue '(:a) [:b :c]))
(expect AssertionError (queue :goat :sheep))

; Check empty queues
(expect (more-> nil .front
                nil .rear)
        EMPTY)

; Populate queue and check for Queue protocol fulfillment
(expect true (.is-empty? EMPTY))
(expect (more-> '(:a) .front 
                [:b :c] .rear)
        (-> EMPTY  (.snoc :a) (.snoc :b) (.snoc :c)))
(expect :a (-> (queue '(:a) [:b :c]) .head))
(expect (more-> '(:b :c) .front
                nil .rear)
        (-> (queue '(:a) [:b :c]) .tail))

; Make sure that it participates in the IPersistentList abstraction
; Sequence
(expect '(:a :b :c) (queue '(:a) [:b :c]))

; Count
(expect 4 (-> (queue '(:a :b) [:c :d]) .count))

; Cons (demonstrated by using into)
(expect (more-> '(0) .front
                [1 2 3 4 5 6 7 8 9] .rear)
        (into EMPTY (range 10)))

; Use a constructed value for further tests
(def FIVER (into EMPTY (range 5)))

; Empty
(expect EMPTY (-> (into EMPTY (range 5)) .empty))

; Equivalence
(expect true (= FIVER
                (queue '(0) [1 2 3 4])))
(expect false (= FIVER
                 :Goat))
(expect false (= FIVER
                 EMPTY))

; Peek
(expect 0 (-> FIVER .peek))

; Pop (and more cons tests)
(expect (more-> '(1 2 3 4) .front
                nil .rear)
        (-> FIVER .pop))
(expect (more-> '(2 3 4) .front
                nil .rear)
        (-> FIVER .pop .pop))
(expect (more-> '(2 3 4) .front
                [5] .rear)
        (-> FIVER .pop .pop (.cons 5)))
(expect (more-> '(2 3 4) .front
                [5 6] .rear)
        (-> FIVER .pop .pop (.cons 5) (.cons 6)))
(expect (more-> '(3 4) .front
                [5 6] .rear)
        (-> FIVER .pop .pop (.cons 5) (.cons 6) .pop))

; ILookup abstraction
(expect '(0) (:front FIVER))
(expect [1 2 3 4]  (:rear FIVER))
(expect nil (:goat FIVER))
(expect "Maa!!!" (.valAt FIVER :goat "Maa!!!"))

; Human-readable representation
(expect "#queue{:front (0) :rear [1 2 3 4]}" (str FIVER))
