(ns twitter-streaming.onyx.functions
  (:require [clojure.data.fressian :as fress]
            [clojure.java.jdbc :as sql]))

(defn deserialize-message [bytearray]
  (fress/read bytearray))

(defn count-word
  [{:keys [host port db-name]} {:keys [n event-time]}]
  (let [sentence n
        words (map identity (clojure.string/split sentence #"\s+"))
        db (str "postgresql://" host ":" port "/" db-name)]
    (doseq [word words]
      (when  (some #(= \# %) word)
        (let [exists (sql/query db [(str "SELECT count FROM wordcount WHERE word = '" word "'")])]
          (if (empty? exists)
            (sql/insert! db :wordcount {:word word :count 1})
            (sql/execute! db [(str "UPDATE wordcount SET count=" (inc (:count (first exists)))
                                   " WHERE word= '" word "'")]))))
      word)))

;; (defn count-word
;;   [{:keys [host port db-name]} coll sentence]
;;   (let [words (map identity (clojure.string/split sentence #"\s+"))
;;         db (str "postgresql://" host ":" port "/" db-name)]
;;     (doseq [word words]
;;       (when  (some #(= \# %) word)
;;         (swap! coll update-in [word] (fnil (partial inc) 0))
;;         (when (> (count (keys @coll)) 100)
;;           (db-insert db @coll)
;;           (reset! coll {}))))
;;     (when (not (empty? @coll))
;;       (db-insert db @coll)
;;       (reset! coll {}))
;;     {:done :wordcounting}))
