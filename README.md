# Scoring Service

## Intro

This challenge is designed to allow you to demonstrate your ability to:

- create maintainable software
- construct a service which meets requirements
- evaluate options and make informed decisions
- show off a little bit (write code you're proud of)

## About

In this project we will be computing "score cards" for GitHub users based on a stream of events and providing one or more routes to surface this data.

Events and their importance (represented by a score) are presented here:

| Event Type | Points |
|------------|---|
| PushEvent  | 5 |
| PullRequestReviewCommentEvent | 4 |
| WatchEvent  | 3 |
| CreateEvent | 2 |
| Every other event | 1 |

## Usage (how to run your code)

Fill in this section. Tell us how to start your service, how to query it, and how to run tests. Feel free to use your favorite Clojure libraries.

To test run:

lein test

To run gorilla REPL where you can see my experiments:

lein gorilla :port 8994

Links to see what I did:

http://localhost:8994/worksheet.html?filename=ws/rentpath-scoring-service.clj

http://localhost:8994/worksheet.html?filename=ws/kafka-example.clj

http://localhost:8994/worksheet.html?filename=ws/reitit-example.clj

To run test service and open browser (this will be real service once I'm done):

lein ring server

## Goals

Using the above table for event types and their relative scores:

- Implement a source of events that you use to score mock (or real) users' GitHub activity
- Implement a server that exposes one or more routes that return aggregate scores for those users

These are purposefully open-ended. We want to see that you can put a service together, but more importantly we're interested in seeing where you decide to showcase your expertise.
