(ns app.db.votes
  (:require [app.db.helpers :as helpers]
            [honeysql.helpers :as hsql-helpers]))


(defn add-vote [user-id post-id]
  (helpers/execute (->
                    (hsql-helpers/insert-into :votes)
                    (hsql-helpers/values [{:voter-id user-id
                                           :post-id post-id}]))))

(defn delete-vote [vote-id]
  (helpers/execute
   (->
    (hsql-helpers/delete-from :votes)
    (:where [:= :id vote-id]))))

(defn get-vote [vote-id]
  (first
   (helpers/query
    {:select [:*]
     :from [:votes]
     :where [:= :id vote-id]})))

(defn delete-vote [vote-id]
  (helpers/execute
   (->
    (hsql-helpers/delete-from :votes)
    (hsql-helpers/where [:= :id vote-id]))))
