(ns webchange.ui.pages.switches.views
  (:require
    [reagent.core :as r]
    [webchange.ui.index :as ui]
    [webchange.ui.pages.layout :refer [layout panel]]))

(def states [{}
             {:state "hover"}
             {:indeterminate? true}
             {:disabled? true}])

(def variants [{:checked? true
                :label    "Active"}
               {:checked? false
                :label    "Inactive"}])

(defn- variant-states
  [{:keys [component variant variant-idx]}]
  [:<>
   (for [[idx state] (map-indexed vector states)]
     ^{:key (str "variant-" variant-idx "--" "state-" idx)}
     [component (merge variant state)])])

(defn- component
  [props]
  [ui/switch (assoc props :title (js/JSON.stringify (clj->js props)))])

(defn page
  []
  [:div#page-switches
   [layout {:title "Switches"}
    [:h2 "States"]
    [panel {:class-name "switched-panel"}
     (for [[idx variant] (map-indexed vector variants)]
       ^{:key (str "variant-" idx)}
       [variant-states {:variant     variant
                        :variant-idx idx
                        :component   component}])]
    [:h2 "Interactive"]
    [panel {:class-name "switched-panel"}
     (r/with-let [checked? (r/atom true)
                  handle-change #(swap! checked? not)]
       [ui/switch {:label     (if @checked? "Active" "Inactive")
                   :checked?  @checked?
                   :on-change handle-change}])]]])
