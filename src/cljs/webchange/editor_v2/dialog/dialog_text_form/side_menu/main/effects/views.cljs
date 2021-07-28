(ns webchange.editor-v2.dialog.dialog-text-form.side-menu.main.effects.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.dialog.dialog-text-form.side-menu.main.common.section-block.views :refer [section-block]]
    [webchange.editor-v2.dialog.dialog-text-form.side-menu.main.common.options-list.views :refer [options-list]]
    [webchange.editor-v2.dialog.dialog-text-form.side-menu.main.effects.state :as state]
    [webchange.ui-framework.components.index :refer [icon-button]]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(def event-type
  [{:id 1 :title "Default" :type "default"}
   {:id 2 :title "Image" :type "image"}
   {:id 3 :title "Questions" :type "question"}])

(defn- get-current-effects-list
  [{:keys [available-effects effect-type]}]
  (filter (fn [{:keys [text value selected? type]}]
            (if (= "default" effect-type)
              (or (= nil type) (= effect-type type))
              (= effect-type type)))
          available-effects))

(defn- effects-list
  [{:keys [effect-type]}]
  (let [available-effects @(re-frame/subscribe [::state/available-effects])
        current-effects-list (get-current-effects-list {:available-effects available-effects :effect-type effect-type})
        handle-click #(re-frame/dispatch [::state/set-selected-effect %])]
    (if (not-empty current-effects-list)
      [:div
       [:span.input-label "Available effects:"]
       [options-list {:options      current-effects-list
                      :on-click      handle-click
                      :get-drag-data (fn [{:keys [value]}]
                                       {:action "add-effect-action"
                                        :id     value})}]]
      [:div
       [:span.input-label "No Available effects"]])))

(defn- actions
  []
  (let [selected-effect @(re-frame/subscribe [::state/selected-effect])
        available-actions [["Before" "insert-before" #(re-frame/dispatch [::state/add-current-effect-action :before])]
                           ["After" "insert-after" #(re-frame/dispatch [::state/add-current-effect-action :after])]
                           ["Parallel" "insert-parallel" #(re-frame/dispatch [::state/add-current-effect-action :parallel])]]]
    (when (some? selected-effect)
      [:div.actions
       [:span.input-label "Add:"]
       (for [[idx [text icon handler]] (map-indexed vector available-actions)]
         ^{:key idx}
         [icon-button
          {:icon     icon
           :size     "small"
           :on-click handler}
          text])])))

(defn- actions-placeholder
  []
  [:div.placeholder
   [:span "Drag and drop available effect"]
   [:span "or"]
   [:span "Select effect and phrase action to insert effect relative to phrase"]])

(defn form
  []
  (let [show-actions? @(re-frame/subscribe [::state/show-actions?])]
    [:div.effects-form
     (for [{:keys [id title type]} event-type]
       ^{:key id}
       [section-block {:title title}
        [effects-list {:effect-type type}]
        (if show-actions?
          [actions]
          [actions-placeholder])])]))
