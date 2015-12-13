(ns twitter-streaming.web.views.view
  (:require [hiccup.core :refer [h]]
            [hiccup.page :as page]
            [hiccup.form :as form]
            [ring.util.anti-forgery :as anti-forgery]))


(defn home
  [collection]
  (page/html5
   [:h2 "Tweet-hashtags"]
   (form/form-to [:post "/add"]
                 (anti-forgery/anti-forgery-field)
                 (form/label "word" "Search term: ")
                 (form/text-field "word")
                 (form/submit-button "add"))
   [:ul
    (for [{:keys [word hashes]} collection]
      [:li
       [:b word]
       (form/form-to [:post (str "/delete/" word)]
                     (anti-forgery/anti-forgery-field)
                     (form/submit-button "delete"))
       [:table
        (for [{:keys [word hashes]} collection]
          [:tr [:td word] [:td hashes]])]])]))
