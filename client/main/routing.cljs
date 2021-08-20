(ns routing
  (:require [db]
            [re-frame.core :as rf]))
  
(.addEventListener js/window "hashchange" (fn [^js/HashChangeEvent e] (rf/dispatch [:route-changed (.-newURL e)])))
