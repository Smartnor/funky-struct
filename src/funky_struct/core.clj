(ns funky-struct.core
  (:refer-clojure :rename {cons core-cons}
                  :exclude [reverse]))

(declare cons)

; Protocol definitions
(defprotocol Queue
  (is-empty? [q])
  (snoc [q x])
  (head [q])
  (tail [q]))

; Private functions
(defn- reverse'
  [s r]
  (if (empty? s)
    r
    (recur (rest s)
           (cons (first s) r))))

; Common functions
(defn cons
  [x r]
  (lazy-seq (core-cons x r)))
(defn ++
  [s t]
  (lazy-seq (if (empty? s)
              t
              (cons (first s)
                    (++ (rest s) t)))))
(defn reverse
  [s]
  (lazy-seq (reverse' s '())))
