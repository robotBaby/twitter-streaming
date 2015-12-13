(ns twitter-streaming.core
  (:require [twitter-streaming.twitter :as t]
            [twitter-streaming.web.models.model :as model]
            [twitter-streaming.web.web :as web]
            [twitter-streaming.kafka :as k]
            [twitter-streaming.onyx.onyx :as o]
            [twitter-streaming.onyx.config :as config]))


(defn -main
  []
  (let [consumer-key ""
        consumer-secret ""
        user-access-token ""
        user-access-token-secret ""
        p (k/new-kafka-producer config/host config/zk-port config/kafka-port config/kafka-topic)]
    (web/go)
    (t/streaming consumer-key consumer-secret user-access-token user-access-token-secret)))
