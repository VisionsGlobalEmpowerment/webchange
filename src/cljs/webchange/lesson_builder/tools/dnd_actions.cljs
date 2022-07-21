(ns webchange.lesson-builder.tools.dnd-actions
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.tools.stage-actions :as stage-actions]
    [webchange.logger.index :as logger]
    [webchange.utils.numbers :refer [try-parse-int]]
    [webchange.utils.scene-action-data :as action-utils]))

(def drop-actions {"add-emotion"            #(re-frame/dispatch [::insert-new-animation-action % :emotion])
                   "remove-emotion"         #(re-frame/dispatch [::insert-remove-animation-action % :emotion])
                   "add-character-dialogue" #(re-frame/dispatch [::insert-new-phrase-action %])
                   "add-effect"             #(re-frame/dispatch [::insert-new-effect-action %])
                   "add-text-animation"     #(re-frame/dispatch [::insert-new-text-animation-action %])})

(defn- parse-action-path
  [path-str]
  (->> (clojure.string/split path-str ",")
       (map (fn [step]
              (let [idx (try-parse-int step)]
                (if (number? idx)
                  idx
                  (keyword step)))))))

(defn- props->position
  [{:keys [target side]}]
  (let [relative-position (if (= (:vertical side) :top)
                            :before
                            :after)
        path (-> target :path parse-action-path)
        parent-data-path (butlast path)
        position (cond-> (last path)
                         (= relative-position :after) (inc))]
    {:parent-data-path parent-data-path
     :position         position}))

(re-frame/reg-event-fx
  ::insert-new-animation-action
  (fn [_ [_ {:keys [dragged target] :as props} track]]
    (if-not (empty? target)
      (let [{:keys [parent-data-path position]} (props->position props)
            {:keys [animation target]} dragged
            action-data (action-utils/create-dialog-add-animation-action {:animation animation
                                                                          :target    target
                                                                          :track     track})]
        {:dispatch [::stage-actions/insert-action {:action-data      action-data
                                                   :parent-data-path parent-data-path
                                                   :position         position}]})
      (logger/warn "Drop target is empty"))))

(re-frame/reg-event-fx
  ::insert-remove-animation-action
  (fn [_ [_ {:keys [dragged target] :as props} track]]
    (if-not (empty? target)
      (let [{:keys [parent-data-path position]} (props->position props)
            {:keys [target]} dragged
            action-data (action-utils/create-dialog-remove-animation-action {:target target
                                                                             :track  track})]
        {:dispatch [::stage-actions/insert-action {:action-data      action-data
                                                   :parent-data-path parent-data-path
                                                   :position         position}]})
      (logger/warn "Drop target is empty"))))

(re-frame/reg-event-fx
  ::insert-new-effect-action
  (fn [_ [_ {:keys [dragged target] :as props}]]
    (if-not (empty? target)
      (let [{:keys [parent-data-path position]} (props->position props)
            {:keys [action-type]} dragged]
        {:dispatch [::stage-actions/insert-action {:action-data      (action-utils/create-dialog-effect-action {:type action-type})
                                                   :parent-data-path parent-data-path
                                                   :position         position}]})
      (logger/warn "Drop target is empty"))))

(re-frame/reg-event-fx
  ::insert-new-phrase-action
  (fn [_ [_ {:keys [target] :as props}]]
    (if-not (empty? target)
      (let [{:keys [parent-data-path position]} (props->position props)]
        {:dispatch [::stage-actions/insert-action {:action-data      (action-utils/create-dialog-animation-sequence-action)
                                                   :parent-data-path parent-data-path
                                                   :position         position}]})
      (logger/warn "Drop target is empty"))))

(re-frame/reg-event-fx
  ::insert-new-text-animation-action
  (fn [_ [_ {:keys [target] :as props}]]
    (if-not (empty? target)
      (let [{:keys [parent-data-path position]} (props->position props)]
        {:dispatch [::stage-actions/insert-action {:action-data      (action-utils/create-dialog-text-animation-action)
                                                   :parent-data-path parent-data-path
                                                   :position         position}]})
      (logger/warn "Drop target is empty"))))