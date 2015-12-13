(ns twitter-streaming.web.models.migration
  (:require [clojure.java.jdbc :as sql]
            [twitter-streaming.web.models.model :as model]))

(defn migrated? []
  (-> (sql/query model/spec
                 [(str "select count(*) from information_schema.tables "
                       "where table_name='hashcollect'")])
      first :count pos?))

(defn table? []
  (-> (sql/query model/spec
                 [(str "select count(*) from hashcollect")]) first :count pos?))

(defn migrate []
  (when (not (migrated?))
    (print "Creating database structure...") (flush)
    (sql/db-do-commands model/spec
                        (sql/create-table-ddl
                         :hashcollect
                         [:word :text]
                         [:hashes :integer])))
  (when (not (table?))
    (print "Creating a dummy emtry...")
    (model/add-word "love"))
  (println " done"))
