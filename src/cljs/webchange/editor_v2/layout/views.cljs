(ns webchange.editor-v2.layout.views
  (:require
    [webchange.editor-v2.concepts.views :refer [delete-dataset-item-modal]]
    [webchange.editor-v2.layout.toolbar.views :refer [toolbar]]
    [webchange.editor-v2.translator.views-modal :refer [translator-modal]]
    [reagent.core :as r]))

(defn- get-styles
  []
  {:main-container    {:height         "100%"
                       :display        "flex"
                       :flex-direction "column"}
   :toolbar-container {:height    "64px"
                       :flex-grow 0}
   :content-container {:padding   "32px"
                       :flex-grow 1}})

(defn- modal-windows
  []
  [:div
   [translator-modal]
   [delete-dataset-item-modal]])

(defn layout
  []
  (let [this (r/current-component)
        styles (get-styles)]
    [:div {:style (:main-container styles)}
     [:div {:style (:toolbar-container styles)}
      [toolbar (select-keys (r/props this) [:title :breadcrumbs])]]
     (into [:div {:style (:content-container styles)}]
           (r/children this))
     [modal-windows]]))
