;; gorilla-repl.fileformat = 1

;; **
;;; # Reitit Example
;;; 
;;; Welcome to gorilla :-)
;;; 
;;; Shift + enter evaluates code. Hit alt+g twice in quick succession or click the menu icon (upper-right corner) for more commands ...
;;; 
;;; It's a good habit to run each worksheet in its own namespace: feel free to use the declaration we've provided below if you'd like.
;; **

;; @@
(ns reitit-example
  (:require [reitit.core :as r])
  (:require [reitit.ring :as ring])
  (:require [reitit.coercion.spec])
  (:require [reitit.ring.coercion :as rrc])
  (:require [reitit.swagger :as swagger])
  (:require [reitit.swagger-ui :as swagger-ui])
  (:use ring.adapter.jetty)
  (:use [clojure.repl]))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; @@
(def router
  (r/router
    [["/api/ping" ::ping]
     ["/api/orders/:id" ::order]]))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;reitit-example/router</span>","value":"#'reitit-example/router"}
;; <=

;; @@
(r/match-by-path router "/api/ping")
;; @@
;; =>
;;; {"type":"list-like","open":"<span class='clj-record'>#reitit.core.Match{</span>","close":"<span class='clj-record'>}</span>","separator":" ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:template</span>","value":":template"},{"type":"html","content":"<span class='clj-string'>&quot;/api/ping&quot;</span>","value":"\"/api/ping\""}],"value":"[:template \"/api/ping\"]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:data</span>","value":":data"},{"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:name</span>","value":":name"},{"type":"html","content":"<span class='clj-keyword'>:reitit-example/ping</span>","value":":reitit-example/ping"}],"value":"[:name :reitit-example/ping]"}],"value":"{:name :reitit-example/ping}"}],"value":"[:data {:name :reitit-example/ping}]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:result</span>","value":":result"},{"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}],"value":"[:result nil]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:path-params</span>","value":":path-params"},{"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[],"value":"{}"}],"value":"[:path-params {}]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:path</span>","value":":path"},{"type":"html","content":"<span class='clj-string'>&quot;/api/ping&quot;</span>","value":"\"/api/ping\""}],"value":"[:path \"/api/ping\"]"}],"value":"#reitit.core.Match{:template \"/api/ping\", :data {:name :reitit-example/ping}, :result nil, :path-params {}, :path \"/api/ping\"}"}
;; <=

;; @@
(r/match-by-name router ::order {:id 2})
;; @@
;; =>
;;; {"type":"list-like","open":"<span class='clj-record'>#reitit.core.Match{</span>","close":"<span class='clj-record'>}</span>","separator":" ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:template</span>","value":":template"},{"type":"html","content":"<span class='clj-string'>&quot;/api/orders/:id&quot;</span>","value":"\"/api/orders/:id\""}],"value":"[:template \"/api/orders/:id\"]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:data</span>","value":":data"},{"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:name</span>","value":":name"},{"type":"html","content":"<span class='clj-keyword'>:reitit-example/order</span>","value":":reitit-example/order"}],"value":"[:name :reitit-example/order]"}],"value":"{:name :reitit-example/order}"}],"value":"[:data {:name :reitit-example/order}]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:result</span>","value":":result"},{"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}],"value":"[:result nil]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:path-params</span>","value":":path-params"},{"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:id</span>","value":":id"},{"type":"html","content":"<span class='clj-string'>&quot;2&quot;</span>","value":"\"2\""}],"value":"[:id \"2\"]"}],"value":"{:id \"2\"}"}],"value":"[:path-params {:id \"2\"}]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:path</span>","value":":path"},{"type":"html","content":"<span class='clj-string'>&quot;/api/orders/2&quot;</span>","value":"\"/api/orders/2\""}],"value":"[:path \"/api/orders/2\"]"}],"value":"#reitit.core.Match{:template \"/api/orders/:id\", :data {:name :reitit-example/order}, :result nil, :path-params {:id \"2\"}, :path \"/api/orders/2\"}"}
;; <=

