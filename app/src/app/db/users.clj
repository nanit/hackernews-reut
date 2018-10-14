(ns app.db.users
  (:require [app.db.helpers :as helpers]
            [crypto.password.bcrypt :as password]
            [honeysql.helpers :as hsql-helpers]))

(defn encrypt-password [plaintext-password]
  (password/encrypt plaintext-password))

(defn check-password [raw encrypted]
  (password/check raw encrypted))


(defn add-user [name plaintext-password]
  (helpers/execute (->
                    (hsql-helpers/insert-into :users)
                    (hsql-helpers/values [{:name name
                                           :password (encrypt-password plaintext-password)}]))))

(defn get-user [user-id]
  (first
   (helpers/query
    {:select :*
     :from :users
     :where [:= :id user-id]})))

(defn get-user-by-name [name]
  (first
   (helpers/query
    {:select :*
     :from :users
     :where [:= :name name]})))

(defn delete-user [user-id]
  (helpers/execute
   (->
    (hsql-helpers/delete-from :users)
    (:where [:= :id user-id]))))

(defn update-user [user-id name plaintext-password]
  (helpers/execute (->
                    (hsql-helpers/update :users)
                    (hsql-helpers/sset {:name name
                                        :plaintext-password plaintext-password})
                    (hsql-helpers/where [:= :id user-id]))))
