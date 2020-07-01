(ns webchange.student-dashboard.toolbar.sync.views-resources-list
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.student-dashboard.toolbar.sync.state.course-resources :as course-resources]))

(defn- get-styles
  []
  {:list-item {:padding-top    8
               :padding-bottom 8}
   :checkbox  {:padding 6}})

(defn- list-item
  [{:keys [id level-name lesson-name selected?]}]
  (r/with-let [init-selected (r/atom selected?)
               show-progress? (r/atom false)]
              (when-not (= selected? @init-selected)
                (reset! show-progress? false))
              (let [title (str level-name " - " lesson-name)
                    handle-click (fn []
                                   (reset! show-progress? true)
                                   (re-frame/dispatch [::course-resources/switch-lesson-resources id]))
                    styles (get-styles)]
                [ui/list-item {:button   true
                               :style    (:list-item styles)
                               :on-click handle-click}
                 [ui/list-item-text title]
                 (if @show-progress?
                   [ui/circular-progress]
                   [ui/checkbox {:disable-ripple true
                               :style          (:checkbox styles)
                               :checked        selected?}])])))

(defn resources-list
  []
  (let [lessons @(re-frame/subscribe [::course-resources/course-resources-list])]
    [ui/list
     (for [{:keys [id] :as lesson} lessons]
       ^{:key id}
       [list-item lesson])]))
