(ns app.controllers.posts
  (:require [ring.util.response :refer [response]]
            [app.authentication.tokens :as tokens]
            [app.db.posts :as posts]))


(defn top-posts [req]
  "top posts")

(defn add-post [req]
  (let [author-id (get-in req [:json-params "author-id"])
        text (get-in req [:json-params "text"])]
    (response (posts/add-post author-id text))))

(defn get-post [post-id]
  (response (posts/get-post post-id)))

(defn update-post [req]
  (let [post-id (get-in req [:json-params "post-id"])
        text (get-in req [:json-params "text"])]
    (response (posts/update-post post-id text))))

(defn delete-post [post-id]
  (response posts/delete-post post-id))


