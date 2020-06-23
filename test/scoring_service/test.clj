(ns scoring-service.test
  (:use [clojure.test])
  (:use [scoring-service.core]))


(deftest score-event-type-test 
  (testing "Score Event Type"
    (testing "PushEvent"
      (is (= 5 (score-event-type "PushEvent"))))
    (testing "PullRequestReviewCommentEvent"
      (is (= 4 (score-event-type "PullRequestReviewCommentEvent"))))
    (testing "WatchEvent"
      (is (= 3 (score-event-type "WatchEvent"))))
    (testing "CreateEvent"
      (is (= 2 (score-event-type "CreateEvent"))))
    (testing "AnyThingElseEvent"
      (is (= 1 (score-event-type "AnyThingElseEvent"))))))
         
