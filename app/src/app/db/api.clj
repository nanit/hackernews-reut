(ns app.db.api
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

(defn query [q]
  (j/query conn (sql/format (sql/build q))))

(defn execute [q]
  (j/execute! conn (sql/format (sql/build q)) {:return-keys true}))

(defn delete-db []
  (doall (map #(execute
                (->
                 (delete-from %))) [:votes :posts :users] )))

(defn add-user [name]
  (execute (->
            (insert-into :users)
            (values [{:name name}]))))

(defn get-user-by-name [name]
  (first
   (query
    {:select :*
     :from :users
     :where [:= :name name]})))

(defn delete-user-by-name [name]
  (execute
   (->
    (delete-from :users)
    (:where [:= :name name]))))


(defn get-post-by-id [id]
  (query
   {:select [:*]
    :from [:posts]
    :where [:= :id id]}))



(defn add-post [user-id post-text]
  (execute (->
            (insert-into :posts)
            (values [{:author_id user-id
                      :text post-text}]))))

(defn add-vote [user-id post-id]
  (execute (->
            (insert-into :votes)
            (values [{:voter_id user-id
                      :post-id post-id}]))))
