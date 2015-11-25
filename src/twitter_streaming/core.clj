(ns twitter-streaming.core
  (:require [twitter-streaming.twitter :as t]
            [twitter-streaming.onyx.onyx :as o]
            [twitter-streaming.onyx.config :as config]))


(defn -main
  []
  (let [consumer-key ""
        consumer-secret ""
        user-access-token ""
        user-access-token-secret ""
        p (t/new-kafka-producer config/host config/zk-port config/kafka-port config/kafka-topic)
        track-terms ["ISIS"]
        onyx (o/submit)]
    (t/streaming consumer-key consumer-secret user-access-token user-access-token-secret
                 p config/kafka-topic
                 track-terms)))
