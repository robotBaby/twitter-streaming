(ns twitter-streaming.web
  (:require [compojure.core :refer [defroutes GET]]
            [ring.adapter.jetty :as ring]))

(defroutes routes
  (GET "/" [] "<h2>Hello World</h2>"))
