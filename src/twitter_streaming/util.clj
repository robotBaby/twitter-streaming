(ns twitter-streaming.util
  (:require  [clojure.java.jdbc :as sql]))

(defn create-table []
  (sql/db-do-commands "postgresql://localhost:5432/wordcount" (sql/create-table-ddl
                                                               :wordcount
                                                               [:word :text]
                                                               [:count :int])))

(defn delete-table []
  (sql/db-do-commands "postgresQl://localhost:5432/wordcount" "Drop TABLE wordcount"))

(defn select []
  (sql/query "postgresql://localhost:5432/wordcount" ["SELECT * FROM wordcount"]))