;; @@
(def app
  (ring/ring-handler
    (ring/router
      ["/api"
       ["/math" {:get {:parameters {:query {:x int?, :y int?}}
                       :responses {200 {:body {:total pos-int?}}}
                       :handler (fn [{{{:keys [x y]} :query} :parameters}]
                                  {:status 200
                                   :body {:total (+ x y)}})}}]]
      ;; router data effecting all routes
      {:data {:coercion reitit.coercion.spec/coercion
              :middleware [rrc/coerce-exceptions-middleware
                           rrc/coerce-request-middleware
                           rrc/coerce-response-middleware]}})))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;reitit-example/app</span>","value":"#'reitit-example/app"}
;; <=

;; @@
(app {:request-method :get
      :uri "/api/math"
      :query-params {:x "1", :y "2"}})
;; @@
;; =>
;;; {"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:status</span>","value":":status"},{"type":"html","content":"<span class='clj-long'>200</span>","value":"200"}],"value":"[:status 200]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:body</span>","value":":body"},{"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:total</span>","value":":total"},{"type":"html","content":"<span class='clj-long'>3</span>","value":"3"}],"value":"[:total 3]"}],"value":"{:total 3}"}],"value":"[:body {:total 3}]"}],"value":"{:status 200, :body {:total 3}}"}
;; <=

;; @@
(app {:request-method :get
      :uri "/api/math"
      :query-params {:x "1", :y "a"}})
;; @@
;; =>
;;; {"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:status</span>","value":":status"},{"type":"html","content":"<span class='clj-long'>400</span>","value":"400"}],"value":"[:status 400]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:body</span>","value":":body"},{"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:spec</span>","value":":spec"},{"type":"html","content":"<span class='clj-string'>&quot;(spec-tools.core/spec {:spec (clojure.spec.alpha/keys :req-un [:spec$13620/x :spec$13620/y]), :type :map, :leaf? false})&quot;</span>","value":"\"(spec-tools.core/spec {:spec (clojure.spec.alpha/keys :req-un [:spec$13620/x :spec$13620/y]), :type :map, :leaf? false})\""}],"value":"[:spec \"(spec-tools.core/spec {:spec (clojure.spec.alpha/keys :req-un [:spec$13620/x :spec$13620/y]), :type :map, :leaf? false})\"]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:problems</span>","value":":problems"},{"type":"list-like","open":"<span class='clj-vector'>[</span>","close":"<span class='clj-vector'>]</span>","separator":" ","items":[{"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:path</span>","value":":path"},{"type":"list-like","open":"<span class='clj-vector'>[</span>","close":"<span class='clj-vector'>]</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:y</span>","value":":y"}],"value":"[:y]"}],"value":"[:path [:y]]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:pred</span>","value":":pred"},{"type":"html","content":"<span class='clj-string'>&quot;clojure.core/int?&quot;</span>","value":"\"clojure.core/int?\""}],"value":"[:pred \"clojure.core/int?\"]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:val</span>","value":":val"},{"type":"html","content":"<span class='clj-string'>&quot;a&quot;</span>","value":"\"a\""}],"value":"[:val \"a\"]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:via</span>","value":":via"},{"type":"list-like","open":"<span class='clj-vector'>[</span>","close":"<span class='clj-vector'>]</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:spec$13620/y</span>","value":":spec$13620/y"}],"value":"[:spec$13620/y]"}],"value":"[:via [:spec$13620/y]]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:in</span>","value":":in"},{"type":"list-like","open":"<span class='clj-vector'>[</span>","close":"<span class='clj-vector'>]</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:y</span>","value":":y"}],"value":"[:y]"}],"value":"[:in [:y]]"}],"value":"{:path [:y], :pred \"clojure.core/int?\", :val \"a\", :via [:spec$13620/y], :in [:y]}"}],"value":"[{:path [:y], :pred \"clojure.core/int?\", :val \"a\", :via [:spec$13620/y], :in [:y]}]"}],"value":"[:problems [{:path [:y], :pred \"clojure.core/int?\", :val \"a\", :via [:spec$13620/y], :in [:y]}]]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:type</span>","value":":type"},{"type":"html","content":"<span class='clj-keyword'>:reitit.coercion/request-coercion</span>","value":":reitit.coercion/request-coercion"}],"value":"[:type :reitit.coercion/request-coercion]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:coercion</span>","value":":coercion"},{"type":"html","content":"<span class='clj-keyword'>:spec</span>","value":":spec"}],"value":"[:coercion :spec]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:value</span>","value":":value"},{"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:x</span>","value":":x"},{"type":"html","content":"<span class='clj-string'>&quot;1&quot;</span>","value":"\"1\""}],"value":"[:x \"1\"]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:y</span>","value":":y"},{"type":"html","content":"<span class='clj-string'>&quot;a&quot;</span>","value":"\"a\""}],"value":"[:y \"a\"]"}],"value":"{:x \"1\", :y \"a\"}"}],"value":"[:value {:x \"1\", :y \"a\"}]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:in</span>","value":":in"},{"type":"list-like","open":"<span class='clj-vector'>[</span>","close":"<span class='clj-vector'>]</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:request</span>","value":":request"},{"type":"html","content":"<span class='clj-keyword'>:query-params</span>","value":":query-params"}],"value":"[:request :query-params]"}],"value":"[:in [:request :query-params]]"}],"value":"{:spec \"(spec-tools.core/spec {:spec (clojure.spec.alpha/keys :req-un [:spec$13620/x :spec$13620/y]), :type :map, :leaf? false})\", :problems [{:path [:y], :pred \"clojure.core/int?\", :val \"a\", :via [:spec$13620/y], :in [:y]}], :type :reitit.coercion/request-coercion, :coercion :spec, :value {:x \"1\", :y \"a\"}, :in [:request :query-params]}"}],"value":"[:body {:spec \"(spec-tools.core/spec {:spec (clojure.spec.alpha/keys :req-un [:spec$13620/x :spec$13620/y]), :type :map, :leaf? false})\", :problems [{:path [:y], :pred \"clojure.core/int?\", :val \"a\", :via [:spec$13620/y], :in [:y]}], :type :reitit.coercion/request-coercion, :coercion :spec, :value {:x \"1\", :y \"a\"}, :in [:request :query-params]}]"}],"value":"{:status 400, :body {:spec \"(spec-tools.core/spec {:spec (clojure.spec.alpha/keys :req-un [:spec$13620/x :spec$13620/y]), :type :map, :leaf? false})\", :problems [{:path [:y], :pred \"clojure.core/int?\", :val \"a\", :via [:spec$13620/y], :in [:y]}], :type :reitit.coercion/request-coercion, :coercion :spec, :value {:x \"1\", :y \"a\"}, :in [:request :query-params]}}"}
;; <=

