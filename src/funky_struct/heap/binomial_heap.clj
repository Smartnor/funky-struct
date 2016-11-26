(ns funky-struct.heap.binomial-heap
  (:require [funky-struct.core :as f])
  (:refer-clojure :exclude [merge]))

(declare EMPTY)
(declare ins-tree)
(declare rank)
(declare link)
(declare root)
(declare remove-min-tree)

(deftype Node [r x c]
  clojure.lang.ILookup
  (valAt [t k] (.valAt t k nil))
  (valAt [t k not-found]
    (condp = k
      :r (.r t)
      :x (.x t)
      :c (.c t)
      not-found))

  Object
  (toString [t] (pr-str t)))

(deftype BinomialHeap [ts]
  f/Heap
  (is-empty? [_] (empty? ts))
  (insert [_ x]
    (cond
      (and (empty? ts)
           (not (instance? Comparable
                           x))) (throw (Exception. (str "The item you are inserting "
                                                        "is not an instance of Comparable")))
      (and (not (empty? ts))
           (not (instance? (class (.x (first ts)))
                           x))) (throw (Exception. (str "The item you are inserting "
                                                        "is an instance of "
                                                        (class x)
                                                        " rather than "
                                                        (class (.x (first ts)))
                                                        " like the other items"))) 
      :else (BinomialHeap. (ins-tree (Node. 0 x '()) ts))))
  (merge [h1 h2]
    (cond
      (not (instance? BinomialHeap h2)) (throw (Exception. (str "The second parameter should "
                                                                "have class BinomialHeap "
                                                                "rather than " 
                                                                (class h2))))
      (empty? (.ts h2)) h1
      (empty? (.ts h1)) h2
      :else (let [ts1 (.ts h1)
                  ts2 (.ts h2)
                  t1 (first ts1)
                  ts1' (rest ts1)
                  t2 (first ts2)
                  ts2' (rest ts2)]
              (cond
                (< (rank t1) (rank t2)) (BinomialHeap. (cons t1
                                                             (.ts (.merge (BinomialHeap. ts1')
                                                                          (BinomialHeap. ts2)))))
                (< (rank t2) (rank t1)) (BinomialHeap. (cons t2
                                                             (.ts (.merge (BinomialHeap. ts1)
                                                                          (BinomialHeap. ts2')))))
                :else (BinomialHeap. (ins-tree (link t1 t2)
                                               (.ts (.merge (BinomialHeap. ts1')
                                                            (BinomialHeap. ts2')))))))))
  (find-min [_] (root (first (remove-min-tree ts))))
  (delete-min [_]
    (let [[t1 ts2] (remove-min-tree ts)
          ts1 (.c t1)]
      (.merge (BinomialHeap. (reverse ts1))
              (BinomialHeap. ts2))))

  clojure.lang.ILookup
  (valAt [h k] (.valAt h k nil))
  (valAt [h k not-found]
    (condp = k
      :ts (.ts h)
      not-found))

  Object
  (toString [h] (pr-str h)))

(def EMPTY (BinomialHeap. '()))

(defn- rank [t] (.r t))
(defn- root [t] (.x t))
(defn- link
  [t1 t2]
  (if (<= (.x t1) (.x t2))
    (Node. (inc (.r t1)) (.x t1) (cons t2 (.c t1)))
    (Node. (inc (.r t1)) (.x t2) (cons t1 (.c t2)))))
(defn- ins-tree
  [t ts]
  (if (empty? ts)
    (list t)
    (if (< (rank t) (rank (first ts)))
      (cons t ts)
      (recur (link t (first ts)) (rest ts)))))
(defn- remove-min-tree
  [ts]
  (condp = (count ts)
    0 [nil '()]
    1 [(first ts) '()]
    (let [t (first ts) [t' ts'] (remove-min-tree (rest ts))]
      (if (<= (root t) (root t'))
        [t (rest ts)]
        [t' (cons t ts')]))))

(defmethod print-method Node [o ^java.io.Writer w]
  (.write w (str "#node{:r "
                 (pr-str (.r o))
                 " :x "
                 (pr-str (.x o))
                 " :c "
                 (pr-str (.c o))
                 "}")))

(defmethod print-method BinomialHeap [o ^java.io.Writer w]
  (.write w (str "#binomial-heap{:ts "
                 (pr-str (.ts o))
                 "}")))
