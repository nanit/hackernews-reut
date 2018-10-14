(ns app.middleware
  (:require [app.authentication.tokens :as tokens]))


(defn wrap-auth [handler]
  (fn
    ;; sync
    ([request]
     (if (tokens/authenticated? request)
       (handler request)))

    ;; async
    ([request respond raise]
     (if (tokens/authenticated? request)
       (respond handler request raise)
       (respond {:status 401})))))
