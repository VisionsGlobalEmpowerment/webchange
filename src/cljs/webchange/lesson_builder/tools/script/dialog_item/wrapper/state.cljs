(ns webchange.lesson-builder.tools.script.dialog-item.wrapper.state
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.tools.stage-actions :as stage-actions]
    [webchange.logger.index :as logger]
    [webchange.utils.numbers :refer [try-parse-int]]
    [webchange.utils.scene-action-data :refer [get-new-phrase-action]]))

(defn- parse-action-path
  [path-str]
  (->> (clojure.string/split path-str ",")
       (map (fn [step]
              (let [idx (try-parse-int step)]
                (if (number? idx)
                  idx
                  (keyword step)))))))

(re-frame/reg-event-fx
  ::insert-new-phrase-action
  (fn [_ [_ {:keys [target side]}]]
    (if-not (empty? target)
      (let [relative-position (if (= (:vertical side) :top)
                                :before
                                :after)
            path (-> target :path parse-action-path)
            parent-data-path (butlast path)
            position (cond-> (last path)
                             (= relative-position :after) (inc))]
        {:dispatch [::stage-actions/insert-action {:action-data      (get-new-phrase-action)
                                                   :parent-data-path parent-data-path
                                                   :position         position}]})
      (logger/warn "Drop target is empty"))))
