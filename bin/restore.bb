#!/usr/bin/env bb

(require '[babashka.cli :as cli]
         '[babashka.fs :as fs]
         '[babashka.http-client :as http]
         '[clojure.edn :as edn]
         '[clojure.java.io :as io])

(def cli-options {:config {}
                  :host-url {:default "https://host.bluebrickschool.org/"}
                  :local-binary-path {:default "/app/webchange.jar"}
                  :restore {:coerce boolean}
                  :download {:coerce boolean}})

(def opts (cli/parse-opts *command-line-args* {:spec cli-options}))

(def config (when (:config opts)
              (-> opts :config slurp edn/read-string)))

(def host-url (or (-> config :secondary :host-url)
                  (-> opts :host-url)))

(def local-binary-path (or (-> config :local-binary-path)
                           (-> opts :local-binary-path)))

(def local-binary-folder (-> local-binary-path fs/parent))
(def lock-path (str local-binary-folder "/update-content.lock"))

(defn find-last-backup
  []
  (let [backups (map str (fs/glob local-binary-folder "*.*.*.bak"))]
    (-> backups
        (sort)
        (first))))

(defn get-latest-binary
  []
  (println host-url)
  (let [url (str host-url (if (.endsWith host-url "/") "" "/") "api/software/latest")]
    (:body (http/get url {:as :stream}))))

(defn restore! []
  (if-let [last-backup (find-last-backup)]
    (do
      (println "Resotring previous backup: " last-backup)
      (when (fs/exists? lock-path)
        (fs/delete lock-path))
      (fs/copy last-backup local-binary-path {:replace-existing true}))
    (println "No previous backups were found")))

(defn download! []
  (println "Downloading latest version")  
  (let [latest-binary-in (get-latest-binary)
        download-path (str local-binary-path ".dwl")]
    (-> download-path fs/parent fs/create-dirs)
    (when (fs/exists? local-binary-path)
      (fs/copy local-binary-path (str local-binary-path "." (System/currentTimeMillis) ".bak")))
    (with-open [local-binary-out (io/output-stream download-path)]
      (io/copy latest-binary-in local-binary-out))
    (fs/move download-path local-binary-path {:replace-existing true}))
  (println "Done"))

(cond
  (:restore opts) (restore!)
  (:download opts) (download!))

