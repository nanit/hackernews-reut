(ns app.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.util.response :refer [response]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-params]]
            [app.db.api :as db-api]
            ;; no CSRF token in api-defaults. consider adding site-defaults

            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]))



(defn top-posts [req]
  "top posts")


;; posts
(defn add-post [req]
  (let [author-id (get-in req [:json-params "author-id"])
        text (get-in req [:json-params "text"])]
    (response (db-api/add-post author-id text))))


;; users
(defn add-user [req]
  (response (db-api/add-user (get-in req [:json-params "name"]))))


;; votes
(defn add-vote [req]
  (let [voter-id (get-in req [:json-params "voter-id"])
        post-id (get-in req [:json-params "post-id"])]
    (response (db-api/add-vote voter-id post-id))))


(defroutes post-routes
  (context "/posts" []
           (PUT "/" req (add-post req))
           (route/not-found "Not Found")))

(defroutes user-routes
  (context "/users" []
           (PUT "/" req (add-user req))
           (route/not-found "Not Found")))

(defroutes vote-routes
  (context "/votes" []
           (PUT "/" req (add-vote req))
           (route/not-found "Not Found")))

(defroutes api
  post-routes
  user-routes
  vote-routes)

(def app
  (-> api
      (wrap-defaults api-defaults)
      wrap-json-params
      wrap-json-response))
