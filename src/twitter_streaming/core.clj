(ns twitter-streaming.core
  (:require [clojure.data.fressian :as fress]
            [clj-kafka.producer :as k-p])
  (:import [twitter4j FilterQuery StatusListener TwitterStreamFactory]
           [twitter4j.conf ConfigurationBuilder])
  (:gen-class))

(defn send-kafka-msg
  [{:keys [producer topic]} msg]
  (k-p/send-message producer (k-p/message topic (.array (fress/write msg)))))

(defn streaming
  "Streaming tweets into kafka"
  [consumer-key consumer-secret user-access-token user-access-token-secret
   kafka-producer kafka-topic]

  (let [listener (proxy [StatusListener]
                     []
                   (onStatus [status]
                     (let [tweet-text (.getText status)]
                       (println tweet-text)
                       (send-kafka-msg
                        {:producer kafka-producer :topic kafka-topic}
                        tweet-text)
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
      (.sample))))

(def host "localhost")
(def zk-port 2181)
(def kafka-port 9092)
(def kafka-topic "twitter-streaming")

(defn new-kafka-producer
  [host zk-port kafka-port kafka-topic]
  (k-p/producer {"metadata.broker.list" (str host ":" kafka-port)
                 "zk.connect" (str host ":" zk-port)
                 "serializer.class" "kafka.serializer.DefaultEncoder"
                 "partitioner.class" "kafka.producer.DefaultPartitioner"}))
(defn -main
  []
  (let [consumer-key "kgPY3tAKH3szyJLSpBSQiJRft"
        consumer-secret "6GJFpJIaPNvWHs7zfsvrXbLMs06uiCKl7w8LbyBxdQH2OwW59K"
        user-access-token "2655196867-DSc4lfoG7wexkkmEGwlUTGJt4VyA8vH1T4YJlbq"
        user-access-token-secret "UVlaVW4xaqXE5HQztwmGgsahMlf3v1kRXoeEKQOU3Mlk1"
        p (new-kafka-producer host zk-port kafka-port kafka-topic)]
    (streaming consumer-key consumer-secret user-access-token user-access-token-secret
               p kafka-topic)))
