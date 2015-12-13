(ns twitter-streaming.twitter
  (:require [twitter-streaming.kafka :as k]
            [twitter-streaming.web.models.model :as model])
  (:import [twitter4j FilterQuery StatusListener TwitterStreamFactory]
           [twitter4j.conf ConfigurationBuilder])
  (:gen-class))

(defn collect-hashtag
  [tweet-text]
  (let [hashes (reduce (fn [collection word]
                         (if (some #(= \# %) word)
                           (conj collection word)
                           collection))
                       [] (clojure.string/split tweet-text #"\s+"))]
    hashes))

(defn tracking-terms
  [track-terms tweet-text]
  (let [tracked-terms (reduce (fn [collection word]
                                (if (some #(= word %) track-terms)
                                  (conj collection word)
                                  collection))
                              [] (clojure.string/split tweet-text #"\s+"))]
    tracked-terms))

(defn streaming
  "Streaming tweets into kafka"
  [consumer-key consumer-secret user-access-token user-access-token-secret]

  (let [listener (proxy [StatusListener]
                     []
                   (onStatus [status]
                     (let [tweet-text (.getText status)
                           terms-tracked (tracking-terms
                                          (model/get-trackterms)
                                          tweet-text)
                           tweet-hashes (collect-hashtag tweet-text)]
                       (when (and (not (empty? terms-tracked)) (not (empty? tweet-hashes)))
                         (doseq [term terms-tracked]
                           (println term (count tweet-hashes) tweet-text)
                           (model/update-db term (count tweet-hashes)))
                         ))))
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
      (.filter (doto (FilterQuery.)
                        (.count 0)
                        (.track (into-array
                                 (model/get-trackterms))))))))
