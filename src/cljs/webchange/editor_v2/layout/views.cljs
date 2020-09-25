(ns webchange.editor-v2.layout.views
  (:require
    [webchange.editor-v2.concepts.views :refer [delete-dataset-item-modal]]
    [webchange.editor-v2.layout.toolbar.views :refer [toolbar]]
    [webchange.editor-v2.translator.views-modal :refer [translator-modal]]
    [webchange.editor-v2.dialog.views-modal :refer [dialog-modal]]
    [webchange.editor-v2.translator.text.views :refer [configuration-modal]]
    [reagent.core :as r]))

(defn- get-styles
  []
  {:main-container             {:height         "100%"
                                :display        "flex"
                                :flex-direction "column"}
   :toolbar-container          {:height    "64px"
                                :flex-grow 0}
   :content-container          {:padding   "32px"
                                :flex-grow 1
                                :overflow  "auto"}
   :content-container-centered {:display         "flex"
                                :justify-content "center"
                                :align-items     "start"}})

(defn- modal-windows
  []
  [:div
   [dialog-modal]
   [translator-modal]
   [configuration-modal]
   [delete-dataset-item-modal]])

(defn layout
  [{:keys [align]
    :or   {align "left"}}]
  (let [this (r/current-component)
        styles (get-styles)
        content-styles (cond-> (:content-container styles)
                               (= align "center") (merge (:content-container-centered styles)))]
    [:div {:style (:main-container styles)}
     [:div {:style (:toolbar-container styles)}
      [toolbar (select-keys (r/props this) [:title :breadcrumbs])]]
     (into [:div {:style content-styles}]
           (r/children this))
     [modal-windows]]))
