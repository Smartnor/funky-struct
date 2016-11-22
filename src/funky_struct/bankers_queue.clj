(ns funky-struct.bankers-queue
  (:require [funky-struct.core :as f]))

(declare EMPTY)
(declare check)

(deftype BankersQueue [lenf f lenr r]
  f/Queue
  (is-empty? [_] (zero? lenf))
  (snoc [_ x] (check lenf f (inc lenr) (f/cons x r)))
  (head [_] (first f))
  (tail [_]
    (if (zero? lenf)
      EMPTY
      (check (dec lenf) (rest f) lenr r))))

(defn bankers-queue
  "BankersQueue constructor"
  [f r]
  {:pre [(and (list? f)
              (list? r)
              (or (and (empty? f)
                       (empty? r))
                  (not (empty? f))))]}
  (BankersQueue. (count f) f (count r) r))

(def EMPTY (BankersQueue. 0 '() 0 '()))

(defn- check
  [lenf f lenr r]
  (if (<= lenr lenf)
    (BankersQueue. lenf f lenr r)
    (BankersQueue. (+ lenf lenr) (f/++ f (f/reverse r)) 0 '())))

