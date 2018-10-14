(ns app.middleware
  (:require [app.authentication.tokens :refer (authenticated?)]))


(defn wrap-auth
  [handler]
  (fn
    ;; sync
    ([request]
     (if (authenticated? request)
       (handler request)))

    ;; async
    ([request respond raise]
     (if (authenticated? request)
       (respond handler request raise)
       (respond {:status 401})))))
