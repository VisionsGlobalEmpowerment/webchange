(ns webchange.editor-v2.course-table.fields.tags.views-edit-restriction
  (:require
    [cljs-react-material-ui.icons :as ic]
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.course-table.fields.tags.state :as tags-state]))

(defn- tag-item
  [{:keys [tag variant on-click on-delete]
    :or   {variant "outlined"}}]
  [ui/chip (cond-> {:label      tag
                    :variant    variant
                    :class-name "tag-chip"
                    :style      {:margin    "4px"
                                 :min-width "32px"}}
                   (some? on-click) (assoc :on-click #(on-click tag))
                   (some? on-delete) (-> (assoc :on-delete #(on-delete tag))
                                         (update-in [:style] assoc :padding-right "14px")))])

(defn- add-tag-form
  [{:keys [on-save]}]
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
                     :style       {:border-bottom "solid 1px #616161"
                                   :position      "relative"
                                   :top           "-2px"
                                   :margin-left   "4px"
                                   :width         "80px"}
                     :InputProps  {:style {:font-size "0.815rem"}}}]]))

(defn tags-restriction-form
  [{:keys [component-id]}]
  (r/with-let [edit? (r/atom false)
               handle-add-click (fn [] (reset! edit? true))
               handle-delete-click (fn [tag] (re-frame/dispatch [::tags-state/remove-selected-restriction tag component-id]))
               handle-save-click (fn [tag]
                                   (re-frame/dispatch [::tags-state/add-restriction-tag tag component-id])
                                   (reset! edit? false))]
    (let [tags @(re-frame/subscribe [::tags-state/tags-restriction component-id])]
      [:div
       (for [tag tags]
         ^{:key tag}
         [tag-item {:tag       tag
                    :on-delete handle-delete-click}])
       (if @edit?
         [add-tag-form {:on-save handle-save-click}]
         [tag-item {:tag      "+"
                    :variant  "default"
                    :on-click handle-add-click}])])))
