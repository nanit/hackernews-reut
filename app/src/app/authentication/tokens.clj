(ns app.authentication.tokens
  (:require [taoensso.carmine :as car :refer (wcar)]))

;; redis connection settings
(def connection-spec {:host "127.0.0.1" :port 6379 :password "password" :db 0})
(def server1-conn {:pool {} :spec connection-spec})
(defmacro wcar* [& body] `(car/wcar server1-conn ~@body))

;; TODO: probably should generate a session using user data, also encrypt/decrypt from redis
(defn new-token []
  (str (java.util.UUID/randomUUID)))

(defn generate-token [user-id]
  ;; FIXME: ttl?
  (let [token (new-token)]
    (wcar* (car/set token user-id))
    token))

(defn user-id-from-token [token]
  (wcar* (car/get token)))

(defn is-token-valid? [token]
  (= 1 (wcar* (car/exists token))))

(defn token-from-request [request]
  (get-in request [:headers "token"]))

(defn authenticated? [token]
  (-> token token-from-request is-token-valid?))
