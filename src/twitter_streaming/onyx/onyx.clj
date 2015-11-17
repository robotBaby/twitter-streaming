(ns twitter-streaming.onyx.onyx
  (:require [clojure.core.async :refer [chan >!! <!!]]
            [twitter-streaming.onyx.functions]
            [twitter-streaming.onyx.config :as config]
            [twitter-streaming.onyx.catalog :as cat]
            [twitter-streaming.onyx.workflow :as wf]
            [twitter-streaming.onyx.flowcondition :as fl]
            [onyx.plugin.kafka]
            [onyx.api]))

;; Core async channel output

(def out-chan (chan 10000))

(defn inject-out-ch [event lifecycle]
  {:core.async/chan out-chan})

(def out-calls
  {:lifecycle/before-task-start inject-out-ch})

;;;;;;; Declaring lifecycle

(def lifecycles
  [{:lifecycle/task :read-messages
    :lifecycle/calls :onyx.plugin.kafka/read-messages-calls}
   {:lifecycle/task :out
    :lifecycle/calls :twitter-streaming.onyx.onyx/out-calls}
   {:lifecycle/task :out
    :lifecycle/calls :onyx.plugin.core-async/writer-calls}])

;;;;; Submit job and shutdown

(defn read-output[]
  (<!! out-chan))

(defn submit []
  (let [env (onyx.api/start-env config/env-config)
        peer-group (onyx.api/start-peer-group config/peer-config)
        v-peers (onyx.api/start-peers 10 peer-group)
        job {:catalog cat/catalog
             :workflow wf/workflow
             :lifecycles lifecycles
             :flow-conditions fl/flow-conditions
             :task-scheduler :onyx.task-scheduler/balanced}
        submitting (onyx.api/submit-job config/peer-config job)]
    {:v-peers v-peers :peer-group peer-group :env env}))

(defn shutdown
  [{:keys [v-peers peer-group env]}]
  (do
    (doseq [v-peer v-peers]
      (onyx.api/shutdown-peer v-peer))
    (onyx.api/shutdown-peer-group peer-group)
    (onyx.api/shutdown-env env)
    (shutdown-agents)))
