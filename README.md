# twitter-streaming

start zookeeper at "localhost:2181" bin/zookeeper-server-start.sh config/zookeeper.properties

start kafka at "localhost:9092" bin/kafka-server-start.sh config/server.properties

refer to http://kafka.apache.org/documentation.html#quickstart for more instructions

run as lein clean && lein run


## License

Copyright © 2015 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
