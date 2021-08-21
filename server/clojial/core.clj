(ns clojial.core
  (:require
   [clojial.utils :as utils]
   [ring.adapter.jetty :refer [run-jetty]]
   [ring.middleware.reload :as r]
   [ring.middleware.stacktrace :as st]
   [compojure.route :as route]
   [compojure.core :as c]
   [next.jdbc :as jdbc]
   [honey.sql :as sql]))

(defn run [_]
  (def some-uuid (utils/uuid))
  (println (str "Works! " some-uuid)))

(c/defroutes main-routes
  (c/GET "/" [] "Hello world7")
  (route/not-found "Not found"))

(defn start-server
  []
  (run-jetty
   (r/wrap-reload #'main-routes {:dirs ["server"]})
   {:port 3000
    :join? false}))

(comment
  (try
   (def server (start-server))
   (catch Exception _
       (do
         (.stop server)
         (def server (start-server)))))

 (do
   (def db-options {:dbtype "sqlite"
                    :dbname "data.db"})
   (def ds (jdbc/get-datasource db-options))
   (def insert-q {:insert-into [:posts]
                  :columns [:id :name :description]
                  :values [[(utils/uuid) "First Post" "What this is all about"]]})
   (jdbc/execute! ds (sql/format insert-q))
   (def q {:select [:*]
           :from [:posts]})
   (def posts (jdbc/execute! ds (sql/format q)))))
