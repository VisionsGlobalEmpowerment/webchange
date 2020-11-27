(ns webchange.mq.zero-mq
  (:require [ezzmq.core :as zmq]
            [config.core :refer [env]]
            [clojure.core.async :as async]
            [clojure.edn :as edn]))

(def queues {:voice-recognition {:vent (async/chan 100)
                                 :vent-url (:voice-recognition-vent-socket env)
                                 :sink-url (:voice-recognition-sink-socket env)
                                 }})

(defn init-ventilator
  [vent-url chan]
  (async/go
    (zmq/with-new-context
      (let [vent (zmq/socket :push {:bind vent-url})]
          (while true
            (let [task (async/<! chan)]
            (zmq/send-msg vent (str task))))))
    ))

(defn sink-receive
  [queue-name on-receive-callback]
  (zmq/with-new-context
    (let [sink (zmq/socket :pull {:bind (get-in queues [queue-name :sink-url])})]
      (while true
        (let [result (-> (zmq/receive-msg sink {:stringify true}) first)]
          (on-receive-callback (edn/read-string  result)))))))

(defn send
  [queue-name message]
  (let [queue (get-in queues [queue-name :vent])]
    (async/put! queue message)))

(defn receive
  [queue-name on-receive-callback]
  (zmq/with-new-context
    (let [vent (zmq/socket :pull {:connect (get-in queues [queue-name :vent-url])})
          sink (zmq/socket :push {:connect (get-in queues [queue-name :sink-url])})]
      (while true
        (let [task (-> (zmq/receive-msg vent {:stringify true}) first edn/read-string)
              result (on-receive-callback task)]
          (if result (zmq/send-msg sink (pr-str result))))))))

