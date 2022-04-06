(ns webchange.templates.library.flipbook.display-names
  (:require
    [clojure.tools.logging :as log]))

;{:text "page-text-52", :action "page-52-action", :object "page-52", :removable? true}

(defn- set-dialog-name
  [activity-data action-name page-idx]
  (log/debug ">" [:actions (keyword action-name) :phrase-description] (str "Page " page-idx))
  (->> (str "Page " page-idx)
       (assoc-in activity-data [:actions (keyword action-name) :phrase-description])))

(defn update-display-names
  [activity-data {:keys [book-name]}]
  (->> (get-in activity-data [:objects book-name :pages])
       (map-indexed vector)
       (reduce (fn [activity-data [page-idx {:keys [action]}]]
                 (cond-> activity-data
                         (some? action) (set-dialog-name action page-idx)))
               activity-data)))
