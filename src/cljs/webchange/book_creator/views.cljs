(ns webchange.book-creator.views
  (:require
    [webchange.editor-v2.layout.components.interpreter_stage.views :as interpreter]
    [webchange.book-creator.text-form.views :refer [text-form]]))

(defn- stage
  []
  [:div.book-creator--stage
   {:style (interpreter/get-stage-size {:width 800})}
   [interpreter/stage]])

(defn- block-header
  [{:keys [title]}]
  [:div.block-header
   [:span.title title]])

(defn book-creator
  []
  [:div.book-creator
   [:div.main-content
    [block-header {:title "Layout"}]
    [stage]]
   [:div.side-block
    [text-form]]])
