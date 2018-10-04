(ns app.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            ;; no CSRF token in api-defaults. consider adding site-defaults
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]))

(defn add-post [req]
  "add post")

(defn top-posts [req]
  "top posts")

(defn delete-post [req]
  "delete post")

(defn get-post [req]
  "get post")

(defroutes post-routes
  (context "/posts" []
           (PUT "/" req (add-post req))
           (GET "/top" req (top-posts req))
           (DELETE "/:post-id" req (delete-post req))
           (GET "/:post-id" req (get-post req))
           (route/not-found "Not Found")))

(def app
  (wrap-defaults post-routes api-defaults))
