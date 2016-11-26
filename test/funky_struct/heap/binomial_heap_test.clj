(ns funky-struct.heap.binomial-heap-test
  (:use expectations
        funky-struct.heap.binomial-heap))

; Check empty heap
(expect (more-> '() .ts)
        EMPTY)

(def FIVER (-> EMPTY
               (.insert 5)
               (.insert 7)
               (.insert 144)
               (.insert 3)
               (.insert 12)))

; Populate heap and check for heap protocol fulfillment
(expect true (.is-empty? EMPTY))
(expect false (.is-empty? FIVER))
(expect funky_struct.heap.binomial_heap.BinomialHeap
        (.insert FIVER 77))
(expect Exception (.insert FIVER :goat))
(expect funky_struct.heap.binomial_heap.BinomialHeap
        (.merge FIVER
                (-> EMPTY
                    (.insert 77)
                    (.insert 490))))
(expect Exception (.merge FIVER :goat))
(expect 3 (.find-min FIVER))
(expect 7 (.find-min (-> FIVER
                         .delete-min
                         .delete-min)))
(expect true (.is-empty? (-> FIVER
                             .delete-min
                             .delete-min
                             .delete-min
                             .delete-min
                             .delete-min)))

; Make sure that it participates in the IPersistentList abstraction
; Sequence
(expect '(12 3 5 7 144) (seq FIVER))

; Count
(expect 5 (count FIVER))

; Cons (demonstrated by using into)
(expect '(8 9 0 4 6 7 5 2 3 1)
        (seq (into EMPTY (range 10))))

; Empty
(expect EMPTY (-> (into EMPTY (range 5)) .empty))

; Equivalence
(expect true (= FIVER
                (-> EMPTY
                    (.insert 5)
                    (.insert 7)
                    (.insert 144)
                    (.insert 3)
                    (.insert 12))))
(expect false (= FIVER
                 :Goat))
(expect false (= FIVER
                 EMPTY))
