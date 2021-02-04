(ns webchange.editor-v2.components.page-layout.views
  (:require
    [reagent.core :as r]
    [webchange.editor-v2.concepts.views :refer [delete-dataset-item-modal]]
    [webchange.editor-v2.components.toolbar.views :refer [toolbar]]
    [webchange.editor-v2.translator.views-modal :refer [translator-modal]]
    [webchange.editor-v2.dialog.views-modal :refer [dialog-modal]]
    [webchange.editor-v2.question.views-modal :refer [question-modal]]
    [webchange.editor-v2.translator.text.views-text-chunks-editor :refer [configuration-modal]]
    [webchange.ui.utils :refer [deep-merge]]))

(defn- get-styles
  []
  {:main-container             {:height         "100%"
                                :display        "flex"
                                :flex-direction "column"}
   :toolbar-container          {:height      "64px"
                                :flex-grow   0
                                :flex-shrink 0}
   :content-container          {:padding   "32px"
                                :flex-grow 1
                                :overflow  "auto"}
   :content-container-centered {:display         "flex"
                                :justify-content "center"
                                :align-items     "start"}})

(defn- modal-windows
  []
  [:div
   [question-modal]
   [dialog-modal]
   [translator-modal]
   [configuration-modal]
   [delete-dataset-item-modal]])

(defn layout
  [{:keys [align content-ref styles]
    :or   {align       "left"
           content-ref #()
           styles      {}}}]
  (let [this (r/current-component)
        styles (-> (get-styles)
                   (deep-merge styles))
        content-styles (cond-> (:content-container styles)
                               (= align "center") (merge (:content-container-centered styles)))]
    [:div {:style (:main-container styles)}
     [:div {:style (:toolbar-container styles)}
      [toolbar (select-keys (r/props this) [:title :breadcrumbs])]]
     (into [:div {:style content-styles
                  :ref   content-ref}]
           (r/children this))
     [modal-windows]]))
