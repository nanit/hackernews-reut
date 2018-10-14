(ns app.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.json :refer [wrap-json-response wrap-json-params]]
            [app.middleware :refer [wrap-auth]]
            ;; no CSRF token in api-defaults. consider adding site-defaults

            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [app.controller :as controller]))

(defn parse-int [s]
  (Integer. s))

(defroutes post-routes
  (context "/posts" []
           (PUT "/" req (controller/add-post req))
           (GET "/:post-id" [post-id] (controller/get-post (parse-int post-id)))
           (route/not-found "Not Found")))

(defroutes user-routes
  (context "/users" []
           (GET "/:id" [id] (controller/get-user (parse-int id)))
           (route/not-found "Not Found")))

(defroutes vote-routes
  (context "/votes" []
           (PUT "/" req (controller/add-vote req))
           (GET "/:id" [id] (controller/get-vote (parse-int id)))
           (route/not-found "Not Found")))

(defroutes auth-routes
  post-routes
  user-routes
  vote-routes)

(defroutes api
  (POST "/login" req (controller/login req))
  (PUT "/register" req (controller/add-user req))
  (wrap-routes auth-routes wrap-auth))

(def app
  (-> api
      (wrap-defaults api-defaults)
      wrap-json-params
      wrap-json-response))
