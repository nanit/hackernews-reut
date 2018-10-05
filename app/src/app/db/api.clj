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


;; users
(defn add-user [name]
  (execute (->
            (insert-into :users)
            (values [{:name name}]))))

(defn get-user [id]
  (first
   (query
    {:select :*
     :from :users
     :where [:= :id id]})))

(defn delete-user [id]
  (execute
   (->
    (delete-from :users)
    (:where [:= :id id]))))

(defn get-post [id]
  (first (query
          {:select [:*]
           :from [:posts]
           :where [:= :id id]})))


;; posts
(defn add-post [author-id post-text]
  (execute (->
            (insert-into :posts)
            (values [{:author_id author-id
                      :text post-text}]))))

(defn delete-post [id]
  (execute
   (->
    (delete-from :posts)
    (:where [:= :id id]))))

(defn get-post [id]
  (first (query
          {:select [:*]
           :from [:posts]
           :where [:= :id id]})))


;; votes
(defn add-vote [user-id post-id]
  (execute (->
            (insert-into :votes)
            (values [{:voter_id user-id
                      :post-id post-id}]))))

(defn delete-vote [id]
  (execute
   (->
    (delete-from :votes)
    (:where [:= :id id]))))

(defn get-vote [id]
  (first (query
          {:select [:*]
           :from [:votes]
           :where [:= :id id]})))
