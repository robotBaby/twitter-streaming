(ns twitter-streaming.kafka
  (:require [clojure.data.fressian :as fress]
            [clj-kafka.producer :as k-p]
            [clj-kafka.consumer.zk :as k-c]))

(defn send-kafka-msg
  [{:keys [producer topic]} msg]
  (k-p/send-message producer (k-p/message topic (.array (fress/write msg)))))


(defn read-kafka-msg
  [{:keys [consumer topic]}]
  (k-c/messages consumer topic))

(defn new-kafka-producer
  [host zk-port kafka-port kafka-topic]
  (k-p/producer {"metadata.broker.list" (str host ":" kafka-port)
                 "zk.connect" (str host ":" zk-port)
                 "serializer.class" "kafka.serializer.DefaultEncoder"
                 "partitioner.class" "kafka.producer.DefaultPartitioner"}))

(defn new-kafka-consumer
  [host zk-port kafka-port kafka-topic]
  (k-c/consumer {"metadata.broker.list" (str host ":" kafka-port)
                 "zookeeper.connect" (str host ":" zk-port)
                 "group.id" "consumer"}))
