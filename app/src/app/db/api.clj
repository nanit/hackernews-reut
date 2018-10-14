(ns app.db.api
  (:require [honeysql.core :as sql]
            [honeysql.helpers :refer :all :as helpers]
            [clojure.java.jdbc :as j]
            [crypto.password.bcrypt :as password]
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

(defn encrypt-password [plaintext-password]
  (password/encrypt plaintext-password))

(defn check-password [raw encrypted]
  (password/check raw encrypted))

;; users
(defn add-user [name plaintext-password]
  (execute (->
            (insert-into :users)
            (values [{:name name
                      :password (encrypt-password plaintext-password)}]))))

(defn get-user [id]
  (first
   (query
    {:select :*
     :from :users
     :where [:= :id id]})))

(defn get-user-by-name [name]
  (first
   (query
    {:select :*
     :from :users
     :where [:= :name name]})))

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
  (first
   (query
    {:select [:*]
     :from [:votes]
     :where [:= :id id]})))
