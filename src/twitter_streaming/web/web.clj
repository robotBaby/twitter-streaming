(ns twitter-streaming.web.web
  (:require [compojure.core :refer [defroutes]]
            [ring.adapter.jetty :as ring]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [twitter-streaming.web.controllers.controller :as controller]
            [twitter-streaming.web.views.layout :as layout]
            [twitter-streaming.web.models.migration :as schema])
  (:gen-class))

(defroutes routes
  controller/routes
  (route/resources "/")
  (route/not-found (layout/four-oh-four)))

(def application (wrap-defaults routes site-defaults))

(defn start [port]
  (ring/run-jetty application {:port port
                               :join? false}))

(defn go []
  (schema/migrate)
  (let [port (Integer. (or (System/getenv "PORT") "8080"))]
    (start port)))
