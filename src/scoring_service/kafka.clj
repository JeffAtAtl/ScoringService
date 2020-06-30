;; gorilla-repl.fileformat = 1

;; **
;;; # Gorilla REPL
;;; 
;;; Welcome to gorilla :-)
;;; 
;;; Shift + enter evaluates code. Hit alt+g twice in quick succession or click the menu icon (upper-right corner) for more commands ...
;;; 
;;; It's a good habit to run each worksheet in its own namespace: feel free to use the declaration we've provided below if you'd like.
;; **

;; @@
(ns scoring-service.kafka
  (:import (org.apache.kafka.clients.admin AdminClient AdminClientConfig NewTopic)
           (org.apache.kafka.clients.consumer KafkaConsumer)
           (org.apache.kafka.clients.producer KafkaProducer ProducerRecord)
           (org.apache.kafka.common.serialization StringDeserializer StringSerializer)))

;; define atom to count records processed and functions to access
(def records-processed (atom 0))

(defn inc-records-processed
    []
    (swap! records-processed inc))

(defn get-records-processed
    []
    @records-processed)

(defn reset-records-processed
    []
    (reset! records-processed 0))

(defn create-topics!
  "Create the topics from the provided list"
  [bootstrap-server topics partitions replication]
  (let [config {AdminClientConfig/BOOTSTRAP_SERVERS_CONFIG bootstrap-server}
        adminClient (AdminClient/create config)
        new-topics (map #(NewTopic. % partitions replication) topics)]
    (.createTopics adminClient new-topics)))

(defn delete-topics!
  "Delete the topics from the provided list"
  [bootstrap-server topics]
  (let [config {AdminClientConfig/BOOTSTRAP_SERVERS_CONFIG bootstrap-server}
        adminClient (AdminClient/create config)]
    (.deleteTopics adminClient topics)))

(defn list-topics
  "List the topics on this cluster of brokers"
  [bootstrap-server]
  (let [config {AdminClientConfig/BOOTSTRAP_SERVERS_CONFIG bootstrap-server}
        adminClient (AdminClient/create config)]
    (.get (.names (.listTopics adminClient)))))

(defn- build-consumer
  "Create the consumer instance to consume
from the provided kafka topic name"
  ([consumer-topic bootstrap-server group-id]
    (let [consumer-props
            {"bootstrap.servers", bootstrap-server
             "group.id",          group-id
             "key.deserializer",  StringDeserializer
             "value.deserializer", StringDeserializer
             "auto.offset.reset", "earliest"
             "enable.auto.commit", "true"}]
    (doto (KafkaConsumer. consumer-props)
          (.subscribe [consumer-topic]))))
  ([consumer-topic bootstrap-server]
    (build-consumer consumer-topic bootstrap-server "example")))

(defn- build-producer
  "Create the kafka producer to send on messages received"
  [bootstrap-server]
  (let [producer-props {"value.serializer" StringSerializer
                        "key.serializer" StringSerializer
                        "bootstrap.servers" bootstrap-server}]
    (KafkaProducer. producer-props)))

(defn send-producer
  [t k v] 
  (let [producer (build-producer "localhost:9092")]
    (.send producer (ProducerRecord. t k v))))

(defn consume-topic
  [t]
  (let [consumer (build-consumer t "localhost:9092" t)])
    (->> (.poll consumer-1 1000)
         seq
         (map (juxt #(.offset %) 
                    #(.partition %)
                    #(.key %) 
                    #(.value %) 
                    #(java.util.Date. (.timestamp %))))
         (map #(zipmap [:offset :partition :key :val] %)))))

;; This is our processing setup and loop (wrapped by future so it runs in a seperate Thread)
(def consumer-producer-future 
     (future 
         (let [consumer-topic "github-event-topic"
               producer-topic "login-score-topic"
               bootstrap-server "localhost:9092"
               zookeeper-hosts "localhost:2181"
               consumer (build-consumer consumer-topic bootstrap-server)
               producer (build-producer bootstrap-server)]

           (while true
                  (let [records (.poll consumer 100)]
                    (doseq [record records]
                       (inc-records-processed) ;; keep track of how many records have been processed
                       (.send producer (ProducerRecord. producer-topic 
                                                        (.key record)
                                                        (str "Processed Value: " (.value record)))))
                    (.commitAsync consumer))))))
