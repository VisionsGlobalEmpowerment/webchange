(ns webchange.editor-v2.dialog.dialog-form.views-audios-list-item-header
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [reagent.core :as r]
    [webchange.editor-v2.dialog.dialog-form.views-audios-list-item-data :refer [audio-data audio-data-form]]
    [webchange.editor-v2.dialog.dialog-form.views-audios-list-item-menu :refer [audio-menu]]))

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
         [ui/circular-progress {:size 20 :color "secondary"}]
         (when-not @edit-state?
           [audio-menu {:on-edit            handle-edit
                        :on-bring-to-top    handle-bring-to-top
                        :on-clear-selection handle-clear-selection
                        :on-delete          handle-delete}]))])))