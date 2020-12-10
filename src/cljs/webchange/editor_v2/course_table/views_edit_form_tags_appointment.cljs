(ns webchange.editor-v2.course-table.views-edit-form-tags-appointment
  (:require
    [cljs-react-material-ui.icons :as ic]
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.course-table.state.edit-tags :as tags-state]))

(defn- input
  [{:keys [value on-change type style enabled?]
    :or   {on-change #()}}]
  [ui/text-field (cond-> {:value     value
                          :variant   "outlined"
                          :on-change #(on-change (.. % -target -value))}
                         (some? style) (update :style merge style)
                         (not enabled?) (-> (assoc :disabled true)
                                            (dissoc :variant)
                                            (update :style merge {:padding "10px 19px"}))
                         (some? type) (assoc :type type))])

(defn- tag-form
  [{:keys [data edit-mode?]}]
  (let [{:keys [tag score-low score-high]} @data
        handle-edit-score-low #(swap! data assoc :score-low %)
        handle-edit-score-high #(swap! data assoc :score-high %)
        handle-edit-name #(swap! data assoc :tag %)]
    [:div
     [input {:value     tag
             :on-change handle-edit-name
             :enabled?  edit-mode?
             :style     {:width "150px"}}]
     [input {:value     score-low
             :type      "number"
             :on-change handle-edit-score-low
             :enabled?  edit-mode?
             :style     {:width       "80px"
                         :margin-left "16px"}}]
     [input {:value     score-high
             :type      "number"
             :on-change handle-edit-score-high
             :enabled?  edit-mode?
             :style     {:width       "80px"
                         :margin-left "16px"}}]]))

(defn- actions
  [{:keys [data]}]
  [:div {:style {:margin-left "16px"}}
   (for [[idx {:keys [icon on-click color]
               :or   {color "default"}}] (map-indexed vector data)]
     ^{:key idx}
     [ui/icon-button {:on-click on-click
                      :color    color}
      [icon {:style {:font-size "18px"}}]])])

(defn- tag-row
  [{:keys [tag] :as tag-data}]
  (r/with-let [edit-mode? (r/atom false)
               data (r/atom tag-data)
               handle-edit-click (fn [] (reset! edit-mode? true))
               handle-save-click (fn []
                                   (re-frame/dispatch [::tags-state/edit-tag-appointment tag @data])
                                   (reset! edit-mode? false))
               handle-delete-click (fn [] (re-frame/dispatch [::tags-state/delete-tag-appointment tag]))
               handle-cancel-click (fn []
                                     (reset! data tag-data)
                                     (reset! edit-mode? false))]
    [ui/list-item {:style {:padding "0"}}
     [tag-form {:data       data
                :edit-mode? @edit-mode?}]
     (if @edit-mode?
       [actions {:data [{:icon ic/clear :on-click handle-cancel-click}
                        {:icon ic/done :on-click handle-save-click :color "secondary"}]}]
       [actions {:data [{:icon ic/edit :on-click handle-edit-click}
                        {:icon ic/delete :on-click handle-delete-click}]}])]))

(defn tags-appointment-form
  []
  (let [tags @(re-frame/subscribe [::tags-state/tags-appointment])
        handle-add (fn [] (re-frame/dispatch [::tags-state/add-tag-appointment]))]
    [:div {:style {:text-align "right"}}
     [ui/list {:style {:padding "16px 0"}}
      (for [{:keys [tag] :as tag-data} tags]
        ^{:key tag}
        [tag-row tag-data])]
     [ui/button {:on-click handle-add
                 :color    "secondary"}
      "Add" [ic/add]]]))
