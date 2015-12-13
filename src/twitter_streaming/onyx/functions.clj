(ns twitter-streaming.onyx.functions
  (:require [clojure.data.fressian :as fress]
            [clojure.java.jdbc :as sql]))

(defn deserialize-message [bytearray]
  (fress/read bytearray))


(defn count-word
  [{:keys [host port db-name]} {:keys [tracked-terms tweet-hashes]}]
  (let [db (str "postgresql://" host ":" port "/" db-name)]
    (doseq [word tracked-terms]
      (let [exists (sql/query db [(str "SELECT hashes FROM hashcollect WHERE word = '" word "'")])]
        (if (empty? exists)
          (sql/insert! db :hashcollect {:word word :hashes (count tweet-hashes)})
          (sql/execute! db [(str "UPDATE hashcollect SET hashes=" (+ (count tweet-hashes) (:hashes (first exists)))
                                 " WHERE word= '" word "'")])))
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
