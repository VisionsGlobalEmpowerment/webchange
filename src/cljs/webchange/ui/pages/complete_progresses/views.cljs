(ns webchange.ui.pages.complete-progresses.views
  (:require
    [webchange.ui.index :as ui]
    [webchange.ui.pages.layout :refer [layout panel]]))

(def states [{}])

(def variants [{:value 100}
               {:value 90}
               {:value 45}
               {:value 25}
               {:value 5}
               {:value 0}
               {:value 100
                :text  "IRA1 : Sleepy Mr. Sloth"}
               {:value   100
                :caption "21/04/2022"
                :text    "IRA1 : Sleepy Mr. Sloth"}])

(defn- component
  [props]
  [ui/complete-progress (assoc props :title (js/JSON.stringify (clj->js props)))])

(defn- variant-states
  [{:keys [component variant variant-idx]}]
  [:<>
   (for [[idx state] (map-indexed vector states)]
     ^{:key (str "variant-" variant-idx "--" "state-" idx)}
     [component (merge variant state)])])

(defn page
  []
  [:div#page--complete-progresses
   [layout {:title "Complete Progress"}
    [panel {:class-name "progresses-panel"}
     (for [[idx variant] (map-indexed vector variants)]
       ^{:key (str "variant-" idx)}
       [variant-states {:variant     variant
                        :variant-idx idx
                        :component   component}])]]])
