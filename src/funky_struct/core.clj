(ns funky-struct.core)

(defprotocol Queue
  (is-empty? [q])
  (snoc [q x])
  (head [q])
  (tail [q]))
