(ns twitter-streaming.twitter
  (:require [clojure.data.fressian :as fress]
            [clj-kafka.producer :as k-p]
            [twitter-streaming.onyx.onyx :as o])
  (:import [twitter4j FilterQuery StatusListener TwitterStreamFactory]
           [twitter4j.conf ConfigurationBuilder])
  (:gen-class))

(defn send-kafka-msg
  [{:keys [producer topic]} msg]
  (k-p/send-message producer (k-p/message topic (.array (fress/write msg)))))

(defn streaming
  "Streaming tweets into kafka"
  [consumer-key consumer-secret user-access-token user-access-token-secret
   kafka-producer kafka-topic
   track-terms]

  (let [track (into-array track-terms)
        filter (doto (FilterQuery.)
                 (.count 0)
                 (.track track))
        listener (proxy [StatusListener]
                     []
                   (onStatus [status]
                     (let [tweet-text (.getText status)]
                       (println tweet-text)
                       (send-kafka-msg
                        {:producer kafka-producer :topic kafka-topic}
                        {:n tweet-text :event-time (new java.util.Date)})
                       )))
        config (-> (doto (ConfigurationBuilder.)
                     (.setOAuthConsumerKey consumer-key)
                     (.setOAuthConsumerSecret consumer-secret)
                     (.setOAuthAccessToken user-access-token)
                     (.setOAuthAccessTokenSecret user-access-token-secret)
                     (.setJSONStoreEnabled true)
                     )
                   .build)]
    (doto (.getInstance (TwitterStreamFactory. config))
      (.addListener listener)
      (.filter filter))))


(defn new-kafka-producer
  [host zk-port kafka-port kafka-topic]
  (k-p/producer {"metadata.broker.list" (str host ":" kafka-port)
                 "zk.connect" (str host ":" zk-port)
                 "serializer.class" "kafka.serializer.DefaultEncoder"
                 "partitioner.class" "kafka.producer.DefaultPartitioner"}))
