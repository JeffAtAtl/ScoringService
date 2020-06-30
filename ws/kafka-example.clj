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
(ns kafka-example
  (:use [gorilla-repl table latex])
  (:import (org.apache.kafka.clients.admin AdminClient AdminClientConfig NewTopic)
           (org.apache.kafka.clients.consumer KafkaConsumer)
           (org.apache.kafka.clients.producer KafkaProducer ProducerRecord)
           (org.apache.kafka.common.serialization StringDeserializer StringSerializer)))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; @@
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
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;kafka-example/reset-records-processed</span>","value":"#'kafka-example/reset-records-processed"}
;; <=

;; @@
(defn create-topics!
  "Create the topics from the provided list"
  [bootstrap-server topics partitions replication]
  (let [config {AdminClientConfig/BOOTSTRAP_SERVERS_CONFIG bootstrap-server}
        adminClient (AdminClient/create config)
        new-topics (map #(NewTopic. % partitions replication) topics)]
    (.createTopics adminClient new-topics)))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;kafka-example/create-topics!</span>","value":"#'kafka-example/create-topics!"}
;; <=

;; @@
(defn delete-topics!
  "Delete the topics from the provided list"
  [bootstrap-server topics]
  (let [config {AdminClientConfig/BOOTSTRAP_SERVERS_CONFIG bootstrap-server}
        adminClient (AdminClient/create config)]
    (.deleteTopics adminClient topics)))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;kafka-example/delete-topics!</span>","value":"#'kafka-example/delete-topics!"}
;; <=

;; @@
(defn list-topics
  "List the topics on this cluster of brokers"
  [bootstrap-server]
  (let [config {AdminClientConfig/BOOTSTRAP_SERVERS_CONFIG bootstrap-server}
        adminClient (AdminClient/create config)]
    (.get (.names (.listTopics adminClient)))))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;kafka-example/list-topics</span>","value":"#'kafka-example/list-topics"}
;; <=

;; @@
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
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;kafka-example/build-consumer</span>","value":"#'kafka-example/build-consumer"}
;; <=

;; @@
(defn- build-producer
  "Create the kafka producer to send on messages received"
  [bootstrap-server]
  (let [producer-props {"value.serializer" StringSerializer
                        "key.serializer" StringSerializer
                        "bootstrap.servers" bootstrap-server}]
    (KafkaProducer. producer-props)))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;kafka-example/build-producer</span>","value":"#'kafka-example/build-producer"}
;; <=

;; @@
(list-topics "localhost:9092")
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-unkown'>#{&quot;test-consumer-topic&quot; &quot;logs_broker&quot; &quot;connect-configs&quot; &quot;telecom_italia_data&quot; &quot;tst-produced-topic&quot; &quot;test-1&quot; &quot;coyote-test-avro&quot; &quot;coyote-test-binary&quot; &quot;_schemas&quot; &quot;test-produced-topic&quot; &quot;connect-offsets&quot; &quot;test-produced-topic-2&quot; &quot;example-produced-topic-2&quot; &quot;sea_vessel_position_reports&quot; &quot;nyc_yellow_taxi_trip_data&quot; &quot;example-produced-topic&quot; &quot;connect-statuses&quot; &quot;backblaze_smart&quot; &quot;telecom_italia_grid&quot; &quot;tst-produced-topic-2&quot; &quot;tst-consumer-topic&quot; &quot;example-consumer-topic&quot; &quot;coyote-test-json&quot;}</span>","value":"#{\"test-consumer-topic\" \"logs_broker\" \"connect-configs\" \"telecom_italia_data\" \"tst-produced-topic\" \"test-1\" \"coyote-test-avro\" \"coyote-test-binary\" \"_schemas\" \"test-produced-topic\" \"connect-offsets\" \"test-produced-topic-2\" \"example-produced-topic-2\" \"sea_vessel_position_reports\" \"nyc_yellow_taxi_trip_data\" \"example-produced-topic\" \"connect-statuses\" \"backblaze_smart\" \"telecom_italia_grid\" \"tst-produced-topic-2\" \"tst-consumer-topic\" \"example-consumer-topic\" \"coyote-test-json\"}"}
;; <=

;; @@
(defn send-producer-1 
  [t k w] 
  (let [producer-1 (build-producer "localhost:9092")]
    (.send producer-1 (ProducerRecord. t k w))))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;kafka-example/send-producer-1</span>","value":"#'kafka-example/send-producer-1"}
;; <=

;; @@
(dotimes [n 5] (send-producer-1 "test-1" (str n) (str "record-" n)))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; @@
(def consumer-1 (build-consumer "test-1" "localhost:9092" "test-1"))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;kafka-example/consumer-1</span>","value":"#'kafka-example/consumer-1"}
;; <=

;; @@
(->> (.poll consumer-1 1000)
     seq
     (map (juxt #(.offset %) 
                #(.partition %)
                #(.key %) 
                #(.value %) 
                #(java.util.Date. (.timestamp %))))
     (concat (list (list "offset" "partition" "key" "value" "timestamp")))
     table-view
)
;; @@
;; =>
;;; {"type":"list-like","open":"<center><table>","close":"</table></center>","separator":"\n","items":[{"type":"list-like","open":"<tr><td>","close":"</td></tr>","separator":"</td><td>","items":[{"type":"html","content":"<span class='clj-string'>&quot;offset&quot;</span>","value":"\"offset\""},{"type":"html","content":"<span class='clj-string'>&quot;partition&quot;</span>","value":"\"partition\""},{"type":"html","content":"<span class='clj-string'>&quot;key&quot;</span>","value":"\"key\""},{"type":"html","content":"<span class='clj-string'>&quot;value&quot;</span>","value":"\"value\""},{"type":"html","content":"<span class='clj-string'>&quot;timestamp&quot;</span>","value":"\"timestamp\""}],"value":"(\"offset\" \"partition\" \"key\" \"value\" \"timestamp\")"},{"type":"list-like","open":"<tr><td>","close":"</td></tr>","separator":"</td><td>","items":[{"type":"html","content":"<span class='clj-long'>45</span>","value":"45"},{"type":"html","content":"<span class='clj-unkown'>0</span>","value":"0"},{"type":"html","content":"<span class='clj-string'>&quot;0&quot;</span>","value":"\"0\""},{"type":"html","content":"<span class='clj-string'>&quot;record-0&quot;</span>","value":"\"record-0\""},{"type":"html","content":"<span class='clj-unkown'>#inst &quot;2020-06-29T22:04:02.331-00:00&quot;</span>","value":"#inst \"2020-06-29T22:04:02.331-00:00\""}],"value":"[45 0 \"0\" \"record-0\" #inst \"2020-06-29T22:04:02.331-00:00\"]"},{"type":"list-like","open":"<tr><td>","close":"</td></tr>","separator":"</td><td>","items":[{"type":"html","content":"<span class='clj-long'>46</span>","value":"46"},{"type":"html","content":"<span class='clj-unkown'>0</span>","value":"0"},{"type":"html","content":"<span class='clj-string'>&quot;1&quot;</span>","value":"\"1\""},{"type":"html","content":"<span class='clj-string'>&quot;record-1&quot;</span>","value":"\"record-1\""},{"type":"html","content":"<span class='clj-unkown'>#inst &quot;2020-06-29T22:04:02.343-00:00&quot;</span>","value":"#inst \"2020-06-29T22:04:02.343-00:00\""}],"value":"[46 0 \"1\" \"record-1\" #inst \"2020-06-29T22:04:02.343-00:00\"]"},{"type":"list-like","open":"<tr><td>","close":"</td></tr>","separator":"</td><td>","items":[{"type":"html","content":"<span class='clj-long'>47</span>","value":"47"},{"type":"html","content":"<span class='clj-unkown'>0</span>","value":"0"},{"type":"html","content":"<span class='clj-string'>&quot;2&quot;</span>","value":"\"2\""},{"type":"html","content":"<span class='clj-string'>&quot;record-2&quot;</span>","value":"\"record-2\""},{"type":"html","content":"<span class='clj-unkown'>#inst &quot;2020-06-29T22:04:02.344-00:00&quot;</span>","value":"#inst \"2020-06-29T22:04:02.344-00:00\""}],"value":"[47 0 \"2\" \"record-2\" #inst \"2020-06-29T22:04:02.344-00:00\"]"},{"type":"list-like","open":"<tr><td>","close":"</td></tr>","separator":"</td><td>","items":[{"type":"html","content":"<span class='clj-long'>48</span>","value":"48"},{"type":"html","content":"<span class='clj-unkown'>0</span>","value":"0"},{"type":"html","content":"<span class='clj-string'>&quot;3&quot;</span>","value":"\"3\""},{"type":"html","content":"<span class='clj-string'>&quot;record-3&quot;</span>","value":"\"record-3\""},{"type":"html","content":"<span class='clj-unkown'>#inst &quot;2020-06-29T22:04:02.345-00:00&quot;</span>","value":"#inst \"2020-06-29T22:04:02.345-00:00\""}],"value":"[48 0 \"3\" \"record-3\" #inst \"2020-06-29T22:04:02.345-00:00\"]"},{"type":"list-like","open":"<tr><td>","close":"</td></tr>","separator":"</td><td>","items":[{"type":"html","content":"<span class='clj-long'>49</span>","value":"49"},{"type":"html","content":"<span class='clj-unkown'>0</span>","value":"0"},{"type":"html","content":"<span class='clj-string'>&quot;4&quot;</span>","value":"\"4\""},{"type":"html","content":"<span class='clj-string'>&quot;record-4&quot;</span>","value":"\"record-4\""},{"type":"html","content":"<span class='clj-unkown'>#inst &quot;2020-06-29T22:04:02.345-00:00&quot;</span>","value":"#inst \"2020-06-29T22:04:02.345-00:00\""}],"value":"[49 0 \"4\" \"record-4\" #inst \"2020-06-29T22:04:02.345-00:00\"]"},{"type":"list-like","open":"<tr><td>","close":"</td></tr>","separator":"</td><td>","items":[{"type":"html","content":"<span class='clj-long'>50</span>","value":"50"},{"type":"html","content":"<span class='clj-unkown'>0</span>","value":"0"},{"type":"html","content":"<span class='clj-string'>&quot;0&quot;</span>","value":"\"0\""},{"type":"html","content":"<span class='clj-string'>&quot;record-0&quot;</span>","value":"\"record-0\""},{"type":"html","content":"<span class='clj-unkown'>#inst &quot;2020-06-29T22:05:55.559-00:00&quot;</span>","value":"#inst \"2020-06-29T22:05:55.559-00:00\""}],"value":"[50 0 \"0\" \"record-0\" #inst \"2020-06-29T22:05:55.559-00:00\"]"},{"type":"list-like","open":"<tr><td>","close":"</td></tr>","separator":"</td><td>","items":[{"type":"html","content":"<span class='clj-long'>51</span>","value":"51"},{"type":"html","content":"<span class='clj-unkown'>0</span>","value":"0"},{"type":"html","content":"<span class='clj-string'>&quot;1&quot;</span>","value":"\"1\""},{"type":"html","content":"<span class='clj-string'>&quot;record-1&quot;</span>","value":"\"record-1\""},{"type":"html","content":"<span class='clj-unkown'>#inst &quot;2020-06-29T22:05:55.559-00:00&quot;</span>","value":"#inst \"2020-06-29T22:05:55.559-00:00\""}],"value":"[51 0 \"1\" \"record-1\" #inst \"2020-06-29T22:05:55.559-00:00\"]"},{"type":"list-like","open":"<tr><td>","close":"</td></tr>","separator":"</td><td>","items":[{"type":"html","content":"<span class='clj-long'>52</span>","value":"52"},{"type":"html","content":"<span class='clj-unkown'>0</span>","value":"0"},{"type":"html","content":"<span class='clj-string'>&quot;2&quot;</span>","value":"\"2\""},{"type":"html","content":"<span class='clj-string'>&quot;record-2&quot;</span>","value":"\"record-2\""},{"type":"html","content":"<span class='clj-unkown'>#inst &quot;2020-06-29T22:05:55.560-00:00&quot;</span>","value":"#inst \"2020-06-29T22:05:55.560-00:00\""}],"value":"[52 0 \"2\" \"record-2\" #inst \"2020-06-29T22:05:55.560-00:00\"]"},{"type":"list-like","open":"<tr><td>","close":"</td></tr>","separator":"</td><td>","items":[{"type":"html","content":"<span class='clj-long'>53</span>","value":"53"},{"type":"html","content":"<span class='clj-unkown'>0</span>","value":"0"},{"type":"html","content":"<span class='clj-string'>&quot;3&quot;</span>","value":"\"3\""},{"type":"html","content":"<span class='clj-string'>&quot;record-3&quot;</span>","value":"\"record-3\""},{"type":"html","content":"<span class='clj-unkown'>#inst &quot;2020-06-29T22:05:55.560-00:00&quot;</span>","value":"#inst \"2020-06-29T22:05:55.560-00:00\""}],"value":"[53 0 \"3\" \"record-3\" #inst \"2020-06-29T22:05:55.560-00:00\"]"},{"type":"list-like","open":"<tr><td>","close":"</td></tr>","separator":"</td><td>","items":[{"type":"html","content":"<span class='clj-long'>54</span>","value":"54"},{"type":"html","content":"<span class='clj-unkown'>0</span>","value":"0"},{"type":"html","content":"<span class='clj-string'>&quot;4&quot;</span>","value":"\"4\""},{"type":"html","content":"<span class='clj-string'>&quot;record-4&quot;</span>","value":"\"record-4\""},{"type":"html","content":"<span class='clj-unkown'>#inst &quot;2020-06-29T22:05:55.560-00:00&quot;</span>","value":"#inst \"2020-06-29T22:05:55.560-00:00\""}],"value":"[54 0 \"4\" \"record-4\" #inst \"2020-06-29T22:05:55.560-00:00\"]"},{"type":"list-like","open":"<tr><td>","close":"</td></tr>","separator":"</td><td>","items":[{"type":"html","content":"<span class='clj-long'>55</span>","value":"55"},{"type":"html","content":"<span class='clj-unkown'>0</span>","value":"0"},{"type":"html","content":"<span class='clj-string'>&quot;0&quot;</span>","value":"\"0\""},{"type":"html","content":"<span class='clj-string'>&quot;record-0&quot;</span>","value":"\"record-0\""},{"type":"html","content":"<span class='clj-unkown'>#inst &quot;2020-06-29T22:12:47.285-00:00&quot;</span>","value":"#inst \"2020-06-29T22:12:47.285-00:00\""}],"value":"[55 0 \"0\" \"record-0\" #inst \"2020-06-29T22:12:47.285-00:00\"]"},{"type":"list-like","open":"<tr><td>","close":"</td></tr>","separator":"</td><td>","items":[{"type":"html","content":"<span class='clj-long'>56</span>","value":"56"},{"type":"html","content":"<span class='clj-unkown'>0</span>","value":"0"},{"type":"html","content":"<span class='clj-string'>&quot;1&quot;</span>","value":"\"1\""},{"type":"html","content":"<span class='clj-string'>&quot;record-1&quot;</span>","value":"\"record-1\""},{"type":"html","content":"<span class='clj-unkown'>#inst &quot;2020-06-29T22:12:47.287-00:00&quot;</span>","value":"#inst \"2020-06-29T22:12:47.287-00:00\""}],"value":"[56 0 \"1\" \"record-1\" #inst \"2020-06-29T22:12:47.287-00:00\"]"},{"type":"list-like","open":"<tr><td>","close":"</td></tr>","separator":"</td><td>","items":[{"type":"html","content":"<span class='clj-long'>57</span>","value":"57"},{"type":"html","content":"<span class='clj-unkown'>0</span>","value":"0"},{"type":"html","content":"<span class='clj-string'>&quot;2&quot;</span>","value":"\"2\""},{"type":"html","content":"<span class='clj-string'>&quot;record-2&quot;</span>","value":"\"record-2\""},{"type":"html","content":"<span class='clj-unkown'>#inst &quot;2020-06-29T22:12:47.288-00:00&quot;</span>","value":"#inst \"2020-06-29T22:12:47.288-00:00\""}],"value":"[57 0 \"2\" \"record-2\" #inst \"2020-06-29T22:12:47.288-00:00\"]"},{"type":"list-like","open":"<tr><td>","close":"</td></tr>","separator":"</td><td>","items":[{"type":"html","content":"<span class='clj-long'>58</span>","value":"58"},{"type":"html","content":"<span class='clj-unkown'>0</span>","value":"0"},{"type":"html","content":"<span class='clj-string'>&quot;3&quot;</span>","value":"\"3\""},{"type":"html","content":"<span class='clj-string'>&quot;record-3&quot;</span>","value":"\"record-3\""},{"type":"html","content":"<span class='clj-unkown'>#inst &quot;2020-06-29T22:12:47.289-00:00&quot;</span>","value":"#inst \"2020-06-29T22:12:47.289-00:00\""}],"value":"[58 0 \"3\" \"record-3\" #inst \"2020-06-29T22:12:47.289-00:00\"]"},{"type":"list-like","open":"<tr><td>","close":"</td></tr>","separator":"</td><td>","items":[{"type":"html","content":"<span class='clj-long'>59</span>","value":"59"},{"type":"html","content":"<span class='clj-unkown'>0</span>","value":"0"},{"type":"html","content":"<span class='clj-string'>&quot;4&quot;</span>","value":"\"4\""},{"type":"html","content":"<span class='clj-string'>&quot;record-4&quot;</span>","value":"\"record-4\""},{"type":"html","content":"<span class='clj-unkown'>#inst &quot;2020-06-29T22:12:47.289-00:00&quot;</span>","value":"#inst \"2020-06-29T22:12:47.289-00:00\""}],"value":"[59 0 \"4\" \"record-4\" #inst \"2020-06-29T22:12:47.289-00:00\"]"},{"type":"list-like","open":"<tr><td>","close":"</td></tr>","separator":"</td><td>","items":[{"type":"html","content":"<span class='clj-long'>60</span>","value":"60"},{"type":"html","content":"<span class='clj-unkown'>0</span>","value":"0"},{"type":"html","content":"<span class='clj-string'>&quot;0&quot;</span>","value":"\"0\""},{"type":"html","content":"<span class='clj-string'>&quot;record-0&quot;</span>","value":"\"record-0\""},{"type":"html","content":"<span class='clj-unkown'>#inst &quot;2020-06-29T22:14:43.005-00:00&quot;</span>","value":"#inst \"2020-06-29T22:14:43.005-00:00\""}],"value":"[60 0 \"0\" \"record-0\" #inst \"2020-06-29T22:14:43.005-00:00\"]"},{"type":"list-like","open":"<tr><td>","close":"</td></tr>","separator":"</td><td>","items":[{"type":"html","content":"<span class='clj-long'>61</span>","value":"61"},{"type":"html","content":"<span class='clj-unkown'>0</span>","value":"0"},{"type":"html","content":"<span class='clj-string'>&quot;1&quot;</span>","value":"\"1\""},{"type":"html","content":"<span class='clj-string'>&quot;record-1&quot;</span>","value":"\"record-1\""},{"type":"html","content":"<span class='clj-unkown'>#inst &quot;2020-06-29T22:14:43.006-00:00&quot;</span>","value":"#inst \"2020-06-29T22:14:43.006-00:00\""}],"value":"[61 0 \"1\" \"record-1\" #inst \"2020-06-29T22:14:43.006-00:00\"]"},{"type":"list-like","open":"<tr><td>","close":"</td></tr>","separator":"</td><td>","items":[{"type":"html","content":"<span class='clj-long'>62</span>","value":"62"},{"type":"html","content":"<span class='clj-unkown'>0</span>","value":"0"},{"type":"html","content":"<span class='clj-string'>&quot;2&quot;</span>","value":"\"2\""},{"type":"html","content":"<span class='clj-string'>&quot;record-2&quot;</span>","value":"\"record-2\""},{"type":"html","content":"<span class='clj-unkown'>#inst &quot;2020-06-29T22:14:43.006-00:00&quot;</span>","value":"#inst \"2020-06-29T22:14:43.006-00:00\""}],"value":"[62 0 \"2\" \"record-2\" #inst \"2020-06-29T22:14:43.006-00:00\"]"},{"type":"list-like","open":"<tr><td>","close":"</td></tr>","separator":"</td><td>","items":[{"type":"html","content":"<span class='clj-long'>63</span>","value":"63"},{"type":"html","content":"<span class='clj-unkown'>0</span>","value":"0"},{"type":"html","content":"<span class='clj-string'>&quot;3&quot;</span>","value":"\"3\""},{"type":"html","content":"<span class='clj-string'>&quot;record-3&quot;</span>","value":"\"record-3\""},{"type":"html","content":"<span class='clj-unkown'>#inst &quot;2020-06-29T22:14:43.007-00:00&quot;</span>","value":"#inst \"2020-06-29T22:14:43.007-00:00\""}],"value":"[63 0 \"3\" \"record-3\" #inst \"2020-06-29T22:14:43.007-00:00\"]"},{"type":"list-like","open":"<tr><td>","close":"</td></tr>","separator":"</td><td>","items":[{"type":"html","content":"<span class='clj-long'>64</span>","value":"64"},{"type":"html","content":"<span class='clj-unkown'>0</span>","value":"0"},{"type":"html","content":"<span class='clj-string'>&quot;4&quot;</span>","value":"\"4\""},{"type":"html","content":"<span class='clj-string'>&quot;record-4&quot;</span>","value":"\"record-4\""},{"type":"html","content":"<span class='clj-unkown'>#inst &quot;2020-06-29T22:14:43.008-00:00&quot;</span>","value":"#inst \"2020-06-29T22:14:43.008-00:00\""}],"value":"[64 0 \"4\" \"record-4\" #inst \"2020-06-29T22:14:43.008-00:00\"]"},{"type":"list-like","open":"<tr><td>","close":"</td></tr>","separator":"</td><td>","items":[{"type":"html","content":"<span class='clj-long'>65</span>","value":"65"},{"type":"html","content":"<span class='clj-unkown'>0</span>","value":"0"},{"type":"html","content":"<span class='clj-string'>&quot;0&quot;</span>","value":"\"0\""},{"type":"html","content":"<span class='clj-string'>&quot;record-0&quot;</span>","value":"\"record-0\""},{"type":"html","content":"<span class='clj-unkown'>#inst &quot;2020-06-29T23:00:57.300-00:00&quot;</span>","value":"#inst \"2020-06-29T23:00:57.300-00:00\""}],"value":"[65 0 \"0\" \"record-0\" #inst \"2020-06-29T23:00:57.300-00:00\"]"},{"type":"list-like","open":"<tr><td>","close":"</td></tr>","separator":"</td><td>","items":[{"type":"html","content":"<span class='clj-long'>66</span>","value":"66"},{"type":"html","content":"<span class='clj-unkown'>0</span>","value":"0"},{"type":"html","content":"<span class='clj-string'>&quot;1&quot;</span>","value":"\"1\""},{"type":"html","content":"<span class='clj-string'>&quot;record-1&quot;</span>","value":"\"record-1\""},{"type":"html","content":"<span class='clj-unkown'>#inst &quot;2020-06-29T23:00:57.335-00:00&quot;</span>","value":"#inst \"2020-06-29T23:00:57.335-00:00\""}],"value":"[66 0 \"1\" \"record-1\" #inst \"2020-06-29T23:00:57.335-00:00\"]"},{"type":"list-like","open":"<tr><td>","close":"</td></tr>","separator":"</td><td>","items":[{"type":"html","content":"<span class='clj-long'>67</span>","value":"67"},{"type":"html","content":"<span class='clj-unkown'>0</span>","value":"0"},{"type":"html","content":"<span class='clj-string'>&quot;2&quot;</span>","value":"\"2\""},{"type":"html","content":"<span class='clj-string'>&quot;record-2&quot;</span>","value":"\"record-2\""},{"type":"html","content":"<span class='clj-unkown'>#inst &quot;2020-06-29T23:00:57.362-00:00&quot;</span>","value":"#inst \"2020-06-29T23:00:57.362-00:00\""}],"value":"[67 0 \"2\" \"record-2\" #inst \"2020-06-29T23:00:57.362-00:00\"]"},{"type":"list-like","open":"<tr><td>","close":"</td></tr>","separator":"</td><td>","items":[{"type":"html","content":"<span class='clj-long'>68</span>","value":"68"},{"type":"html","content":"<span class='clj-unkown'>0</span>","value":"0"},{"type":"html","content":"<span class='clj-string'>&quot;3&quot;</span>","value":"\"3\""},{"type":"html","content":"<span class='clj-string'>&quot;record-3&quot;</span>","value":"\"record-3\""},{"type":"html","content":"<span class='clj-unkown'>#inst &quot;2020-06-29T23:00:57.393-00:00&quot;</span>","value":"#inst \"2020-06-29T23:00:57.393-00:00\""}],"value":"[68 0 \"3\" \"record-3\" #inst \"2020-06-29T23:00:57.393-00:00\"]"},{"type":"list-like","open":"<tr><td>","close":"</td></tr>","separator":"</td><td>","items":[{"type":"html","content":"<span class='clj-long'>69</span>","value":"69"},{"type":"html","content":"<span class='clj-unkown'>0</span>","value":"0"},{"type":"html","content":"<span class='clj-string'>&quot;4&quot;</span>","value":"\"4\""},{"type":"html","content":"<span class='clj-string'>&quot;record-4&quot;</span>","value":"\"record-4\""},{"type":"html","content":"<span class='clj-unkown'>#inst &quot;2020-06-29T23:00:57.431-00:00&quot;</span>","value":"#inst \"2020-06-29T23:00:57.431-00:00\""}],"value":"[69 0 \"4\" \"record-4\" #inst \"2020-06-29T23:00:57.431-00:00\"]"}],"value":"#gorilla_repl.table.TableView{:contents ((\"offset\" \"partition\" \"key\" \"value\" \"timestamp\") [45 0 \"0\" \"record-0\" #inst \"2020-06-29T22:04:02.331-00:00\"] [46 0 \"1\" \"record-1\" #inst \"2020-06-29T22:04:02.343-00:00\"] [47 0 \"2\" \"record-2\" #inst \"2020-06-29T22:04:02.344-00:00\"] [48 0 \"3\" \"record-3\" #inst \"2020-06-29T22:04:02.345-00:00\"] [49 0 \"4\" \"record-4\" #inst \"2020-06-29T22:04:02.345-00:00\"] [50 0 \"0\" \"record-0\" #inst \"2020-06-29T22:05:55.559-00:00\"] [51 0 \"1\" \"record-1\" #inst \"2020-06-29T22:05:55.559-00:00\"] [52 0 \"2\" \"record-2\" #inst \"2020-06-29T22:05:55.560-00:00\"] [53 0 \"3\" \"record-3\" #inst \"2020-06-29T22:05:55.560-00:00\"] [54 0 \"4\" \"record-4\" #inst \"2020-06-29T22:05:55.560-00:00\"] [55 0 \"0\" \"record-0\" #inst \"2020-06-29T22:12:47.285-00:00\"] [56 0 \"1\" \"record-1\" #inst \"2020-06-29T22:12:47.287-00:00\"] [57 0 \"2\" \"record-2\" #inst \"2020-06-29T22:12:47.288-00:00\"] [58 0 \"3\" \"record-3\" #inst \"2020-06-29T22:12:47.289-00:00\"] [59 0 \"4\" \"record-4\" #inst \"2020-06-29T22:12:47.289-00:00\"] [60 0 \"0\" \"record-0\" #inst \"2020-06-29T22:14:43.005-00:00\"] [61 0 \"1\" \"record-1\" #inst \"2020-06-29T22:14:43.006-00:00\"] [62 0 \"2\" \"record-2\" #inst \"2020-06-29T22:14:43.006-00:00\"] [63 0 \"3\" \"record-3\" #inst \"2020-06-29T22:14:43.007-00:00\"] [64 0 \"4\" \"record-4\" #inst \"2020-06-29T22:14:43.008-00:00\"] [65 0 \"0\" \"record-0\" #inst \"2020-06-29T23:00:57.300-00:00\"] [66 0 \"1\" \"record-1\" #inst \"2020-06-29T23:00:57.335-00:00\"] [67 0 \"2\" \"record-2\" #inst \"2020-06-29T23:00:57.362-00:00\"] [68 0 \"3\" \"record-3\" #inst \"2020-06-29T23:00:57.393-00:00\"] [69 0 \"4\" \"record-4\" #inst \"2020-06-29T23:00:57.431-00:00\"]), :opts nil}"}
;; <=

;; @@
;; This is our processing setup and loop (wrapped by future so it runs in a seperate Thread)
(def consumer-producer-future 
     (future 
         (let [consumer-topic "tst-consumer-topic"
               producer-topic "tst-produced-topic"
               producer-topic-2 "tst-produced-topic-2"
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
                                                        (str "Processed Value: " (.value record))))
                       (.send producer (ProducerRecord. producer-topic-2 
                                                        (.key record)
                                                        (str "Processed Value length: " (str (count (.value record)))))))
                    (.commitAsync consumer))))))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;kafka-example/consumer-producer-future</span>","value":"#'kafka-example/consumer-producer-future"}
