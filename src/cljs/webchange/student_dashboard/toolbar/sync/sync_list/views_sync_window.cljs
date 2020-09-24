(ns webchange.student-dashboard.toolbar.sync.sync-list.views-sync-window
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.student-dashboard.toolbar.sync.sync-list.state.selection-state :as resources]
    [webchange.student-dashboard.toolbar.sync.sync-list.state.window :as window]
    [webchange.student-dashboard.toolbar.sync.sync-list.views-lessons-list :refer [lessons-list]]))

(defn- get-styles
  []
  {:actions {:padding "15px 6px"}})

(defn- loading-block
  []
  [:div
   {:style {:display         "flex"
            :min-height      "128px"
            :align-items     "center"
            :justify-content "center"}}
   [ui/circular-progress]])

(defn sync-list-modal
  []
  (let [open? @(re-frame/subscribe [::window/open?])
        lessons @(re-frame/subscribe [::resources/lessons])
        selected-lessons-count (->> lessons
                                    (map :selected?)
                                    (filter true?)
                                    (count))
        loading? @(re-frame/subscribe [::resources/loading])
        handle-save #(re-frame/dispatch [::window/save])
        handle-close #(re-frame/dispatch [::window/close])
        handle-select-item #(re-frame/dispatch [::resources/select (:id %)])
        handle-deselect-item #(re-frame/dispatch [::resources/deselect (:id %)])
        styles (get-styles)]
    [ui/dialog
     {:open       open?
      :on-close   handle-close
      :full-width true
      :max-width  "sm"}
     [ui/dialog-title
      "Select Resources"]
     [ui/dialog-content
      [ui/dialog-content-text
       "Select levels/scenes to be available offline."]
      (when (= selected-lessons-count 0)
        [ui/dialog-content-text {:style {:color "#ff9f21"}}
         "No lesson selected"])
      [ui/divider {:style {:margin "15px 0px"}}]
      (if loading?
        [loading-block]
        [lessons-list {:lessons     lessons
                       :on-select   handle-select-item
                       :on-deselect handle-deselect-item}])]
     [ui/dialog-actions {:style (:actions styles)}
      [ui/button
       {:variant  "contained"
        :on-click handle-close}
       "Close"]
      [ui/button
       {:variant  "contained"
        :color    "secondary"
        :on-click handle-save}
       "Save"]]]))
