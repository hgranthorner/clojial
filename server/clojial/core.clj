(ns clojial.core
  (:require
   [utils]
   [next.jdbc :as jdbc]
   [honey.sql :as sql]))

(defn run [_]
  (def some-uuid (uuid))
  (println (str "Works! " some-uuid)))

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
   (def posts (jdbc/execute! ds (sql/format q)))))
