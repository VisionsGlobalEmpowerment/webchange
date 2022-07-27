(ns webchange.lesson-builder.tools.dnd-actions
  (:require
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.stage-actions :as stage-actions]
    [webchange.logger.index :as logger]
    [webchange.utils.numbers :refer [try-parse-int]]
    [webchange.utils.scene-action-data :as action-utils]))

(def drop-actions {"add-action"             #(re-frame/dispatch [::insert-new-activity-action %])
                   "add-emotion"            #(re-frame/dispatch [::insert-new-animation-action % :emotion])
                   "add-movement"           #(re-frame/dispatch [::insert-new-movement-action %])
                   "remove-emotion"         #(re-frame/dispatch [::insert-remove-animation-action % :emotion])
                   "add-character-dialogue" #(re-frame/dispatch [::insert-new-phrase-action %])
                   "add-effect"             #(re-frame/dispatch [::insert-new-effect-action %])
                   "add-text-animation"     #(re-frame/dispatch [::insert-new-text-animation-action %])
                   "move-dialog-action"     #(re-frame/dispatch [::move-dialog-action %])})

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

(defn- parse-target-position
  [props]
  (let [{:keys [parent-data-path position]} (props->position props)]
    (-> (vec parent-data-path)
        (conj position))))

(re-frame/reg-event-fx
  ::insert-new-activity-action
  (fn [_ [_ {:keys [dragged target] :as props}]]
    (if-not (empty? target)
      (let [{:keys [parent-data-path position]} (props->position props)
            {:keys [action-id]} dragged]
        {:dispatch [::stage-actions/insert-action {:action-data      (action-utils/create-dialog-activity-action {:id action-id})
                                                   :parent-data-path parent-data-path
                                                   :position         position}]})
      (logger/warn "Drop target is empty"))))

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
  ::insert-new-movement-action
  (fn [_ [_ {:keys [dragged target] :as props}]]
    (if-not (empty? target)
      (let [{:keys [parent-data-path position]} (props->position props)
            {:keys [character movement target]} dragged
            action-data (action-utils/create-dialog-char-movement-action {:action        movement
                                                                          :transition-id character
                                                                          :target        target})]
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

(re-frame/reg-event-fx
  ::move-dialog-action
  (fn [_ [_ {:keys [dragged target] :as props}]]
    (if-not (empty? target)
      (let [target-action-path (parse-target-position props)
            source-action-path (parse-action-path (:path dragged))]
        {:dispatch [::stage-actions/move-action {:source-action-path source-action-path
                                                 :target-action-path target-action-path}]})
      (logger/warn "Drop target is empty"))))
