(ns webchange.student-dashboard.sync.views-sync-list
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [clojure.set :refer [difference union]]
    [webchange.student-dashboard.sync.events :as events]
    [webchange.student-dashboard.sync.subs :as subs]
    [webchange.service-worker.message :as sw]
    [webchange.service-worker.subs :as sw-subs]))

(def checkbox-style {:padding 6})
(def list-item-style {:padding-top    8
                      :padding-bottom 8})

(defn get-entity-loaded-stat
  [activity-resources loaded-resources]
  (let [not-loaded-count (-> (difference (set activity-resources)
                                         (set loaded-resources))
                             (count))]
    (cond
      (= not-loaded-count 0) :loaded
      (= not-loaded-count (count activity-resources)) :not-loaded
      :else :part-loaded)))

(defn get-level-resources
  [activities]
  (->> activities
       (map :resources)
       (apply concat)
       (distinct)))

(defn get-level-endpoints
  [activities]
  (->> activities
       (map :endpoint)
       (distinct)))

(defn list-item
  [{:keys [name endpoint resources handle-click loaded-resources]}]
  (let [sync-stat (get-entity-loaded-stat resources loaded-resources)]
    [ui/list-item {:button   true
                   :style    list-item-style
                   :on-click #(handle-click (if (= :loaded sync-stat) :remove :add) [endpoint] resources)}
     [ui/list-item-text name]
     [ui/checkbox {:disable-ripple true
                   :style          checkbox-style
                   :checked        (= :loaded sync-stat)
                   :indeterminate  (= :part-loaded sync-stat)}]]))

(defn list-group
  [{:keys [name activities handle-click synced? loaded-resources]}]
  (r/with-let [open? (r/atom true)]
              (let [level-resources (get-level-resources activities)
                    level-endpoints (get-level-endpoints activities)
                    sync-stat (get-entity-loaded-stat level-resources loaded-resources)
                    handle-group-item-click #(reset! open? (not @open?))]
                [:div
                 [ui/list-item {:button   true
                                :on-click handle-group-item-click
                                :style    list-item-style}
                  [ui/list-item-text name]

                  (if @open?
                    [ic/expand-less]
                    [ic/expand-more])
                  [ui/checkbox {:disable-ripple true
                                :style          checkbox-style
                                :on-click       (fn [event]
                                                  (.stopPropagation event)

                                                  (handle-click (if (= :loaded sync-stat) :remove :add) level-endpoints level-resources))
                                :checked        (= :loaded sync-stat)
                                :indeterminate  (= :part-loaded sync-stat)}]]
                 [ui/collapse {:in              @open?
                               :timeout         "auto"
                               :unmount-on-exit true}
                  [ui/list {:style {:padding-left 32}}
                   (for [activity activities]
                     ^{:key (:id activity)}
                     [list-item (merge activity
                                       {:handle-click     handle-click
                                        :synced?          synced?
                                        :loaded-resources loaded-resources})])]]])))

(defn levels-list
  [levels loaded-resources handle-click]
  [ui/list
   (for [level levels]
     ^{:key (:id level)}
     [list-group (merge level
                        {:handle-click     handle-click
                         :loaded-resources loaded-resources})])])

;; ToDo: separate on view and container
(defn sync-list-modal
  []
  (re-frame/dispatch [::events/load-scenes])
  (sw/get-cached-resources)
  (let [scenes-data (re-frame/subscribe [::subs/scenes-data])
        scenes-loading (re-frame/subscribe [::subs/scenes-loading])
        synced-game-resources (re-frame/subscribe [::sw-subs/get-synced-game-resources])
        window-opened? (re-frame/subscribe [::subs/list-open])]
    (r/with-let [loaded-resources (r/atom [])
                 loaded-endpoints (r/atom [])]
                (let [handle-resources-changed (fn [action endpoints resources]
                                                 ;; ToDo: allow remove resources
                                                 (case action
                                                   :add (let [loaded-resources-set (set @loaded-resources)
                                                              add-resources-set (set resources)
                                                              new-resources (difference add-resources-set
                                                                                        loaded-resources-set)]
                                                          (reset! loaded-resources (vec (union loaded-resources-set new-resources)))
                                                          (reset! loaded-endpoints (distinct (concat @loaded-endpoints endpoints))))))
                      handle-save (fn []
                                    (let [resources-to-add (difference (set @loaded-resources) (set @synced-game-resources))]
                                      (sw/set-cached-scenes {:scenes    {:add    @loaded-endpoints}
                                                             :resources {:add    resources-to-add}})
                                      (re-frame/dispatch [::events/close-sync-list])))
                      handle-close #(re-frame/dispatch [::events/close-sync-list])
                      selected-resources (concat @synced-game-resources @loaded-resources)]
                  [ui/dialog
                   {:open       @window-opened?
                    :on-close   handle-close
                    :full-width true
                    :max-width  "sm"}
                   [ui/dialog-title
                    "Select Resources"]
                   [ui/dialog-content {:class-name "translation-form"}
                    [ui/dialog-content-text
                     "Select levels/scenes to be available offline."]
                    [ui/divider]
                    [levels-list @scenes-data selected-resources handle-resources-changed]]
                   [ui/dialog-actions
                    [ui/button
                     {:on-click handle-close}
                     "Cancel"]
                    [ui/button
                     {:color    "secondary"
                      :variant  "contained"
                      :on-click handle-save}
                     "Apply"]]]))))
