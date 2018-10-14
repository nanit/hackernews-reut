(ns app.db-test
  (:require [clojure.test :refer :all]
            [app.db.api :as db]))

(deftest get-user-by-name
  (testing "user does not exist"
    (db/delete-db)
    (let [user (db/get-user-by-name "john doe")]
      (is (= nil user)))))

(deftest sanity
  (testing "api"
    ;; TODO: test delete
    (let [username "reut"
          post-text "blah blah blah"
          password "such secure wow"]
      (db/delete-db)
      (let [user (db/add-user username password)
            user-id (:id user)
            post (db/add-post user-id post-text)
            post-id (:id post)
            vote (db/add-vote user-id post-id)]
        (is (= (:voter_id vote) user-id))
        (is (= (:post_id vote) post-id))
        (let [generated-vote (db/get-vote (:id vote))]
          (is (= vote generated-vote)))))))
