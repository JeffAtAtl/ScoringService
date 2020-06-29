(ns scoring-service.core
  (:require [tentacles.events :as gitapi]))

(defn score-event-type
  "Takes an event type (String) and returns a number score (Number)."
  [t]
  (let [event-score-map {
                         "PushEvent" 5
                         "PullRequestReviewCommentEvent" 4
                         "WatchEvent" 3
                         "CreateEvent" 2}
                        
        score (get event-score-map t 1)]
    score))
    
