(ns db
  (:require
   [utils]
   [clojure.string :as s]
   [re-frame.core :as rf]
   [day8.re-frame.http-fx]
   [ajax.core :as ajax]))
            

(rf/reg-event-fx
 :initialize
 (fn [_ _]
  {:fx [[:dispatch [:get-posts]]]}))

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

(rf/reg-event-fx
 :get-posts
 (fn [{:keys [db]} _]
   {:http-xhrio {:method          :get
                 :uri             "http://localhost:8081/api/posts"
                 :timeout         8000                                           ;; optional see API docs
                 :response-format (ajax/json-response-format {:keywords? true})  ;; IMPORTANT!: You must provide this.
                 :on-success      [:get-posts-success]
                 :on-failure      [:get-posts-failure]}}))

(rf/reg-event-db
 :get-posts-success
 (fn [db [_ posts]]
   (assoc db :posts posts)))

(rf/reg-event-fx
 :get-posts-failure
 (fn [_ [_ x]]
   (println x)))


(rf/reg-sub
 :db
 (fn [db _]
   db))

(rf/reg-sub
 :posts
 (fn [db _]
   (get db :posts)))

(rf/reg-sub
 :url
 (fn [db _]
   (get db :url)))

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
