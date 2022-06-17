(ns webchange.ui.pages.buttons.views
  (:require
    [webchange.ui.index :as ui]
    [webchange.ui.pages.layout :refer [layout panel]]))

(def states [{}
             {:state "hover"}
             {:state "active"}
             {:loading? true}
             {:disabled? true}])

(def variants [{}
               {:chip "plus"}
               {:icon "restore"}
               {:color     "yellow-2"
                :icon      "dnd"
                :icon-side "left"}
               {:color      "blue-2"
                :chip       "plus"
                :chip-color "green-2"
                :shape      "rounded"}])

(defn- component
  [props]
  [ui/button (assoc props :title (js/JSON.stringify (clj->js props))) "Name"])

(defn- variant-states
  [{:keys [variant variant-idx]}]
  [:<>
   (for [[idx state] (map-indexed vector states)]
     ^{:key (str "variant-" variant-idx "--" "state-" idx)}
     [component (merge variant state)])])

(defn page
  []
  [:div#page--buttons
   [layout {:title "Buttons"}
    [panel {:class-name "buttons-panel"}
     (for [[idx variant] (map-indexed vector variants)]
       ^{:key (str "variant-" idx)}
       [variant-states {:variant     variant
                        :variant-idx idx}])]]])