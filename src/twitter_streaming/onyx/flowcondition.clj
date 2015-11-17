(ns twitter-streaming.onyx.flowcondition)

(def always-true (constantly true))

(def flow-conditions
  [{:flow/from :read-messages
    :flow/to [:identity]
    :flow/predicate :twitter-streaming.onyx.flowcondition/always-true}])
