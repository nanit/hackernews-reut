(ns app.db-test
  (:require [clojure.test :refer :all]
            [app.db.api :refer :all]))

(deftest test-db

  (testing "users"
    (let [username "reut"]
      (delete-db)
      (add-user username)
      (let [response (get-user-by-name username)]
        (is (= (:name response) username)))))

  (testing "posts"
    (let [username "reut"
          post-text "blah blah blah"]
      (delete-db)
      (let [user (add-user username)
            user-id (:id user)
            post (add-post user-id post-text)]
        (is (= (:author_id post) user-id))
        (is (= (:text post) post-text)))))

  (testing "votes"
    (let [username "reut"
          post-text "blah blah blah"]
      (delete-db)
      (let [user (add-user username)
            user-id (:id user)
            post (add-post user-id post-text)
            post-id (:id post)
            vote (add-vote user-id post-id)]
        (is (= (:voter_id vote) user-id))
        (is (= (:post_id vote) post-id))))))
