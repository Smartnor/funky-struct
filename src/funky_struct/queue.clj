(ns funky-struct.queue
  (:require [funky-struct.core :as f]))

(declare EMPTY)

(deftype BatchedQueue [front rear]
  f/Queue
  (is-empty? [_] (nil? front))
  (snoc [q x] (if (nil? front)
                (BatchedQueue. (list x) nil)
                (BatchedQueue. front (if (nil? rear)
                                       [x]
                                       (conj rear x)))))
  (head [_] (first front))
  (tail [q]
    (if (nil? front)
      EMPTY
      (if (empty? (rest front))
        (BatchedQueue. (seq rear) nil)
        (BatchedQueue. (rest front) rear))))

  clojure.lang.IPersistentList
  (seq [_] (if (nil? front)
             nil
             (if (nil? rear)
               front
               (concat front (seq rear)))))
  (count [_] (+ (count front) (count rear)))
  (cons [q x] (.snoc q x))
  (empty [_] EMPTY)
  (equiv [_ other] (and (instance? BatchedQueue other)
                        (= front (.front other))
                        (= rear (.rear other))))
  (peek [q] (.head q))
  (pop [q] (.tail q))

  clojure.lang.ILookup
  (valAt [q k] (.valAt q k nil))
  (valAt [q k not-found]
    (condp = k
      :front (.front q)
      :rear (.rear q)
      not-found))

  Object
  (toString [q] (pr-str q)))

(defmethod print-method BatchedQueue [o ^java.io.Writer w]
  (.write w (str "#queue{:front "
                 (pr-str (.front o))
                 " :rear "
                 (pr-str (.rear o))
                 "}")))

(defn queue
  "BatchedQueue constructor.
  The first argument should be nil or list and
  the second argument should be nil or vector."
  [l v]
  {:pre [(or (and (nil? l) (nil? v))
             (and (list? l) (not (empty? l)) (or (nil? v)
                                                 (and (vector? v) (not (empty? v))))))]}
  (BatchedQueue. l v))

(def EMPTY (queue nil nil))
