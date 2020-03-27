(ns webchange.editor-v2.layout.views
  (:require
    [webchange.editor-v2.concepts.views :refer [delete-dataset-item-modal]]
    [webchange.editor-v2.layout.toolbar.views :refer [toolbar]]
    [webchange.editor-v2.translator.views-modal :refer [translator-modal]]))

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
  [props & children]
  (let [styles (get-styles)]
    [:div {:style (:main-container styles)}
     [:div {:style (:toolbar-container styles)}
      [toolbar (select-keys props [:title])]]
     [:div {:style (:content-container styles)}
      children]
     [modal-windows]]))
