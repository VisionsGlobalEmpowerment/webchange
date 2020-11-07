(ns webchange.student-dashboard.toolbar.sync.sync-list.views-lessons-list
  (:require
    [cljs-react-material-ui.reagent :as ui]))

(defn- get-styles
  []
  {:list-item {:padding-top    8
               :padding-bottom 8}
   :checkbox  {:padding 6}})

(defn- lessons-list-item
  [{:keys [name selected? on-select on-deselect] :as lesson}]
  (let [handle-click (fn []
                       (if selected?
                         (on-deselect lesson)
                         (on-select lesson)))
        styles (get-styles)]
    [ui/list-item {:button   true
                   :style    (:list-item styles)
                   :on-click handle-click}
     [ui/list-item-text name]
     [ui/checkbox {:disable-ripple true
                   :style          (:checkbox styles)
                   :checked        selected?}]]))

(defn lessons-list
  [{:keys [lessons on-select on-deselect]}]
  (let []
    [ui/list
     (doall (for [{:keys [id] :as lesson} lessons]
              ^{:key id}
              [lessons-list-item (merge lesson
                                        {:on-select   on-select
                                         :on-deselect on-deselect})]))]))
