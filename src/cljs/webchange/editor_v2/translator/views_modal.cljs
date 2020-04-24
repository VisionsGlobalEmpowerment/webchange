(ns webchange.editor-v2.translator.views-modal
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor.events :as events]
    [webchange.editor-v2.components.confirm-dialog.views :refer [confirm-dialog]]
    [webchange.editor-v2.translator.events :as translator-events]
    [webchange.editor-v2.translator.subs :as translator-subs]
    [webchange.editor-v2.translator.translator-form.audio-assets.subs :as assets-subs]
    [webchange.editor-v2.translator.translator-form.subs :as form-subs]
    [webchange.editor-v2.translator.translator-form.views-form :refer [translator-form]]
    [webchange.subs :as subs]))

(defn- get-styles
  []
  {:save-button-wrapper {:position "relative"}})

(defn- filter-data
  [filter-key data]
  (->> data
       (filter (fn [[_ {:keys [type]}]]
                 (= type filter-key)))))

(defn- group-concepts-updates
  [data-store]
  (->> data-store
       (filter-data :concept)
       (reduce (fn [result [[name id] {:keys [data]}]]
                 (update result id merge (assoc {} name data)))
               {})))

(defn- group-scene-updates
  [data-store]
  (->> data-store
       (filter-data :scene)
       (reduce (fn [result [[name] {:keys [data]}]]
                 (assoc result name data))
               {})))

(defn- save-scene-data!
  []
  (let [scene-id @(re-frame/subscribe [::subs/current-scene])
        assets @(re-frame/subscribe [::assets-subs/assets-data])
        data-store @(re-frame/subscribe [::form-subs/edited-data])]
    (doseq [[action-name action-data] (group-scene-updates (:actions data-store))]
      (re-frame/dispatch [::events/update-scene-action scene-id action-name action-data]))
    (re-frame/dispatch [::events/reset-audio-assets scene-id (vals assets)])
    (re-frame/dispatch [::events/save-current-scene scene-id])))

(defn- save-concepts-data!
  []
  (let [data-store @(re-frame/subscribe [::form-subs/edited-data])]
    (doseq [[id data-patch] (group-concepts-updates (:actions data-store))]
      (re-frame/dispatch [::events/update-dataset-item id data-patch]))))

(defn- save-edited-data!
  []
  (save-scene-data!)
  (save-concepts-data!))

(def close-window! #(re-frame/dispatch [::translator-events/close-translator-modal]))

(defn translator-modal
  []
  (r/with-let [confirm-open? (r/atom false)]
              (let [open? @(re-frame/subscribe [::translator-subs/translator-modal-state])
                    data-store @(re-frame/subscribe [::form-subs/edited-actions-data])
                    handle-save #(do (save-edited-data!)
                                     (close-window!))
                    handle-close #(if (empty? data-store)
                                    (close-window!)
                                    (reset! confirm-open? true))
                    styles (get-styles)]
                [ui/dialog
                 {:open       open?
                  :on-close   handle-close
                  :full-width true
                  :max-width  "xl"}
                 [ui/dialog-title
                  "Dialog Translation"]
                 [ui/dialog-content {:class-name "translation-form"}
                  (when open?
                    [translator-form])
                  [confirm-dialog {:open?       confirm-open?
                                   :on-confirm  handle-save
                                   :on-cancel   #(close-window!)
                                   :title       "Save changes?"
                                   :description "You are going to close translation window without changes saving."
                                   :save-text   "Save"
                                   :cancel-text "Discard"}]]
                 [ui/dialog-actions
                  [ui/button {:on-click handle-close}
                   "Cancel"]
                  [:div {:style (:save-button-wrapper styles)}
                   [ui/button {:color    "secondary"
                               :variant  "contained"
                               :on-click handle-save}
                    "Save"]]]])))
