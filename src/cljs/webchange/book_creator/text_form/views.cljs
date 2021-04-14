(ns webchange.book-creator.text-form.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.book-creator.asset-form.views :refer [asset-form]]
    [webchange.book-creator.state :as state-book-creator]
    [webchange.book-creator.text-form.views-font-family :refer [font-family-component]]
    [webchange.book-creator.text-form.views-font-size :refer [font-size-component]]
    [webchange.book-creator.text-form.views-text :refer [text-component]]
    [webchange.book-creator.text-form.views-voice-over-button :refer [voice-over-button]]
    [webchange.editor-v2.layout.flipbook.page-text.state :as state]
    [webchange.ui-framework.components.index :refer [button]]))

(defn- text-form-view
  [{:keys [action phrase-action-path text]}]
  (r/with-let [id (-> (random-uuid) str)
               _ (re-frame/dispatch [::state/init id {:data               {:text        (get-in text [:data :text])
                                                                           :chunks      (get-in text [:data :chunks])
                                                                           :font-size   (get-in text [:data :font-size])
                                                                           :font-family (get-in text [:data :font-family])}
                                                      :text-object-name   (get-in text [:name])
                                                      :dialog-action-name (keyword action)
                                                      :phrase-action-path phrase-action-path}])
               handle-save #(re-frame/dispatch [::state/save id])]
    (let [loading? @(re-frame/subscribe [::state/loading? id])
          has-changes? @(re-frame/subscribe [::state/has-changes? id])]
      [asset-form {:top-controls  [[font-family-component {:id id}]
                                   [font-size-component {:id id}]]
                   :on-save-click handle-save
                   :disabled?     (or loading? (not has-changes?))
                   :class-name    "text-form"}
       [text-component {:id id}]
       [voice-over-button {:id id}]])
    (finally
      (re-frame/dispatch [::state/reset id]))))

(defn text-form
  []
  (let [{:keys [text] :as object-data} @(re-frame/subscribe [::state-book-creator/current-object-data])]
    (when (some? text)
      ^{:key (:name text)}
      [text-form-view object-data])))
