(ns app.db-test
  (:require [clojure.test :refer :all]
            [app.db.users :as users]
            [app.db.posts :as posts]
            [app.db.votes :as votes]
            [app.db.helpers :as db-helpers]))

(deftest get-user-by-name
  (testing "user exists"
    (db-helpers/delete-db)
    (let [username "name"
          password "pass"]
      (users/add-user username password)
      (let [user (users/get-user-by-name username)]
        (is (= username (:name user))))))

  (testing "user does not exist"
    (db-helpers/delete-db)
    (let [user (users/get-user-by-name "john doe")]
      (is (= nil user)))))

(deftest sanity
  (testing "api"
    ;; TODO: test delete
    (let [username "reut"
          post-text "blah blah blah"
          password "such secure wow"]
      (db-helpers/delete-db)
      (let [user (users/add-user username password)
            user-id (:id user)
            post (posts/add-post user-id post-text)
            post-id (:id post)
            vote (votes/add-vote user-id post-id)
            vote-id (:id vote)]
        (is (= (:voter_id vote) user-id))
        (is (= (:post_id vote) post-id))
        (let [generated-vote (votes/get-vote vote-id)]
          (is (= vote generated-vote))
          (let [new-post-text "1234"
                new-user-name "not reut"
                new-user-pass "hooomaannnn"]
            (posts/update-post post-id new-post-text)
            (users/update-user user-id new-user-name new-user-pass)
            (let [new-user (users/get-user user-id)
                  new-post (posts/get-post post-id)]
              (is (= new-post-text (:text new-post)))
              (is (= new-user-name (:name new-user))))))))))
