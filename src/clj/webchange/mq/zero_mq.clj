(ns webchange.mq.zero-mq
  (:require [ezzmq.core :as zmq]
            [config.core :refer [env]]
            [clojure.tools.logging :as log]
            [clojure.core.async :as async]
            [clojure.edn :as edn]))

(defonce active (atom true))

(def queues {:voice-recognition {:vent (async/chan 100)
                                 :vent-url (:voice-recognition-vent-socket env)
                                 :sink-url (:voice-recognition-sink-socket env)
                                 :worker-vent-url (:voice-recognition-worker-vent-socket env)
                                 :worker-sink-url (:voice-recognition-worker-sink-socket env)
                                 }})

(defn init-ventilator
  [vent-url chan]
  (async/go
    (zmq/with-new-context
      (let [vent (zmq/socket :push {:bind vent-url})]
        (while @active
          (let [task (async/<! chan)]
            (zmq/send-msg vent (str task) {:timeout 30000})))))))

(defn sink-receive
  [queue-name on-receive-callback]
  (zmq/with-new-context
    (let [sink (zmq/socket :pull {:bind (get-in queues [queue-name :sink-url])})]
      (while @active
        (try
          (let [result (-> (zmq/receive-msg sink {:stringify true :timeout 30000}) first)]
            (when result
              (log/debug "Sink receive with result")
              (on-receive-callback (edn/read-string result))))
          (catch Exception e
            (log/error e)))))))

(defn send
  [queue-name message]
  (let [queue (get-in queues [queue-name :vent])]
    (async/put! queue message)))

(defn receive
  [queue-name on-receive-callback]
  (log/debug "Start receiving " queue-name)
  (zmq/with-new-context
    (let [vent (zmq/socket :pull {:connect (get-in queues [queue-name :worker-vent-url])})
          sink (zmq/socket :push {:connect (get-in queues [queue-name :worker-sink-url])})]
      (log/debug "Connection started")
      (while @active
        (let [task (-> (zmq/receive-msg vent {:stringify true :timeout 30000})
                       first
                       edn/read-string)]
          (log/debug "Received task" task)
          (when task
            (when-let [result (on-receive-callback task)]
              (zmq/send-msg sink (pr-str result)))))))))
