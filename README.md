# twitter-streaming

start zookeeper at "localhost:2181" bin/zookeeper-server-start.sh config/zookeeper.properties

start kafka at "localhost:9092" bin/kafka-server-start.sh config/server.properties

refer to http://kafka.apache.org/documentation.html#quickstart for more instructions

refer to https://devcenter.heroku.com/articles/clojure-web-application#connecting-to-postgresql-with-clojure-java-jdbc for setting up a postgresql server 

run as lein clean && lein run


## License

Copyright © 2015 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
