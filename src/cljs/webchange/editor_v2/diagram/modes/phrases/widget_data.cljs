(ns webchange.editor-v2.diagram.modes.phrases.widget-data
  (:require
    [camel-snake-kebab.core :refer [->Camel_Snake_Case]]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.events :as ee]
    [webchange.editor-v2.diagram.diagram-model.custom-nodes.custom-widget.colors :refer [colors]]
    [webchange.editor-v2.diagram.diagram-model.custom-nodes.custom-widget.widget-wrapper :as custom-wrapper]
    [webchange.editor-v2.graph-builder.utils.node-data :refer [object-node?
                                                               phrase-node?
                                                               trigger-node?]]
    [webchange.editor-v2.utils :refer [str->caption
                                       keyword->caption]]))

(defn get-node-color
  [node-data]
  (cond
    (phrase-node? node-data) (get-in colors [:action :phrase])
    (object-node? node-data) (get-in colors [:object :default])
    (trigger-node? node-data) (get-in colors [:global-object :default])
    :else (:default colors)))

(defn- phrase-header
  [node-data]
  (let [phrase (-> node-data
                   (get-in [:data :phrase])
                   (keyword->caption))
        description (get-in node-data [:data :phrase-description])]
    [:div
     [:h3 {:style {:margin      0
                   :padding     5
                   :text-align  "center"
                   :white-space "nowrap"}}
      phrase]
     [:p {:style {:margin     0
                  :padding    "5px"
                  :text-align "center"
                  :max-width  "200px"}}
      description]]))

(defn- not-phrase-header
  [node-data]
  [:div
   [:h3 {:style {:margin      0
                 :padding     5
                 :text-align  "center"
                 :white-space "nowrap"}}
    (str->caption (:name node-data))]])

(defn- header
  [node-data]
  (if (phrase-node? node-data)
    [phrase-header node-data]
    [not-phrase-header node-data]))

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
