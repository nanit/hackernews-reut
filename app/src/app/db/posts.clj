(ns app.db.posts
  (:require [app.db.helpers :as helpers]
            [honeysql.helpers :as hsql-helpers]))


(defn get-post [user-id]
  (first (helpers/query
          {:select [:*]
           :from [:posts]
           :where [:= :id user-id]})))


(defn add-post [author-id post-text]
  (helpers/execute (->
                    (hsql-helpers/insert-into :posts)
                    (hsql-helpers/values [{:author_id author-id
                                           :text post-text}]))))

(defn delete-post [post-id]
  (helpers/execute
   (->
    (hsql-helpers/delete-from :posts)
    (hsql-helpers/where [:= :id post-id]))))

(defn get-post [post-id]
  (first (helpers/query
          {:select [:*]
           :from [:posts]
           :where [:= :id post-id]})))

(defn update-post [post-id text]
  (helpers/execute (->
                    (hsql-helpers/update :posts)
                    (hsql-helpers/sset {:text text})
                    (hsql-helpers/where [:= :id post-id]))))
