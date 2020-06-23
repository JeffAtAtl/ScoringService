(defproject scoring-service "0.1.0-SNAPSHOT"
  :description "A project for RentPath Scoring Service"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.apache.kafka/kafka-clients "2.0.0"]
                 [irresponsible/tentacles "0.6.6"]]
  :main ^:skip-aot scoring-service.core
  :target-path "target/%s"
  :plugins [[org.clojars.benfb/lein-gorilla "0.6.0"]
            [lein-localrepo "0.5.3"]]
  :profiles {:uberjar {:aot :all}})
