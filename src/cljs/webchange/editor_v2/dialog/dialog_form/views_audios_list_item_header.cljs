(ns webchange.editor-v2.dialog.dialog-form.views-audios-list-item-header
  (:require
    [reagent.core :as r]
    [webchange.editor-v2.dialog.dialog-form.views-audios-list-item-data :refer [audio-data audio-data-form]]
    [webchange.ui-framework.components.index :refer [circular-progress menu]]))

(defn- get-styles
  []
  {:block-header {:display         "flex"
                  :justify-content "space-between"
                  :padding         "0 0 8px 0"}})

(defn header
  [{:keys [alias target on-change-data on-bring-to-top on-delete on-clear-selection loading?]}]
  (r/with-let [edit-state? (r/atom false)]
    (let [styles (get-styles)
          handle-edit #(reset! edit-state? true)
          handle-save #(do (reset! edit-state? false)
                           (on-change-data %))
          handle-cancel #(reset! edit-state? false)
          handle-bring-to-top #(on-bring-to-top)
          handle-delete on-delete
          handle-clear-selection on-clear-selection]
      [:div {:style (:block-header styles)}
       (if @edit-state?
         [audio-data-form {:alias     alias
                           :target    target
                           :on-save   handle-save
                           :on-cancel handle-cancel}]
         [audio-data {:alias  alias
                      :target target}])
       (if loading?
         [circular-progress {:color "secondary"}]
         (when-not @edit-state?
           [menu {:items [{:icon     "bring-to-top"
                           :text     "Bring To Top"
                           :on-click handle-bring-to-top}
                          {:icon     "edit"
                           :text     "Name Recording"
                           :on-click handle-edit}
                          {:icon     "clear"
                           :text     "Clear selection"
                           :on-click handle-clear-selection}
                          {:icon     "remove"
                           :text     "Delete"
                           :confirm  "Remove audio asset from scene?"
                           :on-click handle-delete}]}]))])))