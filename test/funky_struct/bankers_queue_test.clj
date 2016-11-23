(ns funky-struct.bankers-queue-test 
  (:use expectations
        funky-struct.bankers-queue))

; Check constuctor
(expect (more-> 3 .lenf
                '(:a :b :c) .f
                2 .lenr
                '(:e :d) .r)
        (bankers-queue '(:a :b :c) '(:e :d)))
(expect AssertionError (bankers-queue :goat :sheep))

; Check empty batched-queues
(expect (more-> 0 .lenf
                '() .f
                0 .lenr
                '() .r)
        EMPTY)

; Populate batched-queue and check for batched-queue protocol fulfillment
(expect true (.is-empty? EMPTY))
(expect (more-> 3 .lenf
                '(:a :b :c) .f
                2 .lenr
                '(:e :d) .r)
        (-> EMPTY 
            (.snoc :a)
            (.snoc :b)
            (.snoc :c)
            (.snoc :d)
            (.snoc :e)))
(expect :a (-> (bankers-queue '(:a) '(:c :b)) .head))
(expect (more-> 2 .lenf
                '(:b :c) .f
                0 .lenr
                '() .r)
        (-> (bankers-queue '(:a) '(:c :b)) .tail))

; Make sure that it participates in the IPersistentList abstraction
; Sequence
(expect '(:a :b :c :d :e) (bankers-queue '(:a :b :c) '(:e :d)))

; Count
(expect 5 (->(bankers-queue '(:a :b :c) '(:e :d)) .count))

; Cons (demonstrated by using into)
(expect (more-> '(0 1 2 3 4 5 6) .f
                '(9 8 7) .r)
        (into EMPTY (range 10)))

; Use a constructed value for further tests
(def FIVER (into EMPTY (range 5)))

; Empty
(expect EMPTY (-> (into EMPTY (range 5)) .empty))

; Equivalence
(expect true (= FIVER
                (bankers-queue '(0 1 2) '(4 3))))
(expect false (= FIVER
                 :Goat))
(expect false (= FIVER
                 EMPTY))

; Peek
(expect 0 (-> FIVER .peek))

; Pop (and more cons tests)
(expect (more-> '(1 2) .f
                '(4 3) .r)
        (-> FIVER .pop))
(expect (more-> '(2 3 4) .f
                '() .r)
        (-> FIVER .pop .pop))
(expect (more-> '(2 3 4) .f
                '(5) .r)
        (-> FIVER .pop .pop (.cons 5)))
(expect (more-> '(2 3 4) .f
                '(6 5) .r)
        (-> FIVER .pop .pop (.cons 5) (.cons 6)))
(expect (more-> '(3 4) .f
                '(6 5) .r)
        (-> FIVER .pop .pop (.cons 5) (.cons 6) .pop))
