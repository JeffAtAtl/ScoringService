(ns scoring-service.ring
  (:require [reitit.core :as r])
  (:require [reitit.ring :as ring])
  (:require [reitit.coercion.spec])
  (:require [reitit.ring.coercion :as rrc])
  (:require [reitit.swagger :as swagger])
  (:require [reitit.swagger-ui :as swagger-ui]))

(def scoring-service-handler
  (ring/ring-handler
    (ring/router
      [["/api"
        ["/ping" {:get (constantly {:status 200, :body "ping"})}]
        ["/pong" {:post (constantly {:status 200, :body "pong"})}]
        ["/math" {:get {:parameters {:query {:x int?, :y int?}}
                        :responses {200 {:body {:total pos-int?}}}
                        :handler (fn [{{{:keys [x y]} :query} :parameters}]
                                   {:status 200
                                    :body {:total (+ x y)}})}}]]
       ["" {:no-doc true}
        ["/swagger.json" {:get (swagger/create-swagger-handler)}]
        ["/api-docs/*" {:get (swagger-ui/create-swagger-ui-handler)}]]]
      ;; router data effecting all routes
      {:data {:coercion reitit.coercion.spec/coercion
              :middleware [rrc/coerce-exceptions-middleware
                           rrc/coerce-request-middleware
                           rrc/coerce-response-middleware]}})))