(ns webchange.editor-v2.course-table.fields.tags.views-edit-appointment
  (:require
    [cljs-react-material-ui.icons :as ic]
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.course-table.fields.tags.state :as tags-state]))

(defn- input
  [{:keys [value on-change type style]
    :or   {on-change #()}}]
  [ui/text-field (cond-> {:value      value
                          :on-change  (fn [event]
                                        (on-change (cond->> (.. event -target -value)
                                                            (= type "number") (.parseInt js/Number))))
                          :style      {:border-bottom "solid 1px #616161"}
                          :InputProps {:style {:font-size   "0.8rem"
                                               :white-space "nowrap"}}}
                         (some? style) (update :style merge style)
                         (some? type) (assoc :type type)
                         (= type "number") (update-in [:InputProps :style] merge {:font-family "monospace"}))])

(defn- tag-row
  [{:keys [data on-edit on-delete]}]
  (let [{:keys [tag score-low score-high]} data
        handle-edit-score-low #(on-edit tag {:score-low %})
        handle-edit-score-high #(on-edit tag {:score-high %})
        handle-edit-name #(on-edit tag {:tag %})
        handle-delete #(on-delete tag)]
    [:li {:style {:min-width "230px"}}
     [input {:value     tag
             :on-change handle-edit-name
             :style     {:width "80px"}}]
     [input {:value     score-low
             :type      "number"
             :on-change handle-edit-score-low
             :style     {:width       "35px"
                         :margin-left "16px"}}]
     [input {:value     score-high
             :type      "number"
             :on-change handle-edit-score-high
             :style     {:width       "35px"
                         :margin-left "16px"}}]
     [ui/icon-button {:on-click handle-delete
                      :style    {:margin-left "16px"
                                 :padding     "8px"}}
      [ic/close {:style {:font-size "0.8rem"}}]]]))

(defn tags-appointment-form
  [{:keys [component-id]}]
  (let [tags @(re-frame/subscribe [::tags-state/tags-appointment component-id])
        handle-add (fn [] (re-frame/dispatch [::tags-state/add-tag-appointment component-id]))
        handle-delete-click (fn [tag] (re-frame/dispatch [::tags-state/delete-tag-appointment tag component-id]))
        handle-edit-appointment (fn [tag data] (re-frame/dispatch [::tags-state/edit-tag-appointment tag data component-id]))]
    [:div
     [:ul.tags-appointments
      (for [{:keys [tag] :as tag-data} tags]
        ^{:key tag}
        [tag-row {:data      tag-data
                  :on-edit   handle-edit-appointment
                  :on-delete handle-delete-click}])]

     [ui/icon-button {:on-click handle-add
                      :variant  "outlined"
                      :style    {:margin-left "206px"
                                 :padding     "8px"}}
      [ic/add {:style {:font-size "0.8rem"}}]]]))
