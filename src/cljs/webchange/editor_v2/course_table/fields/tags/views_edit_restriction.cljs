(ns webchange.editor-v2.course-table.fields.tags.views-edit-restriction
  (:require
    [cljs-react-material-ui.icons :as ic]
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.course-table.fields.tags.state :as tags-state]))

(defn- tag-item
  [{:keys [data on-click]}]
  (let [{:keys [tag selected?]} data]
    [ui/chip {:label    tag
              :color    (if selected? "secondary" "default")
              :style    {:margin    "4px"
                         :min-width "32px"}
              :on-click #(on-click data)}]))

(defn- add-tag-form
  [{:keys [on-save on-cancel]}]
  (r/with-let [value (r/atom "")
               handle-save (fn [] (on-save @value))
               handle-change (fn [e] (reset! value (.. e -target -value)))
               handle-key-down (fn [e]
                                 (.stopPropagation e)
                                 (let [key-code (.-keyCode e)
                                       enter-code 13]
                                   (when (= key-code enter-code)
                                     (handle-save))))]
    [:div {:style {:display "inline-block"}}
     [ui/text-field {:value       @value
                     :auto-focus  true
                     :placeholder "new-tag"
                     :on-change   handle-change
                     :on-key-down handle-key-down
                     :style       {:background-color "#616161"
                                   :border-radius    "16px"
                                   :padding          "3px 12px"
                                   :width            "120px"}
                     :InputProps  {:style {:font-size "0.815rem"}}}]
     [ui/icon-button {:style    {:padding "7px"}
                      :on-click on-cancel}
      [ic/clear {:style {:font-size "18px"}}]]
     [ui/icon-button {:color    "secondary"
                      :style    {:padding "7px"}
                      :on-click handle-save}
      [ic/done {:style {:font-size "18px"}}]]]))

(defn tags-restriction-form
  []
  (r/with-let [edit? (r/atom false)
               handle-add-click (fn [] (reset! edit? true))
               handle-save-click (fn [tag]
                                   (re-frame/dispatch [::tags-state/add-restriction-tag tag])
                                   (reset! edit? false))
               handle-cancel-click (fn [] (reset! edit? false))]
    (let [tags @(re-frame/subscribe [::tags-state/tags-restriction])
          handle-tag-click (fn [{:keys [tag selected?]}]
                             (if selected?
                               (re-frame/dispatch [::tags-state/remove-selected-restriction tag])
                               (re-frame/dispatch [::tags-state/add-selected-restriction tag])))]
      [:div {:style {:margin-top "16px"}}
       (for [{:keys [tag] :as tag-data} tags]
         ^{:key tag}
         [tag-item {:data     tag-data
                    :on-click handle-tag-click}])
       (if @edit?
         [add-tag-form {:on-save   handle-save-click
                        :on-cancel handle-cancel-click}]
         [tag-item {:data     {:tag "+"}
                    :on-click handle-add-click}])])))
