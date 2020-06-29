(defproject scoring-service "0.1.0-SNAPSHOT"
  :description "A project for RentPath Scoring Service"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.apache.kafka/kafka-clients "2.0.0"]
                 [irresponsible/tentacles "0.6.6"]
                 [ring/ring-jetty-adapter "1.7.1"]
                 [metosin/reitit "0.5.2"]]
  :main ^:skip-aot scoring-service.core
  :target-path "target/%s"
  :plugins [[org.clojars.benfb/lein-gorilla "0.6.0"]
            [lein-localrepo "0.5.3"]
            [lein-ring "0.12.5"]]
  :ring {:handler scoring-service.ring/scoring-service-handler}
  :profiles {:uberjar {:aot :all}})
