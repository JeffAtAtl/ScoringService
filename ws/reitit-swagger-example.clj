;; gorilla-repl.fileformat = 1

;; **
;;; # Reitit Swagger Example
;;; 
;;; Welcome to gorilla :-)
;;; 
;;; Shift + enter evaluates code. Hit alt+g twice in quick succession or click the menu icon (upper-right corner) for more commands ...
;;; 
;;; It's a good habit to run each worksheet in its own namespace: feel free to use the declaration we've provided below if you'd like.
;; **

;; @@
(ns reitit-swagger-example
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
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; @@
(s/def ::x int?)
(s/def ::y int?)
(s/def ::total pos-int?)

(s/def ::seed string?)
(s/def ::results
  (st/spec
    {:spec (s/and int? #(< 0 % 100))
     :description "between 1-100"
     :swagger/default 10
     :reason "invalid number"}))

(s/def ::login string?)
(s/def ::score number?)
(s/def ::map-item (s/keys :req-un [::login ::score]))
(s/def ::result (s/coll-of ::map-item))

;(s/valid? ::result [{:login "test" :score 7} {:login "me" :score 8}])

(s/valid? ::map-item {:login "test" :score 1})
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-unkown'>false</span>","value":"false"}
;; <=

;; @@
(def app
  (http/ring-handler
    (http/router
      [["/swagger.json"
        {:get {:no-doc true
               :swagger {:info {:title "my-api"
                                :description "with reitit-http"}}
               :handler (swagger/create-swagger-handler)}}]

       ["/files"
        {:swagger {:tags ["files"]}}

        ["/upload"
         {:post {:summary "upload a file"
                 :parameters {:multipart {:file multipart/temp-file-part}}
                 :responses {200 {:body {:name string?, :size int?}}}
                 :handler (fn [{{{:keys [file]} :multipart} :parameters}]
                            {:status 200
                             :body {:name (:filename file)
                                    :size (:size file)}})}}]

        ["/download"
         {:get {:summary "downloads a file"
                :swagger {:produces ["image/png"]}
                :handler (fn [_]
                           {:status 200
                            :headers {"Content-Type" "image/png"}
                            :body (io/input-stream
                                    (io/resource "reitit.png"))})}}]]

       ["/math"
        {:swagger {:tags ["math"]}}

        ["/plus"
         {:get {:summary "plus with data-spec query parameters"
                :parameters {:query {:x int?, :y int?}}
                :responses {200 {:body {:total pos-int?}}}
                :handler (fn [{{{:keys [x y]} :query} :parameters}]
                           {:status 200
                            :body {:total (+ x y)}})}
          :post {:summary "plus with data-spec body parameters"
                 :parameters {:body {:x int?, :y int?}}
                 :responses {200 {:body {:total int?}}}
                 :handler (fn [{{{:keys [x y]} :body} :parameters}]
                            {:status 200
                             :body {:total (+ x y)}})}}]

        ["/minus"
         {:get {:summary "minus with clojure.spec query parameters"
                :parameters {:query (s/keys :req-un [::x ::y])}
                :responses {200 {:body (s/keys :req-un [::total])}}
                :handler (fn [{{{:keys [x y]} :query} :parameters}]
                           {:status 200
                            :body {:total (- x y)}})}
          :post {:summary "minus with clojure.spec body parameters"
                 :parameters {:body (s/keys :req-un [::x ::y])}
                 :responses {200 {:body (s/keys :req-un [::total])}}
                 :handler (fn [{{{:keys [x y]} :body} :parameters}]
                            {:status 200
                             :body {:total (- x y)}})}}]]]

      {;:reitit.interceptor/transform dev/print-context-diffs ;; pretty context diffs
       ;;:validate spec/validate ;; enable spec validation for route data
       ;;:reitit.spec/wrap spell/closed ;; strict top-level validation
       :exception pretty/exception
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
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;reitit-swagger-example/app</span>","value":"#'reitit-swagger-example/app"}
;; <=

;; @@
(defn start []
  (jetty/run-jetty #'app {:port 3010, :join? false, :async true})
  (println "server running in port 3010"))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;reitit-swagger-example/start</span>","value":"#'reitit-swagger-example/start"}
;; <=

;; @@
(start)
;; @@
;; ->
;;; server running in port 3010
;;; 
;; <-
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; @@
(def app
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
                :responses {200 {:body {:result ::result}}}
                :handler (fn []
                           {:status 200
                            :body {:result (scores)}})}}]

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
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;reitit-swagger-example/app</span>","value":"#'reitit-swagger-example/app"}
;; <=

;; @@
(defn scores
  []
  [{:login "test" :score 7} {:login "me" :score 8}])
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;reitit-swagger-example/scores</span>","value":"#'reitit-swagger-example/scores"}
;; <=

;; @@
(defn score
  [login]
  {:login login :score 7})
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;reitit-swagger-example/score</span>","value":"#'reitit-swagger-example/score"}
;; <=

;; @@
(app {:request-method :get
      :uri "/api/scores"
     })
;; @@
;; =>
;;; {"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:status</span>","value":":status"},{"type":"html","content":"<span class='clj-long'>500</span>","value":"500"}],"value":"[:status 500]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:body</span>","value":":body"},{"type":"html","content":"<span class='clj-unkown'>#object[java.io.ByteArrayInputStream 0x30771382 &quot;java.io.ByteArrayInputStream@30771382&quot;]</span>","value":"#object[java.io.ByteArrayInputStream 0x30771382 \"java.io.ByteArrayInputStream@30771382\"]"}],"value":"[:body #object[java.io.ByteArrayInputStream 0x30771382 \"java.io.ByteArrayInputStream@30771382\"]]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:headers</span>","value":":headers"},{"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-string'>&quot;Content-Type&quot;</span>","value":"\"Content-Type\""},{"type":"html","content":"<span class='clj-string'>&quot;application/json; charset=utf-8&quot;</span>","value":"\"application/json; charset=utf-8\""}],"value":"[\"Content-Type\" \"application/json; charset=utf-8\"]"}],"value":"{\"Content-Type\" \"application/json; charset=utf-8\"}"}],"value":"[:headers {\"Content-Type\" \"application/json; charset=utf-8\"}]"}],"value":"{:status 500, :body #object[java.io.ByteArrayInputStream 0x30771382 \"java.io.ByteArrayInputStream@30771382\"], :headers {\"Content-Type\" \"application/json; charset=utf-8\"}}"}
;; <=

