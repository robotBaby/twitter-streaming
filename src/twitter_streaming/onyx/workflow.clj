(ns twitter-streaming.onyx.workflow)

(def workflow
  [[:read-messages :identity]
   [:identity :out]])
