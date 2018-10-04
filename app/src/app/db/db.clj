(ns app.db
  (:require [honeysql.core :as sql]
            [honeysql.helpers :refer :all :as helpers]
            [clojure.java.jdbc :as j]
            [environ.core :refer [env]]))

(def conn
  {:dbtype "postgresql"
   :dbname "hackernews"
   :host (get env :db-host "127.0.0.1")
   :user "postgres"
   :password "password"
   :ssl true
   :sslfactory "org.postgresql.ssl.NonValidatingFactory"})


(defn add-user [name]
  (->
   (insert-into :users)
   (values [{:name name}])))

(defn get-user-by-name [name]
  {:select :*
   :from :users
   :where [:= :name name]})


(defn get-post-by-id [id]
  {:select [:*]
   :from [:posts]
   :where [:= :id id]})


(defn query [q]
  (j/query conn (sql/format (sql/build q))))

(defn execute [q]
  (j/execute! conn (sql/format (sql/build q))))

;;(execute (add-user "aerlin"))
(query (get-user-by-name "reut"))
