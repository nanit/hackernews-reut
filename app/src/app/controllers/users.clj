(ns app.controllers.users
  (:require [ring.util.response :refer [response]]
            [app.authentication.tokens :as tokens]
            [app.db.users :as users]))

(defn login [req]
  (let  [json-params (:json-params req)
         username (get json-params "name")
         plaintext-password (get json-params "plaintext-password")
         user (users/get-user-by-name username)
         user-id (:id user)
         encrypted-password (:password user)]
    (if (users/check-password plaintext-password encrypted-password)
      (response {:token (tokens/generate-token user-id)})
      (response 403))))

(defn add-user [req]
  (let [params (:json-params req)
        name (get params "name")
        plaintext-password (get params "plaintext-password")]
    (response (users/add-user name plaintext-password))))

(defn get-user [user-id]
  ;; TODO: hide password
  (response (users/get-user user-id)))

(defn update-user [req]
  (let [params (:json-params req)
        user-id (get params "user-id")
        name (get params "name")
        plaintext-password (get params "plaintext-password")]
    (response (users/update-user user-id name plaintext-password))))

(defn delete-user [user-id]
  (response (users/delete-user user-id)))
