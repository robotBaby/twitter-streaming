(ns twitter-streaming.web.models.model
  (:require [clojure.java.jdbc :as sql]))

(def spec (or (System/getenv "DATABASE_URL")
              "postgresql://localhost:5432/hashcollect"))

(defn all []
  (into [] (sql/query spec ["select * from hashcollect order by hashes desc"])))

(defn create [word]
  (into [] (sql/query spec [(str "SELECT * FROM hashcollect WHERE word = '" "love" "'")])))

(defn add-word [word]
  (when (not (nil? word))
    (sql/insert! spec :hashcollect {:word (str word) :hashes 0})))

(defn delete-word [word]
  (sql/execute! spec [(str "DELETE from hashcollect WHERE word= '" word "'")]))

(defn get-trackterms []
  (reduce #(conj % (:word %2)) [] (sql/query spec ["select distinct word from hashcollect order by word desc"])))

(defn update-db
  [word hash-count]
  (let [exists (sql/query spec [(str "SELECT hashes FROM hashcollect WHERE word = '" word "'")])]
    (if (empty? exists)
      (sql/insert! spec :hashcollect {:word word :hashes hash-count})
      (sql/execute! spec [(str "UPDATE hashcollect SET hashes=" (+ hash-count (:hashes (first exists)))
                             " WHERE word= '" word "'")]))))
