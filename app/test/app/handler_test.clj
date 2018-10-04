(ns app.handler-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [app.handler :refer :all]))

(deftest test-app
  (testing "get post"
    (let [response (app (mock/request :get "/posts/123"))]
      (is (= 200 (:status response)))
      (is (= "get post" (:body response)))))
  (testing "delete post"
    (let [response (app (mock/request :delete "/posts/123"))]
      (is (= 200 (:status response)))
      (is (= "delete post" (:body response)))))
  (testing "top posts"
    (let [response (app (mock/request :get "/posts/top"))]
      (is (= 200 (:status response)))
      (is (= "top posts" (:body response)))))
  (testing "add post"
    (let [response (app (mock/request :put "/posts"))]
      (is (= 200 (:status response)))
      (is (= "add post" (:body response))))))
