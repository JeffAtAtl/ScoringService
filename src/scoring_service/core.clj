(ns scoring-service.core
  (:require [tentacles.events :as gitapi])
  (:use ring.adapter.jetty
        [ring.middleware.content-type]
        [ring.middleware.cookies]
        [ring.middleware.params]
        [ring.middleware.session]
        [ring.middleware.session.cookie]
        [ring.util.response]))

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

;; various test handlers

;; Handler that just echos back the string "Hello World"
(defn simple-handler [request]
  {:status 200
   :headers {"Content-Type" "text/plain"}
   :body "Hello World"})

;; Handler that echos back the clients IP Address
;; This demonstrates building responses properly, and extracting values from the request
(defn check-ip-handler [request]
    (content-type
        (response (:remote-addr request))
        "text/plain"))

;; Handler that echos back the incoming parameter "input"
;; This demonstrates middleware chaining and accessing parameters
(def echo-handler
  (-> (fn [{params :params}]
        (content-type
          (response (get params "input"))
          "text/plain"))
      (wrap-params {:encoding "UTF-8"})
  ))

;; Handler that keeps track of how many times each session has accessed the service
;; This demonstrates cookies and sessions
(def request-count-handler
  (-> (fn [{session :session}]
        (let [count (:count session 0)
              session (assoc session :count (inc count))]
          (-> (response (str "You accessed this page " count " times."))
              (assoc :session session))))
      wrap-cookies
      (wrap-session {:cookie-attrs {:max-age 3600}})
  ))

;; Run the provided handler on port 3000
(defn run
  [h]
  (run-jetty h {:port 3000}))
    
