(ns app.controller
  (:require [app.db.api :as db-api]
            [ring.util.response :refer [response]]
            [app.authentication.tokens :as tokens]
            ))


(defn login [req]
  (let  [json-params (:json-params req)
         username (get json-params "name")
         plaintext-password (get json-params "plaintext-password")
         encrypted-password (:password (db-api/get-user-by-name username))]
    (if (db-api/check-password plaintext-password encrypted-password)
      (response {:token (tokens/generate-token)})
      (response 403))))

(defn top-posts [req]
  "top posts")


;; posts
(defn add-post [req]
  (let [author-id (get-in req [:json-params "author-id"])
        text (get-in req [:json-params "text"])]
    (response (db-api/add-post author-id text))))

(defn get-post [post-id]
  (response (db-api/get-post post-id)))


;; users
(defn add-user [req]
  (let [params (:json-params req)
        name (get params "name")
        plaintext-password (get params "plaintext-password")]
    (response (db-api/add-user name plaintext-password))))

(defn get-user [id]
  ;; TODO: hide password
  (response (db-api/get-user id)))


;; votes
(defn add-vote [req]
  (let [voter-id (get-in req [:json-params "voter-id"])
        post-id (get-in req [:json-params "post-id"])]
    (response (db-api/add-vote voter-id post-id))))

(defn get-vote [id]
  (response (db-api/get-vote id)))