;; <=

;; @@
(get-records-processed)
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-long'>15</span>","value":"15"}
;; <=

;; @@
(dotimes [n 5] (send-producer-1 "tst-consumer-topic" (str n) (str "record-" n)))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; @@
(def consumer-2 (build-consumer "tst-produced-topic" "localhost:9092" "tpt"))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;kafka-example/consumer-2</span>","value":"#'kafka-example/consumer-2"}
;; <=

;; @@
(def consumer-3 (build-consumer "tst-produced-topic-2" "localhost:9092" "tpt2"))
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-var'>#&#x27;kafka-example/consumer-3</span>","value":"#'kafka-example/consumer-3"}
;; <=

;; @@
(->> (.poll consumer-2 1000)
     seq
     (map (juxt #(.offset %) 
                #(.partition %)
                #(.key %) 
                #(.value %) 
                #(java.util.Date. (.timestamp %))))
     (concat (list (list "offset" "partition" "key" "value" "timestamp")))
     table-view
)
;; @@
;; =>
;;; {"type":"list-like","open":"<center><table>","close":"</table></center>","separator":"\n","items":[{"type":"list-like","open":"<tr><td>","close":"</td></tr>","separator":"</td><td>","items":[{"type":"html","content":"<span class='clj-string'>&quot;offset&quot;</span>","value":"\"offset\""},{"type":"html","content":"<span class='clj-string'>&quot;partition&quot;</span>","value":"\"partition\""},{"type":"html","content":"<span class='clj-string'>&quot;key&quot;</span>","value":"\"key\""},{"type":"html","content":"<span class='clj-string'>&quot;value&quot;</span>","value":"\"value\""},{"type":"html","content":"<span class='clj-string'>&quot;timestamp&quot;</span>","value":"\"timestamp\""}],"value":"(\"offset\" \"partition\" \"key\" \"value\" \"timestamp\")"},{"type":"list-like","open":"<tr><td>","close":"</td></tr>","separator":"</td><td>","items":[{"type":"html","content":"<span class='clj-long'>10</span>","value":"10"},{"type":"html","content":"<span class='clj-unkown'>0</span>","value":"0"},{"type":"html","content":"<span class='clj-string'>&quot;0&quot;</span>","value":"\"0\""},{"type":"html","content":"<span class='clj-string'>&quot;Processed Value: record-0&quot;</span>","value":"\"Processed Value: record-0\""},{"type":"html","content":"<span class='clj-unkown'>#inst &quot;2020-06-29T22:56:32.656-00:00&quot;</span>","value":"#inst \"2020-06-29T22:56:32.656-00:00\""}],"value":"[10 0 \"0\" \"Processed Value: record-0\" #inst \"2020-06-29T22:56:32.656-00:00\"]"},{"type":"list-like","open":"<tr><td>","close":"</td></tr>","separator":"</td><td>","items":[{"type":"html","content":"<span class='clj-long'>11</span>","value":"11"},{"type":"html","content":"<span class='clj-unkown'>0</span>","value":"0"},{"type":"html","content":"<span class='clj-string'>&quot;1&quot;</span>","value":"\"1\""},{"type":"html","content":"<span class='clj-string'>&quot;Processed Value: record-1&quot;</span>","value":"\"Processed Value: record-1\""},{"type":"html","content":"<span class='clj-unkown'>#inst &quot;2020-06-29T22:56:32.656-00:00&quot;</span>","value":"#inst \"2020-06-29T22:56:32.656-00:00\""}],"value":"[11 0 \"1\" \"Processed Value: record-1\" #inst \"2020-06-29T22:56:32.656-00:00\"]"},{"type":"list-like","open":"<tr><td>","close":"</td></tr>","separator":"</td><td>","items":[{"type":"html","content":"<span class='clj-long'>12</span>","value":"12"},{"type":"html","content":"<span class='clj-unkown'>0</span>","value":"0"},{"type":"html","content":"<span class='clj-string'>&quot;2&quot;</span>","value":"\"2\""},{"type":"html","content":"<span class='clj-string'>&quot;Processed Value: record-2&quot;</span>","value":"\"Processed Value: record-2\""},{"type":"html","content":"<span class='clj-unkown'>#inst &quot;2020-06-29T22:56:32.656-00:00&quot;</span>","value":"#inst \"2020-06-29T22:56:32.656-00:00\""}],"value":"[12 0 \"2\" \"Processed Value: record-2\" #inst \"2020-06-29T22:56:32.656-00:00\"]"},{"type":"list-like","open":"<tr><td>","close":"</td></tr>","separator":"</td><td>","items":[{"type":"html","content":"<span class='clj-long'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>0</span>","value":"0"},{"type":"html","content":"<span class='clj-string'>&quot;3&quot;</span>","value":"\"3\""},{"type":"html","content":"<span class='clj-string'>&quot;Processed Value: record-3&quot;</span>","value":"\"Processed Value: record-3\""},{"type":"html","content":"<span class='clj-unkown'>#inst &quot;2020-06-29T22:56:32.656-00:00&quot;</span>","value":"#inst \"2020-06-29T22:56:32.656-00:00\""}],"value":"[13 0 \"3\" \"Processed Value: record-3\" #inst \"2020-06-29T22:56:32.656-00:00\"]"},{"type":"list-like","open":"<tr><td>","close":"</td></tr>","separator":"</td><td>","items":[{"type":"html","content":"<span class='clj-long'>14</span>","value":"14"},{"type":"html","content":"<span class='clj-unkown'>0</span>","value":"0"},{"type":"html","content":"<span class='clj-string'>&quot;4&quot;</span>","value":"\"4\""},{"type":"html","content":"<span class='clj-string'>&quot;Processed Value: record-4&quot;</span>","value":"\"Processed Value: record-4\""},{"type":"html","content":"<span class='clj-unkown'>#inst &quot;2020-06-29T22:56:32.657-00:00&quot;</span>","value":"#inst \"2020-06-29T22:56:32.657-00:00\""}],"value":"[14 0 \"4\" \"Processed Value: record-4\" #inst \"2020-06-29T22:56:32.657-00:00\"]"}],"value":"#gorilla_repl.table.TableView{:contents ((\"offset\" \"partition\" \"key\" \"value\" \"timestamp\") [10 0 \"0\" \"Processed Value: record-0\" #inst \"2020-06-29T22:56:32.656-00:00\"] [11 0 \"1\" \"Processed Value: record-1\" #inst \"2020-06-29T22:56:32.656-00:00\"] [12 0 \"2\" \"Processed Value: record-2\" #inst \"2020-06-29T22:56:32.656-00:00\"] [13 0 \"3\" \"Processed Value: record-3\" #inst \"2020-06-29T22:56:32.656-00:00\"] [14 0 \"4\" \"Processed Value: record-4\" #inst \"2020-06-29T22:56:32.657-00:00\"]), :opts nil}"}
;; <=

;; @@
(->> (.poll consumer-3 1000)
     seq
     (map (juxt #(.offset %) 
                #(.partition %)
                #(.key %) 
                #(.value %) 
                #(java.util.Date. (.timestamp %))))
     (concat (list (list "offset" "partition" "key" "value" "timestamp")))
     table-view
)
;; @@
;; =>
;;; {"type":"list-like","open":"<center><table>","close":"</table></center>","separator":"\n","items":[{"type":"list-like","open":"<tr><td>","close":"</td></tr>","separator":"</td><td>","items":[{"type":"html","content":"<span class='clj-string'>&quot;offset&quot;</span>","value":"\"offset\""},{"type":"html","content":"<span class='clj-string'>&quot;partition&quot;</span>","value":"\"partition\""},{"type":"html","content":"<span class='clj-string'>&quot;key&quot;</span>","value":"\"key\""},{"type":"html","content":"<span class='clj-string'>&quot;value&quot;</span>","value":"\"value\""},{"type":"html","content":"<span class='clj-string'>&quot;timestamp&quot;</span>","value":"\"timestamp\""}],"value":"(\"offset\" \"partition\" \"key\" \"value\" \"timestamp\")"},{"type":"list-like","open":"<tr><td>","close":"</td></tr>","separator":"</td><td>","items":[{"type":"html","content":"<span class='clj-long'>10</span>","value":"10"},{"type":"html","content":"<span class='clj-unkown'>0</span>","value":"0"},{"type":"html","content":"<span class='clj-string'>&quot;0&quot;</span>","value":"\"0\""},{"type":"html","content":"<span class='clj-string'>&quot;Processed Value length: 8&quot;</span>","value":"\"Processed Value length: 8\""},{"type":"html","content":"<span class='clj-unkown'>#inst &quot;2020-06-29T22:56:32.656-00:00&quot;</span>","value":"#inst \"2020-06-29T22:56:32.656-00:00\""}],"value":"[10 0 \"0\" \"Processed Value length: 8\" #inst \"2020-06-29T22:56:32.656-00:00\"]"},{"type":"list-like","open":"<tr><td>","close":"</td></tr>","separator":"</td><td>","items":[{"type":"html","content":"<span class='clj-long'>11</span>","value":"11"},{"type":"html","content":"<span class='clj-unkown'>0</span>","value":"0"},{"type":"html","content":"<span class='clj-string'>&quot;1&quot;</span>","value":"\"1\""},{"type":"html","content":"<span class='clj-string'>&quot;Processed Value length: 8&quot;</span>","value":"\"Processed Value length: 8\""},{"type":"html","content":"<span class='clj-unkown'>#inst &quot;2020-06-29T22:56:32.656-00:00&quot;</span>","value":"#inst \"2020-06-29T22:56:32.656-00:00\""}],"value":"[11 0 \"1\" \"Processed Value length: 8\" #inst \"2020-06-29T22:56:32.656-00:00\"]"},{"type":"list-like","open":"<tr><td>","close":"</td></tr>","separator":"</td><td>","items":[{"type":"html","content":"<span class='clj-long'>12</span>","value":"12"},{"type":"html","content":"<span class='clj-unkown'>0</span>","value":"0"},{"type":"html","content":"<span class='clj-string'>&quot;2&quot;</span>","value":"\"2\""},{"type":"html","content":"<span class='clj-string'>&quot;Processed Value length: 8&quot;</span>","value":"\"Processed Value length: 8\""},{"type":"html","content":"<span class='clj-unkown'>#inst &quot;2020-06-29T22:56:32.656-00:00&quot;</span>","value":"#inst \"2020-06-29T22:56:32.656-00:00\""}],"value":"[12 0 \"2\" \"Processed Value length: 8\" #inst \"2020-06-29T22:56:32.656-00:00\"]"},{"type":"list-like","open":"<tr><td>","close":"</td></tr>","separator":"</td><td>","items":[{"type":"html","content":"<span class='clj-long'>13</span>","value":"13"},{"type":"html","content":"<span class='clj-unkown'>0</span>","value":"0"},{"type":"html","content":"<span class='clj-string'>&quot;3&quot;</span>","value":"\"3\""},{"type":"html","content":"<span class='clj-string'>&quot;Processed Value length: 8&quot;</span>","value":"\"Processed Value length: 8\""},{"type":"html","content":"<span class='clj-unkown'>#inst &quot;2020-06-29T22:56:32.656-00:00&quot;</span>","value":"#inst \"2020-06-29T22:56:32.656-00:00\""}],"value":"[13 0 \"3\" \"Processed Value length: 8\" #inst \"2020-06-29T22:56:32.656-00:00\"]"},{"type":"list-like","open":"<tr><td>","close":"</td></tr>","separator":"</td><td>","items":[{"type":"html","content":"<span class='clj-long'>14</span>","value":"14"},{"type":"html","content":"<span class='clj-unkown'>0</span>","value":"0"},{"type":"html","content":"<span class='clj-string'>&quot;4&quot;</span>","value":"\"4\""},{"type":"html","content":"<span class='clj-string'>&quot;Processed Value length: 8&quot;</span>","value":"\"Processed Value length: 8\""},{"type":"html","content":"<span class='clj-unkown'>#inst &quot;2020-06-29T22:56:32.657-00:00&quot;</span>","value":"#inst \"2020-06-29T22:56:32.657-00:00\""}],"value":"[14 0 \"4\" \"Processed Value length: 8\" #inst \"2020-06-29T22:56:32.657-00:00\"]"}],"value":"#gorilla_repl.table.TableView{:contents ((\"offset\" \"partition\" \"key\" \"value\" \"timestamp\") [10 0 \"0\" \"Processed Value length: 8\" #inst \"2020-06-29T22:56:32.656-00:00\"] [11 0 \"1\" \"Processed Value length: 8\" #inst \"2020-06-29T22:56:32.656-00:00\"] [12 0 \"2\" \"Processed Value length: 8\" #inst \"2020-06-29T22:56:32.656-00:00\"] [13 0 \"3\" \"Processed Value length: 8\" #inst \"2020-06-29T22:56:32.656-00:00\"] [14 0 \"4\" \"Processed Value length: 8\" #inst \"2020-06-29T22:56:32.657-00:00\"]), :opts nil}"}
;; <=

;; @@

;; @@
