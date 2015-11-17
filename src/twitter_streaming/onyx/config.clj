(ns twitter-streaming.onyx.config)

(def id (java.util.UUID/randomUUID))
(def zk-addr "127.0.0.1:2181")
  (def kafka-topic "twitter-streaming")
(def kafka-group-id "onyx-consumer")

(def env-config
  {:zookeeper/address zk-addr
   :zookeeper/server? true
   :zookeeper.server/port 2188
   :onyx/id id})

(def peer-config
  {:zookeeper/address zk-addr
   :onyx.peer/job-scheduler :onyx.job-scheduler/greedy
   :onyx.messaging/impl :aeron
   :onyx.messaging/peer-ports [40199]
   :onyx.messaging/bind-addr "localhost"
   :onyx/id id})
