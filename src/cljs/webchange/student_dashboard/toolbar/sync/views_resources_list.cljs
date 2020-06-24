(ns webchange.student-dashboard.toolbar.sync.views-resources-list
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [webchange.student-dashboard.toolbar.sync.state.course-resources :as course-resources]))

(defn- get-styles
  []
  {:list-item {:padding-top    8
               :padding-bottom 8}
   :checkbox  {:padding 6}})

(defn- list-item
  [{:keys [id level-name lesson-name selected?]}]
  (let [title (str level-name " - " lesson-name)
        handle-click (fn [] (re-frame/dispatch [::course-resources/switch-lesson-resources id]))
        styles (get-styles)]
    [ui/list-item {:button   true
                   :style    (:list-item styles)
                   :on-click handle-click}
     [ui/list-item-text title]
     [ui/checkbox {:disable-ripple true
                   :style          (:checkbox styles)
                   :checked        selected?}]]))

(defn resources-list
  []
  (let [lessons @(re-frame/subscribe [::course-resources/course-resources-list])]
    [ui/list
     (for [{:keys [id] :as lesson} lessons]
       ^{:key id}
       [list-item lesson])]))
