(ns clojial.core
  (:require
   [clojial.utils :as utils]
   [clojure.string :refer [starts-with?]]
   [ring.adapter.jetty :refer [run-jetty]]
   [ring.middleware.reload :as r]
   [ring.middleware.stacktrace :as st]
   [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
   [ring.util.response :as resp]
   [compojure.route :as route]
   [compojure.core :refer [defroutes GET]]
   [next.jdbc :as jdbc]
   [honey.sql :as sql]))

(defn run [_]
  (def some-uuid (utils/uuid))
  (println (str "Works! " some-uuid)))

(defroutes main-routes
  (GET "/api/hello" [] "Hello world")
  (GET "/api/egg" [] "egg")
  (route/resources "/")
  (route/not-found "Not found"))

(defn- wrap-static
  [handler]
  (fn [request]
    (let [uri (:uri request)]
     (handler
      (if-not (or (starts-with? uri "/api/") (starts-with? uri "/static/"))
        (assoc request :uri "/index.html")
        request)))))

(defn start-server
  []
  (run-jetty
   (-> (r/wrap-reload #'main-routes {:dirs ["server"]})
       (wrap-defaults site-defaults)
       wrap-static)
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
