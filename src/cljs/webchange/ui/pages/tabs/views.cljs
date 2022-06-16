(ns webchange.ui.pages.tabs.views
  (:require
    [webchange.ui.index :as ui]
    [webchange.ui.pages.layout :refer [layout panel]]))

(def states [{}
             {:state "hover"}
             {:state "active"}
             {:disabled? true}])

(def variants [{}
               {:action "plus"}
               {:align "center"}
               {:align  "center"
                :action "check"}
               {:icon    "create"
                :counter 10}])

(defn- component
  [props]
  [ui/tab (assoc props :title (js/JSON.stringify (clj->js props))) "Name"])

(defn- variant-states
  [{:keys [variant variant-idx]}]
  [:<>
   (for [[idx state] (map-indexed vector states)]
     ^{:key (str "variant-" variant-idx "--" "state-" idx)}
     [component (merge variant state)])])

(defn page
  []
  [:div#page--tabs
   [layout {:title "Tabs"}
    [panel {:color      "blue-1"
            :class-name "tabs-panel"}
     (for [[idx variant] (map-indexed vector variants)]
       ^{:key (str "variant-" idx)}
       [variant-states {:variant     variant
                        :variant-idx idx}])]]])