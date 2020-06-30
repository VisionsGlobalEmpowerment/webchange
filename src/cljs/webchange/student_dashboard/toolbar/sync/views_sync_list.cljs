(ns webchange.student-dashboard.toolbar.sync.views-sync-list
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.student-dashboard.toolbar.sync.state.course-resources :as course-resources]
    [webchange.student-dashboard.toolbar.sync.state.sync-list :as sync-list]))

(defn- get-styles
  []
  {:list-item {:padding-top    8
               :padding-bottom 8}
   :checkbox  {:padding 6}
   :actions   {:padding "15px 6px"}})

(defn- lessons-list-item
  [{:keys [id level-name lesson-name selected?]}]
  (let [title (str level-name " - " lesson-name)
        handle-click #(re-frame/dispatch [::course-resources/select-lesson id (not selected?)])
        styles (get-styles)]
    [ui/list-item {:button   true
                   :style    (:list-item styles)
                   :on-click handle-click}
     [ui/list-item-text title]
     [ui/checkbox {:disable-ripple true
                   :style          (:checkbox styles)
                   :checked        selected?}]]))

(defn- lessons-list
  []
  (let [lessons @(re-frame/subscribe [::course-resources/course-lessons-list])]
    [ui/list
     (for [{:keys [id] :as lesson} lessons]
       ^{:key id}
       [lessons-list-item lesson])]))

(defn sync-list-modal
  []
  (let [open? @(re-frame/subscribe [::sync-list/open?])
        loading? @(re-frame/subscribe [::course-resources/loading?])
        handle-save #(re-frame/dispatch [::sync-list/save])
        handle-close #(re-frame/dispatch [::sync-list/cancel])
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
      [ui/divider {:style {:margin "15px 0px"}}]
      (if-not loading?
        [lessons-list]
        [:div
         {:style {:display         "flex"
                  :min-height      "128px"
                  :align-items     "center"
                  :justify-content "center"}}
         [ui/circular-progress]])]
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
