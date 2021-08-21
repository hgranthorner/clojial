(ns clojial.utils)

(defn uuid
  []
  (.toString (java.util.UUID/randomUUID)))
