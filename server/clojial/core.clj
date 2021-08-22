(ns clojial.core
  (:require
   [clojial.db :as db]
   [clojial.utils :as utils]
   [clojure.data.json :as json]
   [clojure.string :refer [starts-with?]]
   [ring.adapter.jetty :refer [run-jetty]]
   [ring.middleware.reload :as r]
   [ring.middleware.stacktrace :as st]
   [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
   [ring.util.response :as resp]
   [compojure.route :as route]
   [compojure.core :refer [defroutes GET]]))
   
(defn run [_]
  (def some-uuid (utils/uuid))
  (println (str "Works! " some-uuid)))

(defn get-posts
  []
  (db/get-posts))

(defn to-json
  [x]
  {:headers {"Content-type" "application/json"
             "Access-Control-Allow-Origin" "*"}
   :body (json/write-str x)})

(defroutes main-routes
  (GET "/api/hello" [] "Hello world")
  (GET "/api/egg" [] "egg")
  (GET "/api/posts" [] (to-json (get-posts)))
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
   {:port 8081
    :join? false}))

(comment
  (try
   (def server (start-server))
   (catch Exception _
       (do
         (.stop server)
         (def server (start-server)))))

  (println "hello")
  (comment))
