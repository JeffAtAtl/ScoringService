(ns scoring-service.ring
  (:use scoring-service.score)
  (:require [reitit.ring :as ring]
            [reitit.http :as http]
            [reitit.coercion.spec]
            [reitit.swagger :as swagger]
            [reitit.swagger-ui :as swagger-ui]
            [reitit.http.coercion :as coercion]
            [reitit.dev.pretty :as pretty]
            [reitit.interceptor.sieppari :as sieppari]
            [reitit.http.interceptors.parameters :as parameters]
            [reitit.http.interceptors.muuntaja :as muuntaja]
            [reitit.http.interceptors.exception :as exception]
            [reitit.http.interceptors.multipart :as multipart]
            [ring.adapter.jetty :as jetty]
            [muuntaja.core :as m]
            [clojure.java.io :as io]
            [clojure.spec.alpha :as s]
            [spec-tools.core :as st]))

(s/def ::login string?)
(s/def ::score number?)
(s/def ::map-item (s/keys :req-un [::login ::score]))
(s/def ::result (s/coll-of ::map-item))

(def scoring-service-handler
  (http/ring-handler
    (http/router
      [["/swagger.json"
        {:get {:no-doc true
               :swagger {:info {:title "scoring-service-api"
                                :description "Scoring Service with reitit-http"}}
               :handler (swagger/create-swagger-handler)}}]

       ["/api"
        {:swagger {:tags ["scores"]}}

        ["/scores"
         {:get {:summary "get all scores"
                :parameters {:query {:limit number?}}
                :responses {200 {:body {:result ::result}}}
                :handler (fn [{{{:keys [limit]} :query} :parameters}]
                           {:status 200
                            :body {:result (scores limit)}})}}]

        ["/score"
         {:get {:summary "get score for specific github login."
                :parameters {:query {:login string?}}
                :responses {200 {:body {:result ::map-item}}}
                :handler (fn [{{{:keys [login]} :query} :parameters}]
                           {:status 200
                            :body {:result (score login)}})}}]]]
      ;; router data effecting all routes
      {:exception pretty/exception
       :data {:coercion reitit.coercion.spec/coercion
              :muuntaja m/instance
              :interceptors [;; swagger feature
                             swagger/swagger-feature
                             ;; query-params & form-params
                             (parameters/parameters-interceptor)
                             ;; content-negotiation
                             (muuntaja/format-negotiate-interceptor)
                             ;; encoding response body
                             (muuntaja/format-response-interceptor)
                             ;; exception handling
                             (exception/exception-interceptor)
                             ;; decoding request body
                             (muuntaja/format-request-interceptor)
                             ;; coercing response bodys
                             (coercion/coerce-response-interceptor)
                             ;; coercing request parameters
                             (coercion/coerce-request-interceptor)
                             ;; multipart
                             (multipart/multipart-interceptor)]}})
    (ring/routes
      (swagger-ui/create-swagger-ui-handler
        {:path "/"
         :config {:validatorUrl nil
                  :operationsSorter "alpha"}})
      (ring/create-default-handler))
    {:executor sieppari/executor}))