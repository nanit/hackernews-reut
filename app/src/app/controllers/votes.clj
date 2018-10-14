(ns app.controllers.votes
  (:require [app.db.votes :as votes]
            [ring.util.response :refer [response]]
            [app.authentication.tokens :as tokens]))


;; votes
(defn add-vote [req]
  (let [voter-id (get-in req [:json-params "voter-id"])
        post-id (get-in req [:json-params "post-id"])]
    (response (votes/add-vote voter-id post-id))))

(defn get-vote [id]
  (response (votes/get-vote id)))

(defn delete-vote [vote-id]
  (response (votes/delete-vote vote-id)))
