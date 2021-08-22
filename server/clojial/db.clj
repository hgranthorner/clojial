(ns clojial.db
  (:require
   [next.jdbc :as jdbc]
   [honey.sql :as sql]))


(def db-options {:dbtype "sqlite"
                 :dbname "data.db"})

(def ds (jdbc/get-datasource db-options))

(defn- query [q]
  (jdbc/execute! ds (sql/format q)))

(defn get-posts
  []
  (query {:select [:*]
           :from [:posts]}))

(comment
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
    (def posts (jdbc/execute! ds (sql/format q))))
  (comment))
