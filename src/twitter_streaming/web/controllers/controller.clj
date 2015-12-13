(ns twitter-streaming.web.controllers.controller
  (:require [compojure.core :refer [defroutes GET POST]]
            [twitter-streaming.kafka :as k]
            [twitter-streaming.onyx.config :as config]
            [clojure.string :as str]
            [ring.util.response :as ring]
            [twitter-streaming.web.views.view :as view]
            [twitter-streaming.web.models.model :as model]))

(defn add
  [word]
  (let [existing-terms (model/get-trackterms)
        ;;p (k/new-kafka-producer config/host config/zk-port
        ;;                        config/kafka-port config/client-to-db-topic)
        ]
    ;;(k/send-kafka-msg  {:producer p :topic config/client-to-db-topic}
    ;;                   (set (conj existing-terms (str word))))
   (model/add-word word)))

(defn delete
  [word]
  (let [existing-terms (model/get-trackterms)
        ;;p (k/new-kafka-producer config/host config/zk-port
        ;;                        config/kafka-port config/client-to-db-topic)
        ]
   ;; (k/send-kafka-msg  {:producer p :topic config/client-to-db-topic}
   ;;                    (set (remove (set [(str word)]) existing-terms)))
   (model/delete-word word)))


(defroutes routes
  (GET  "/" [] (view/home (model/all)))
  (POST "/add" [word]
        (add word)
        (ring/redirect "."))
  (POST "/delete/:word" [word]
        (delete word)
        (ring/redirect "..")))
