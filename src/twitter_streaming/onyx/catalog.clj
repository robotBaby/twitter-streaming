(ns twitter-streaming.onyx.catalog
  (:require [twitter-streaming.onyx.config :as config]
            [twitter-streaming.onyx.functions]
            [onyx.plugin.kafka]))

(def catalog
  [{:onyx/name :read-messages
    :onyx/plugin :onyx.plugin.kafka/read-messages
    :onyx/type :input
    :onyx/medium :kafka
    :kafka/topic config/kafka-topic
    :kafka/group-id config/kafka-group-id
    :kafka/fetch-size 307200
    :kafka/chan-capacity 1000
    :kafka/zookeeper config/zk-addr
    :kafka/offset-reset :smallest
    :kafka/force-reset? true
    :kafka/empty-read-back-off 500
    :kafka/commit-interval 500
    :kafka/deserializer-fn :twitter-streaming.onyx.functions/deserialize-message
    :onyx/min-peers 1
    :onyx/max-peers 1
    :onyx/batch-size 100
    :onyx/doc "Reads messages from a Kafka topic"}

   {:onyx/name :identity
    :onyx/fn :clojure.core/identity
    :onyx/type :function
    :onyx/batch-size 100}

   {:onyx/name :out
    :onyx/plugin :onyx.plugin.core-async/output
    :onyx/type :output
    :onyx/medium :core.async
    :onyx/batch-size 100}])
