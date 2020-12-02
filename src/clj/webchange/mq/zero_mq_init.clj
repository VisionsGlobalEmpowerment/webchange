(ns webchange.mq.zero-mq-init
  (:require [ezzmq.core :as zmq]
            [webchange.mq.zero-mq :as zero-mq]
            [clojure.core.async :as async]
            [mount.core :refer [defstate]]
            [webchange.common.voice-recognition.voice-recognition :as vr]))

(defn init
  []
    (zero-mq/init-ventilator (get-in zero-mq/queues [:voice-recognition :vent-url]) (get-in zero-mq/queues [:voice-recognition :vent]))
    (clojure.core.async/thread (zero-mq/sink-receive :voice-recognition vr/sink-callback)))


(defstate zero-mq-init
          :start (init))