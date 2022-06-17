(ns webchange.ui.pages.buttons.views
  (:require
    [webchange.ui.index :as ui]
    [webchange.ui.pages.layout :refer [layout panel]]))

(def states [{}
             {:state "hover"}
             {:state "active"}
             {:loading? true}
             {:disabled? true}])

(def variants-1 [{}
                 {:chip "plus"}
                 {:icon "restore"}
                 {:color     "yellow-2"
                  :icon      "dnd"
                  :icon-side "left"}
                 {:color      "blue-2"
                  :chip       "plus"
                  :chip-color "green-2"
                  :shape      "rounded"}])

(def variants-2 [{:icon "restore"}
                 {:color "yellow-2"
                  :icon  "dnd"}
                 {:color "green-2"
                  :icon  "close"}])

(defn- component-1
  [props]
  [ui/button (assoc props :title (js/JSON.stringify (clj->js props))) "Name"])

(defn- component-2
  [props]
  [ui/button (assoc props :title (js/JSON.stringify (clj->js props)))])

(defn- variant-states
  [{:keys [component variant variant-idx]}]
  [:<>
   (for [[idx state] (map-indexed vector states)]
     ^{:key (str "variant-" variant-idx "--" "state-" idx)}
     [component (merge variant state)])])

(defn page
  []
  [:div#page--buttons
   [layout {:title "Buttons"}
    [:h2 "Text Buttons"]
    [panel {:class-name "buttons-panel"}
     (for [[idx variant] (map-indexed vector variants-1)]
       ^{:key (str "variant-" idx)}
       [variant-states {:variant     variant
                        :variant-idx idx
                        :component   component-1}])]
    [:h2 "Icon Buttons"]
    [panel {:class-name "buttons-panel"}
     (for [[idx variant] (map-indexed vector variants-2)]
       ^{:key (str "variant-" idx)}
       [variant-states {:variant     variant
                        :variant-idx idx
                        :component   component-2}])]]])