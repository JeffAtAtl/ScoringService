(defproject scoring-service "0.1.0-SNAPSHOT"
  :description "A project for RentPath Scoring Service"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.apache.kafka/kafka-clients "2.0.0"]
                 [irresponsible/tentacles "0.6.6"]
                 [metosin/ring-swagger "0.26.2"]
                 [ring/ring-jetty-adapter "1.7.1"]
                 [ring/ring-devel "1.7.1"]]
  :main ^:skip-aot scoring-service.core
  :target-path "target/%s"
  :plugins [[org.clojars.benfb/lein-gorilla "0.6.0"]
            [lein-localrepo "0.5.3"]
            [lein-ring "0.12.5"]]
  :ring {:handler scoring-service.core/simple-handler}
  :profiles {:uberjar {:aot :all}})
