(ns funky-struct.core-test
  (:refer-clojure :exclude [cons reverse])
  (:require [expectations :refer :all]
            [funky-struct.core :refer :all]))

; Test common functions
(expect '(:a :b :c :d)
        (cons :a '(:b :c :d)))
(expect '(:a :b :c :d)
        (++ '(:a :b) '(:c :d)))
(expect :a
        (first (++ '(:a :b) '(:c :d))))
(expect '(:d :c :b :a)
        (reverse '(:a :b :c :d)))
(expect :d
        (first (reverse '(:a :b :c :d))))
