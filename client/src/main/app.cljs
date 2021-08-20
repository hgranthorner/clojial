(ns app
  (:require
   [devtools]
   [re-frame.core :as rf]
   [reagent.dom :as d]))

(defn ^:dev/after-load run-app
  []
  (println "Hello world")
  (d/render [:h1 "Hello world"]
            (.getElementById js/document "root")))

(defn ^:export main
  []
  ;(rf/dispatch-sync [:initialize])
  (run-app))