;; @@
(app {:request-method :get
      :uri "/api/score"
      :query-params {:login "test"}
     })
;; @@
;; =>
;;; {"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:status</span>","value":":status"},{"type":"html","content":"<span class='clj-long'>200</span>","value":"200"}],"value":"[:status 200]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:body</span>","value":":body"},{"type":"html","content":"<span class='clj-unkown'>#object[java.io.ByteArrayInputStream 0x6c17e09d &quot;java.io.ByteArrayInputStream@6c17e09d&quot;]</span>","value":"#object[java.io.ByteArrayInputStream 0x6c17e09d \"java.io.ByteArrayInputStream@6c17e09d\"]"}],"value":"[:body #object[java.io.ByteArrayInputStream 0x6c17e09d \"java.io.ByteArrayInputStream@6c17e09d\"]]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:headers</span>","value":":headers"},{"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-string'>&quot;Content-Type&quot;</span>","value":"\"Content-Type\""},{"type":"html","content":"<span class='clj-string'>&quot;application/json; charset=utf-8&quot;</span>","value":"\"application/json; charset=utf-8\""}],"value":"[\"Content-Type\" \"application/json; charset=utf-8\"]"}],"value":"{\"Content-Type\" \"application/json; charset=utf-8\"}"}],"value":"[:headers {\"Content-Type\" \"application/json; charset=utf-8\"}]"}],"value":"{:status 200, :body #object[java.io.ByteArrayInputStream 0x6c17e09d \"java.io.ByteArrayInputStream@6c17e09d\"], :headers {\"Content-Type\" \"application/json; charset=utf-8\"}}"}
;; <=

;; @@

;; @@
