(ns webchange.migrations.concepts
  (:require
    [clojure.tools.logging :as log]
    [mount.core :as mount]
    [webchange.migrations.concepts-letter-intro :as letter-intro]
    [webchange.migrations.concepts-writing-practice :as writing-practice]
    [webchange.migrations.concepts-cinema :as cinema]
    [webchange.migrations.concepts-running :as running]))

(defn migrate-up
  [_]
  (mount/start)
  (letter-intro/migrate-up)
  (writing-practice/migrate-up)
  (cinema/migrate-up)
  (running/migrate-up))

(defn migrate-down
  [_]
  (mount/start))