;; @@
(defonce server (run-jetty #'app {:port 3000 :join? false}))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;reitit-example/server</span>","value":"#'reitit-example/server"}
;; <=

;; @@
(.stop server)
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; @@
(.start server)
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; @@
(def app
  (ring/ring-handler
    (ring/router
      [["/api"
        ["/ping" {:get (constantly {:status 200, :body "ping"})}]
        ["/pong" {:post (constantly {:status 200, :body "pong"})}]]
       ["" {:no-doc true}
        ["/swagger.json" {:get (swagger/create-swagger-handler)}]
        ["/api-docs/*" {:get (swagger-ui/create-swagger-ui-handler)}]]])))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;reitit-example/app</span>","value":"#'reitit-example/app"}
;; <=

;; @@
(app {:request-method :get
      :uri "/api/ping"})
;; @@
;; =>
;;; {"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:status</span>","value":":status"},{"type":"html","content":"<span class='clj-long'>200</span>","value":"200"}],"value":"[:status 200]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:body</span>","value":":body"},{"type":"html","content":"<span class='clj-string'>&quot;ping&quot;</span>","value":"\"ping\""}],"value":"[:body \"ping\"]"}],"value":"{:status 200, :body \"ping\"}"}
;; <=

;; @@
(app {:request-method :post
      :uri "/api/pong"})
