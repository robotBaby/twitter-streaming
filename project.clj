(defproject twitter-streaming "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :repositories [["twitter4j" "http://twitter4j.org/maven2"]]
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.twitter4j/twitter4j-core "3.0.6"]
                 [org.twitter4j/twitter4j-stream "3.0.6"]
                 [org.clojure/data.fressian "0.2.0"]
                 [clj-kafka "0.3.3"]
		 [org.clojure/java.jdbc "0.4.1"]
		 [org.postgresql/postgresql "9.4-1201-jdbc41"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [org.onyxplatform/onyx "0.7.5"]
                 [org.onyxplatform/onyx-kafka "0.7.5"]]
  :java-source-paths ["src/jvm"]
  :aot :all
  :main twitter-streaming.core)
