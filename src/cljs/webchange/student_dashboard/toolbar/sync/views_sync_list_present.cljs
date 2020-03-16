(ns webchange.student-dashboard.toolbar.sync.views-sync-list-present
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [reagent.core :as r]))

(def checkbox-style {:padding 6})
(def list-item-style {:padding-top    8
                      :padding-bottom 8})

(defn- list-item
  [{:keys [id name sync-stat on-click]}]
  (let [handle-click (fn []
                       (let [action (if (= :loaded sync-stat) :remove :add)]
                         (on-click action id)))]
    [ui/list-item {:button   true
                   :style    list-item-style
                   :on-click handle-click}
     [ui/list-item-text name]
     [ui/checkbox {:disable-ripple true
                   :style          checkbox-style
                   :checked        (= :loaded sync-stat)
                   :indeterminate  (= :part-loaded sync-stat)}]]))

(defn- list-group
  [{:keys [id name activities sync-stat on-click]}]
  (r/with-let [open? (r/atom true)]
              (let [handle-group-click (fn []
                                         (let [action (if (= :loaded sync-stat) :remove :add)]
                                           (on-click action id nil)))
                    handle-item-click (fn [action item-id]
                                        (on-click action id item-id))
                    handle-collapse-click #(reset! open? (not @open?))]
                [:div
                 [ui/list-item {:button   true
                                :on-click handle-collapse-click
                                :style    list-item-style}
                  [ui/list-item-text name]

                  (if @open?
                    [ic/expand-less]
                    [ic/expand-more])
                  [ui/checkbox {:disable-ripple true
                                :style          checkbox-style
                                :on-click       (fn [event]
                                                  (.stopPropagation event)
                                                  (handle-group-click))
                                :checked        (= :loaded sync-stat)
                                :indeterminate  (= :part-loaded sync-stat)}]]
                 [ui/collapse {:in              @open?
                               :timeout         "auto"
                               :unmount-on-exit true}
                  [ui/list {:style {:padding-left 32}}
                   (for [activity activities]
                     ^{:key (:id activity)}
                     [list-item (merge activity
                                       {:on-click handle-item-click})])]]])))

(defn- levels-list
  [{:keys [levels on-click]}]
  [ui/list
   (for [level levels]
     ^{:key (:id level)}
     [list-group (merge level
                        {:on-click on-click})])])

(defn sync-list-modal-view
  [{:keys [data open? loading? on-close on-save on-item-click]}]
  [ui/dialog
   {:open       open?
    :on-close   on-close
    :full-width true
    :max-width  "sm"}
   [ui/dialog-title
    "Select Resources"]
   [ui/dialog-content
    [ui/dialog-content-text
     "Select levels/scenes to be available offline."]
    [ui/divider]
    (if-not loading?
      [levels-list {:levels   data
                    :on-click on-item-click}]
      [:div
       {:style {:display         "flex"
                :min-height      "128px"
                :align-items     "center"
                :justify-content "center"}}
       [ui/circular-progress]])]
   [ui/dialog-actions
    [ui/button
     {:variant  "contained"
      :on-click on-close}
     "Cancel"]
    [ui/button
     {:color    "secondary"
      :variant  "contained"
      :on-click on-save}
     "Apply"]]])
