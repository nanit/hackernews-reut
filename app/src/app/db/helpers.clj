(ns app.db.helpers
  (:require [honeysql.core :as sql]
            [clojure.java.jdbc :as j]
            [honeysql.helpers :as hsql-helpers]
            [environ.core :refer [env]]))

(def conn
  {:dbtype "postgresql"
   :dbname "hackernews"
   :host (get env :db-host "127.0.0.1")
   :user "postgres"
   :password "password"
   :ssl true
   :sslfactory "org.postgresql.ssl.NonValidatingFactory"})

(defn query [q]
  (j/query conn (sql/format (sql/build q))))

(defn execute [q]
  (j/execute! conn (sql/format (sql/build q)) {:return-keys true}))

(defn delete-db []
  (doall (map #(execute
                (->
                 (hsql-helpers/delete-from %))) [:votes :posts :users] )))

