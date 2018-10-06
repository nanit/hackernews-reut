(ns app.handler-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [app.db.api :as db]
            [clojure.data.json :as json]
            [app.handler :refer :all]))


;; users
(defn create-user [user-name]
  (let [response (app (->
                       (mock/request :put "/users")
                       (mock/json-body {:name user-name})))]
    (json/read-str
     (:body response))))

(defn get-user-by-id [id]
  (let [response (app (mock/request :get (str "/users/" (str id))))]
    (json/read-str
     (:body response))))


;; posts
(defn create-post [user-id post-text]
  (let [response (app (->
                       (mock/request :put "/posts")
                       (mock/json-body {:author-id user-id :text post-text})))]
    (json/read-str
     (:body response))))

(defn get-post-by-id [id]
  (let [response (app (mock/request :get (str "/posts/" (str id))))]
    (json/read-str
     (:body response))))



;; votes
(defn create-vote [user-id, post-id]
  (let [response (app (->
                       (mock/request :put "/votes")
                       (mock/json-body {:voter-id user-id :post-id post-id})))]
    (json/read-str
     (:body response))))

(defn get-vote-by-id [id]
  (let [response (app (mock/request :get (str "/votes/" (str id))))]
    (json/read-str
     (:body response))))



(deftest test-api
  (testing "test all endpoints sanity"
    (db/delete-db)
    (let [user-name "reut"
          user (create-user user-name)]
      (is (= user-name (get user "name")))
      (let [user-id (get user "id")
            post-text "text text text"
            post (create-post user-id post-text)]
        (is (= post-text (get post "text")))
        (let [post-id (get post "id")
              vote (create-vote user-id post-id)]
          (is (= user-id (get vote "voter_id")))
          (is (= post-id (get vote "post_id")))))))


  (testing "get data"
    (db/delete-db)
    (let [user-name "reut"
          post-text "text text text"
          user (create-user user-name)
          user-id (get user "id")
          post (create-post user-id post-text)
          post-id (get post "id")
          vote (create-vote user-id post-id)
          vote-id (get vote "id")]
      (let [generated-user (get-user-by-id user-id)
            generated-post (get-post-by-id post-id)
            generated-vote (get-vote-by-id vote-id)]

      ;;user
      (is (= user-id (get generated-user "id")))
      (is (= user-name (get generated-user "name")))

      ;; post
      (is (= post-id (get generated-post "id")))
      (is (= user-id (get generated-post "author_id")))

      ;; vote
      (is (= vote-id (get generated-vote "id")))
      (is (= user-id (get generated-vote "voter_id")))
      (is (= post-id (get generated-vote "post_id")))))))
