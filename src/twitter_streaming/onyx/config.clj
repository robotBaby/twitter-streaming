(ns twitter-streaming.onyx.config)

(def id (java.util.UUID/randomUUID))
(def host "127.0.0.1")
(def zk-port "2181")
(def kafka-port "9092")
(def zk-addr (str host ":" zk-port))
(def kafka-topic "twitter-streaming11")
(def kafka-group-id "onyx-consumer")

(def env-config
  {:zookeeper/address zk-addr
   :zookeeper/server? true
   :zookeeper.server/port 2188
   :onyx/id id})

(def peer-config
  {:zookeeper/address zk-addr
   :onyx.peer/job-scheduler :onyx.job-scheduler/balanced
   :onyx.messaging/impl :aeron
   :onyx.messaging/peer-ports [40199]
   :onyx.messaging/bind-addr "localhost"
   :onyx/id id})
