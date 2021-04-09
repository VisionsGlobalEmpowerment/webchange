(ns webchange.book-creator.text-form.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.book-creator.text-form.state :as text-form-state]
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
                                                                           :font-size   (get-in text [:data :font-size])
                                                                           :font-family (get-in text [:data :font-family])}
                                                      :text-object-name   (get-in text [:name])
                                                      :dialog-action-name (keyword action)
                                                      :phrase-action-path phrase-action-path}])
               handle-save #(re-frame/dispatch [::state/save id])]
    [:div.text-form
     [:div.font-controls
      [font-family-component {:id id}]
      [font-size-component {:id id}]]
     [:div.text-control-wrapper
      [text-component {:id id}]
      [voice-over-button {:id id}]]
     [:div.buttons-block
      [button {:variant  "contained"
               :color    "primary"
               :size     "big"
               :on-click handle-save}
       "Apply"]]]
    (finally
      (re-frame/dispatch [::state/reset id]))))

(defn text-form
  []
  (let [{:keys [text] :as text-object-data} @(re-frame/subscribe [::text-form-state/current-text-object-data])]
    (when (some? text-object-data)
      ^{:key (:name text)}
      [text-form-view text-object-data])))
