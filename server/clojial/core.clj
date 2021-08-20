(ns clojial.core
  (:import java.util.UUID))

(defn run [_]
  (def some-uuid (. UUID randomUUID))
  (println (str "Works! " some-uuid)))
