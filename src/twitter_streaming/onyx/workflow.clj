(ns twitter-streaming.onyx.workflow)

(def workflow
  [[:read-messages :count-words]
   [:count-words :out]])
