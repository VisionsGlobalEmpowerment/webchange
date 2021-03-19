(ns webchange.book-creator.pages.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.book-creator.views-content-block :refer [content-block]]
    [webchange.editor-v2.layout.components.activity-action.state :as scene-action.events]
    [webchange.editor-v2.layout.components.activity-stage.state :as stage-state]
    [webchange.ui-framework.components.utils :refer [get-class-name]]
    [webchange.ui-framework.index :refer [icon]]))

(defn get-stage-name
  [{:keys [idx]}]
  (let [index (inc idx)
        number (if (< index 10) (str "0" index) (str index))]
    (str number ". Spread")))

(defn- stage-item
  [{:keys [idx img active?] :as stage}]
  (let [handle-click (fn [] (re-frame/dispatch [::stage-state/select-stage idx]))]
    [:div {:on-click   handle-click
           :class-name (get-class-name {"stage-item" true
                                        "active"     active?})}
     [:div.img-container
      (if (some? img)
        [:img {:src img}]
        [icon {:icon "image"}])]
     [:span (get-stage-name stage)]]))

(defn- add-stage-button
  []
  (let [add-stage-action-name :add-page
        handle-click (fn [] (re-frame/dispatch [::scene-action.events/show-actions-form add-stage-action-name]))]
    [:button.add-stage-button {:on-click handle-click}
     [icon {:icon "add"}]]))

(defn- stages-list
  []
  (let [stages @(re-frame/subscribe [::stage-state/stage-options])]
    [:div.stages-list
     (for [{:keys [idx] :as stage} stages]
       ^{:key idx}
       [stage-item stage])
     [add-stage-button]]))

(defn pages-block
  []
  [content-block {:title "Pages"}
   [stages-list]])