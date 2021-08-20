(ns db
  (:require
   [utils]
   [clojure.string :as s]
   [re-frame.core :as rf]))
            

(rf/reg-event-db
 :initialize
 (fn [_ _]
  {:posts [{:id (random-uuid) :name "First post" :description "What this is all about"}
           {:id (random-uuid) :name "Second post" :description "Some content"}]}))

(rf/reg-event-db
 :add-post
 (fn [db [_ post]]
   (update db :posts #(conj % post))))

(rf/reg-event-db
 :route-changed
 (fn [db [_ url]]
   (assoc db :url
    (let [route (-> url
                       (s/split #"#")
                       second)]
         (if (some? route)
           (subs route 1)
           "")))))

(rf/reg-sub
 :db
 (fn [db _]
   db))

(rf/reg-sub
 :posts
 (fn [db _]
   (get db :posts)))

(comment
  (let [url "http://localhost:8080/"
        route (-> url
                  (s/split #"#")
                  second)]
    (if (some? route)
      (subs route 1)
      ""))


  (rf/dispatch-sync [:add-post {:id (random-uuid) :name "First post" :description "What this is all about"}])
  (println "Hello world"))
