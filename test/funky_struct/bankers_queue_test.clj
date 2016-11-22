(ns funky-struct.bankers-queue-test 
  (:use expectations
        funky-struct.bankers-queue))

; Check constuctor
(expect (more-> 1 .lenf
                '(:a) .f
                2 .lenr
                '(:c :b) .r)
        (bankers-queue '(:a) '(:c :b)))
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
