(ns twitter-streaming.onyx.functions
  (:require [clojure.data.fressian :as fress]))

(defn deserialize-message [bytearray]
  (fress/read bytearray))
