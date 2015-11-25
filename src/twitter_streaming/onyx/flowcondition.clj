(ns twitter-streaming.onyx.flowcondition)

(def always-true (constantly true))

(def flow-conditions
  [{:flow/from :read-messages
    :flow/to [:count-words]
    :flow/predicate :twitter-streaming.onyx.flowcondition/always-true}])
