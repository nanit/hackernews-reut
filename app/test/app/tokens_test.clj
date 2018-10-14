(ns app.tokens-test
  (:require  [clojure.test :as t]
             [app.authentication.tokens :as tokens]))


(t/deftest is-token-valid?

  (t/testing "valid token"
    (let [token (tokens/generate-token "uid")]
      (t/is (tokens/is-token-valid? token))))

  (t/testing "invalid token"
    (let [token "invalid"]
      (t/is (not (tokens/is-token-valid? token))))))


(t/deftest token-from-request

  (t/testing "tokenized request"
    (let [token "blah"
          request {:headers {"token" token}}]
      (t/is (= token (tokens/token-from-request request)))))

  (t/testing "untokenized reqeust"
    (let [token "blah"
          request {}]
      (t/is (not= token (tokens/token-from-request request))))))


(t/deftest authenticated?

  (t/testing "authenticated"
    (let [token (tokens/generate-token "uid")
          request {:headers {"token" token}}]
      (t/is (tokens/authenticated? request))))

  (t/testing "invalid token - not authenticated"
    (let [request {:headers {"token" "should not exist in redis"}}]
      (t/is (not (tokens/authenticated? request)))))

  (t/testing "no token - not authenticated"
    (let [request {}]
      (t/is (not (tokens/authenticated? request))))))
