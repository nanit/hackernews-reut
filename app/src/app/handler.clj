(ns app.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.util.response :refer [response]]
            [ring.middleware.json :refer [wrap-json-response wrap-json-params]]
            [app.db.api :as db-api]
            ;; no CSRF token in api-defaults. consider adding site-defaults

            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [app.db.api :as db]))

(defn parse-int [s]
  (Integer. s))

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
  (response (db-api/add-user (get-in req [:json-params "name"]))))

(defn get-user [id]
  (response (db-api/get-user id)))


;; votes
(defn add-vote [req]
  (let [voter-id (get-in req [:json-params "voter-id"])
        post-id (get-in req [:json-params "post-id"])]
    (response (db-api/add-vote voter-id post-id))))

(defn get-vote [id]
  (response (db-api/get-vote id)))



(defroutes post-routes
  (context "/posts" []
           (PUT "/" req (add-post req))
           (GET "/:post-id" [post-id] (get-post (parse-int post-id)))
           (route/not-found "Not Found")))

(defroutes user-routes
  (context "/users" []
           (PUT "/" req (add-user req))
           (GET "/:id" [id] (get-user (parse-int id)))
           (route/not-found "Not Found")))

(defroutes vote-routes
  (context "/votes" []
           (PUT "/" req (add-vote req))
           (GET "/:id" [id] (get-vote (parse-int id)))
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
