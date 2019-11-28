(ns webchange.editor-v2.diagram.modes.phrases.widget-data
  (:require
    [camel-snake-kebab.core :refer [->Camel_Snake_Case]]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.events :as ee]
    [webchange.editor-v2.diagram.diagram-model.custom-nodes.custom-widget.colors :refer [colors]]
    [webchange.editor-v2.diagram.diagram-model.custom-nodes.custom-widget.widget-header :as custom-header]
    [webchange.editor-v2.diagram.diagram-model.custom-nodes.custom-widget.widget-wrapper :as custom-wrapper]))

(defn phrase-node?
  [node-data]
  (contains? (:data node-data) :phrase))

(defn get-node-color
  [node-data]
  (if (phrase-node? node-data)
    "#6BC784"
    (:default colors)))

(defn header
  [node-data]
  (if (phrase-node? node-data)
    (let [phrase (-> node-data
                     (get-in [:data :phrase])
                     (->Camel_Snake_Case)
                     (clojure.string/replace "_" " "))]
      [:div
       [:h3 {:style {:margin      0
                     :padding     5
                     :text-align  "center"
                     :white-space "nowrap"}}
        phrase]])
    [custom-header/header node-data]))

(defn wrapper
  [{:keys [node-data]}]
  (let [this (r/current-component)]
    (into [:div {:on-double-click #(re-frame/dispatch [::ee/show-translator-form node-data])
                 :style           (merge custom-wrapper/node-style
                                         {:background-color (get-node-color node-data)})}]
          (r/children this))))

(defn get-widget-data
  []
  {:header                header
   :wrapper               wrapper
   :get-node-custom-color get-node-color})