;; @@
;; =>
;;; {"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:status</span>","value":":status"},{"type":"html","content":"<span class='clj-long'>200</span>","value":"200"}],"value":"[:status 200]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:body</span>","value":":body"},{"type":"html","content":"<span class='clj-string'>&quot;pong&quot;</span>","value":"\"pong\""}],"value":"[:body \"pong\"]"}],"value":"{:status 200, :body \"pong\"}"}
;; <=

;; @@
(app {:request-method :get
      :uri "/swagger.json"})
;; @@
;; =>
;;; {"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:status</span>","value":":status"},{"type":"html","content":"<span class='clj-long'>200</span>","value":"200"}],"value":"[:status 200]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:body</span>","value":":body"},{"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:swagger</span>","value":":swagger"},{"type":"html","content":"<span class='clj-string'>&quot;2.0&quot;</span>","value":"\"2.0\""}],"value":"[:swagger \"2.0\"]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:x-id</span>","value":":x-id"},{"type":"list-like","open":"<span class='clj-set'>#{</span>","close":"<span class='clj-set'>}</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:reitit.swagger/default</span>","value":":reitit.swagger/default"}],"value":"#{:reitit.swagger/default}"}],"value":"[:x-id #{:reitit.swagger/default}]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:paths</span>","value":":paths"},{"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-string'>&quot;/api/ping&quot;</span>","value":"\"/api/ping\""},{"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:get</span>","value":":get"},{"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:responses</span>","value":":responses"},{"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:default</span>","value":":default"},{"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:description</span>","value":":description"},{"type":"html","content":"<span class='clj-string'>&quot;&quot;</span>","value":"\"\""}],"value":"[:description \"\"]"}],"value":"{:description \"\"}"}],"value":"[:default {:description \"\"}]"}],"value":"{:default {:description \"\"}}"}],"value":"[:responses {:default {:description \"\"}}]"}],"value":"{:responses {:default {:description \"\"}}}"}],"value":"[:get {:responses {:default {:description \"\"}}}]"}],"value":"{:get {:responses {:default {:description \"\"}}}}"}],"value":"[\"/api/ping\" {:get {:responses {:default {:description \"\"}}}}]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-string'>&quot;/api/pong&quot;</span>","value":"\"/api/pong\""},{"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:post</span>","value":":post"},{"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:responses</span>","value":":responses"},{"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:default</span>","value":":default"},{"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:description</span>","value":":description"},{"type":"html","content":"<span class='clj-string'>&quot;&quot;</span>","value":"\"\""}],"value":"[:description \"\"]"}],"value":"{:description \"\"}"}],"value":"[:default {:description \"\"}]"}],"value":"{:default {:description \"\"}}"}],"value":"[:responses {:default {:description \"\"}}]"}],"value":"{:responses {:default {:description \"\"}}}"}],"value":"[:post {:responses {:default {:description \"\"}}}]"}],"value":"{:post {:responses {:default {:description \"\"}}}}"}],"value":"[\"/api/pong\" {:post {:responses {:default {:description \"\"}}}}]"}],"value":"{\"/api/ping\" {:get {:responses {:default {:description \"\"}}}}, \"/api/pong\" {:post {:responses {:default {:description \"\"}}}}}"}],"value":"[:paths {\"/api/ping\" {:get {:responses {:default {:description \"\"}}}}, \"/api/pong\" {:post {:responses {:default {:description \"\"}}}}}]"}],"value":"{:swagger \"2.0\", :x-id #{:reitit.swagger/default}, :paths {\"/api/ping\" {:get {:responses {:default {:description \"\"}}}}, \"/api/pong\" {:post {:responses {:default {:description \"\"}}}}}}"}],"value":"[:body {:swagger \"2.0\", :x-id #{:reitit.swagger/default}, :paths {\"/api/ping\" {:get {:responses {:default {:description \"\"}}}}, \"/api/pong\" {:post {:responses {:default {:description \"\"}}}}}}]"}],"value":"{:status 200, :body {:swagger \"2.0\", :x-id #{:reitit.swagger/default}, :paths {\"/api/ping\" {:get {:responses {:default {:description \"\"}}}}, \"/api/pong\" {:post {:responses {:default {:description \"\"}}}}}}}"}
;; <=

;; @@
(app {:request-method :get
      :uri "/api-docs/"})
