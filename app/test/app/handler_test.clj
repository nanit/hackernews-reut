(ns app.handler-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [app.db.api :as db]
            [clojure.data.json :as json]
            [app.handler :refer :all]))

;;(-> (mock/request :put "/users") (mock/json-body {:name "username"}))

(deftest test-api
  (testing "users"
    (db/delete-db)
    (let [username "reut"
          create-user-request (->
                               (mock/request :put "/users")
                               (mock/json-body {:name username}))
          create-user-response (app create-user-request)]
      (is (= 200 (:status create-user-response)))
      (let [user (json/read-str (:body create-user-response))]
        (is (= username (get user "name")))
        (let [user-id (get user "id")
              post-text "text text text"
              create-post-request (->
                                   (mock/request :put "/posts")
                                   (mock/json-body {:author-id user-id :text post-text}))
              create-post-response (app create-post-request)]
          (is (= 200 (:status create-post-response)))
          (let [post (json/read-str (:body create-post-response))]
            (is (= post-text (get post "text")))
            (let [post-id (get post "id")
                  create-vote-request (->
                                       (mock/request :put "/votes")
                                       (mock/json-body {:voter-id user-id :post-id post-id}))
                  create-vote-response (app create-vote-request)]
              (is (= 200 (:status create-vote-response)))
              (let [vote (json/read-str (:body create-vote-response))]
                (is (= user-id (get vote "voter_id")))
                (is (= post-id (get vote "post_id")))))))))))
