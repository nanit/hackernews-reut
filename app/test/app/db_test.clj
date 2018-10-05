(ns app.db-test
  (:require [clojure.test :refer :all]
            [app.db.api :refer :all]))

(deftest test-db
  (testing "api"
    ;; TODO: test delete
    (let [username "reut"
          post-text "blah blah blah"]
      (delete-db)
      (let [user (add-user username)
            user-id (:id user)
            post (add-post user-id post-text)
            post-id (:id post)
            vote (add-vote user-id post-id)]
        (is (= (:voter_id vote) user-id))
        (is (= (:post_id vote) post-id))
        (let [generated-vote (get-vote (:id vote))]
          (is (= vote generated-vote)))))))
