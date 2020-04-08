(ns webchange.editor-v2.translator.views-modal
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor.events :as events]
    [webchange.editor-v2.components.confirm-dialog.views :refer [confirm-dialog]]
    [webchange.editor-v2.translator.events :as translator-events]
    [webchange.editor-v2.translator.subs :as translator-subs]
    [webchange.editor-v2.translator.translator-form.views-form :refer [translator-form]]
    [webchange.subs :as subs]))

(defn- get-styles
  [{:keys [progress-size]}]
  (let [progress-margin (-> (/ progress-size 2)
                            (Math/ceil)
                            (int))]
    {:save-button-wrapper {:position "relative"}
     :save-hover-progress {:position    "absolute"
                           :left        "50%"
                           :top         "50%"
                           :margin-left (str "-" progress-margin "px")
                           :margin-top  (str "-" progress-margin "px")}}))

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

(defn- save-scene-data
  [scene-id data-store]
  (doseq [[action-name action-data] (group-scene-updates data-store)]
    (re-frame/dispatch [::events/update-scene-action scene-id action-name action-data]))
  (re-frame/dispatch [::events/save-current-scene scene-id]))

(defn- save-concepts-data
  [data-store]
  (doseq [[id data-patch] (group-concepts-updates data-store)]
    (re-frame/dispatch [::events/update-dataset-item id data-patch])))

(defn save-actions-data!
  []
  (let [scene-id @(re-frame/subscribe [::subs/current-scene])
        data-store @(re-frame/subscribe [::translator-subs/phrase-translation-data])]
    (save-scene-data scene-id data-store)
    (save-concepts-data data-store)))

(def close-window! #(re-frame/dispatch [::translator-events/close-translator-modal]))

(defn translator-modal
  []
  (r/with-let [confirm-open? (r/atom false)]
              (let [open? @(re-frame/subscribe [::translator-subs/translator-modal-state])
                    data-store @(re-frame/subscribe [::translator-subs/phrase-translation-data])
                    blocking-progress? @(re-frame/subscribe [::translator-subs/blocking-progress])
                    handle-save #(do (save-actions-data!)
                                     (close-window!))
                    handle-close #(if (empty? data-store)
                                    (close-window!)
                                    (reset! confirm-open? true))
                    progress-size 18
                    styles (get-styles {:progress-size progress-size})]
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
                               :on-click handle-save
                               :disabled blocking-progress?}
                    "Save"]
                   (when blocking-progress?
                     [ui/circular-progress {:size  progress-size
                                            :style (:save-hover-progress styles)}])]]])))