;; @@
;; =>
;;; {"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:status</span>","value":":status"},{"type":"html","content":"<span class='clj-long'>302</span>","value":"302"}],"value":"[:status 302]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:headers</span>","value":":headers"},{"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-string'>&quot;Location&quot;</span>","value":"\"Location\""},{"type":"html","content":"<span class='clj-string'>&quot;/api-docs/index.html&quot;</span>","value":"\"/api-docs/index.html\""}],"value":"[\"Location\" \"/api-docs/index.html\"]"}],"value":"{\"Location\" \"/api-docs/index.html\"}"}],"value":"[:headers {\"Location\" \"/api-docs/index.html\"}]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:body</span>","value":":body"},{"type":"html","content":"<span class='clj-string'>&quot;&quot;</span>","value":"\"\""}],"value":"[:body \"\"]"}],"value":"{:status 302, :headers {\"Location\" \"/api-docs/index.html\"}, :body \"\"}"}
;; <=

;; @@
(app {:request-method :get
      :uri "/api-docs/index.html"})
;; @@
;; =>
;;; {"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:status</span>","value":":status"},{"type":"html","content":"<span class='clj-long'>200</span>","value":"200"}],"value":"[:status 200]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:headers</span>","value":":headers"},{"type":"list-like","open":"<span class='clj-map'>{</span>","close":"<span class='clj-map'>}</span>","separator":", ","items":[{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-string'>&quot;Content-Length&quot;</span>","value":"\"Content-Length\""},{"type":"html","content":"<span class='clj-string'>&quot;1496&quot;</span>","value":"\"1496\""}],"value":"[\"Content-Length\" \"1496\"]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-string'>&quot;Last-Modified&quot;</span>","value":"\"Last-Modified\""},{"type":"html","content":"<span class='clj-string'>&quot;Sun, 28 Jun 2020 01:26:58 GMT&quot;</span>","value":"\"Sun, 28 Jun 2020 01:26:58 GMT\""}],"value":"[\"Last-Modified\" \"Sun, 28 Jun 2020 01:26:58 GMT\"]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-string'>&quot;Content-Type&quot;</span>","value":"\"Content-Type\""},{"type":"html","content":"<span class='clj-string'>&quot;text/html&quot;</span>","value":"\"text/html\""}],"value":"[\"Content-Type\" \"text/html\"]"}],"value":"{\"Content-Length\" \"1496\", \"Last-Modified\" \"Sun, 28 Jun 2020 01:26:58 GMT\", \"Content-Type\" \"text/html\"}"}],"value":"[:headers {\"Content-Length\" \"1496\", \"Last-Modified\" \"Sun, 28 Jun 2020 01:26:58 GMT\", \"Content-Type\" \"text/html\"}]"},{"type":"list-like","open":"","close":"","separator":" ","items":[{"type":"html","content":"<span class='clj-keyword'>:body</span>","value":":body"},{"type":"html","content":"<span class='clj-unkown'>#object[sun.net.www.protocol.jar.JarURLConnection$JarURLInputStream 0x7e2a5b68 &quot;sun.net.www.protocol.jar.JarURLConnection$JarURLInputStream@7e2a5b68&quot;]</span>","value":"#object[sun.net.www.protocol.jar.JarURLConnection$JarURLInputStream 0x7e2a5b68 \"sun.net.www.protocol.jar.JarURLConnection$JarURLInputStream@7e2a5b68\"]"}],"value":"[:body #object[sun.net.www.protocol.jar.JarURLConnection$JarURLInputStream 0x7e2a5b68 \"sun.net.www.protocol.jar.JarURLConnection$JarURLInputStream@7e2a5b68\"]]"}],"value":"{:status 200, :headers {\"Content-Length\" \"1496\", \"Last-Modified\" \"Sun, 28 Jun 2020 01:26:58 GMT\", \"Content-Type\" \"text/html\"}, :body #object[sun.net.www.protocol.jar.JarURLConnection$JarURLInputStream 0x7e2a5b68 \"sun.net.www.protocol.jar.JarURLConnection$JarURLInputStream@7e2a5b68\"]}"}
;; <=

;; @@

;; @@
