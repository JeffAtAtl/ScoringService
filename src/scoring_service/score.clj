(ns scoring-service.score
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

(defn scores
  "Makes a direct api call to GitHub to get the last 100 public events and scores them by login. Sorts by score in reverse order and imits results."
  [limit]
  (->> (gitapi/events {:per-page 100})
       (map (juxt (comp :login :actor) 
                  (comp score-event-type :type)))
       (group-by first)
       (map (juxt key
                  (comp #(reduce + %) #(map second %) val)))
       (sort-by second)
       reverse
       (map #(zipmap [:login :score] %))
       (take limit)
       ))

(defn score
  "Makes a direct api call to GitHub to get performed public events for a specific login."
  [login]
  (let [score (->> (gitapi/performed-events login)
                   (map (comp score-event-type :type))
                   (reduce +)
                   )]
    {:login login :score score}
    ))

