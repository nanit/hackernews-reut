(ns app.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.json :refer [wrap-json-response wrap-json-params]]
            [app.middleware :refer [wrap-auth]]
            ;; no CSRF token in api-defaults. consider adding site-defaults
            [app.controllers.users :as users]
            [app.controllers.posts :as posts]
            [app.controllers.votes :as votes]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]))

(defn parse-int [s]
  (Integer. s))

(defroutes post-routes
  (context "/posts" []
           (PUT "/" req (posts/add-post req))
           (GET "/:post-id" [post-id] (posts/get-post (parse-int post-id)))
           (POST "/:post-id" req (posts/update-post req))
           (route/not-found "Not Found")))

(defroutes user-routes
  (context "/users" []
           (GET "/:user-id" [user-id] (users/get-user (parse-int user-id)))
           (POST "/:user-id" req (users/update-user req))
           (route/not-found "Not Found")))

(defroutes vote-routes
  (context "/votes" []
           (PUT "/" req (votes/add-vote req))
           (GET "/:vote-id" [vote-id] (votes/get-vote (parse-int vote-id)))
           (DELETE "/:vote-id" [vote-id] (votes/delete-vote vote-id))
           (route/not-found "Not Found")))

(defroutes auth-routes
  post-routes
  user-routes
  vote-routes)

(defroutes api
  (POST "/login" req (users/login req))
  (PUT "/register" req (users/add-user req))
  (wrap-routes auth-routes wrap-auth))

(def app
  (-> api
      (wrap-defaults api-defaults)
      wrap-json-params
      wrap-json-response))
