(ns webchange.editor-v2.dialog.dialog-text-form.side-menu.sections.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.dialog.dialog-text-form.side-menu.state :as state]
    [webchange.ui-framework.components.index :as components]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn- section-item
  [{:keys [section on-click]}]
  (let [{:keys [name icon selected?]} section
        handle-click #(on-click section)]
    [components/icon {:icon       icon
                      :class-name (get-class-name {"menu-section-item" true
                                                   "selected"          selected?})
                      :title      name
                      :on-click   handle-click}]))

(defn- sections-group
  [{:keys [data on-click]}]
  [:div.menu-section-group
   (for [{:keys [id] :as section} data]
     ^{:key id}
     [section-item {:section  section
                    :on-click on-click}])])

(defn sections
  []
  (let [available-sections @(re-frame/subscribe [::state/available-sections])
        handle-section-click #(re-frame/dispatch [::state/set-current-section %])]
    [:div.side-menu-sections
     (for [[idx group] (map-indexed vector available-sections)]
       ^{:key idx}
       [sections-group {:data     group
                        :on-click handle-section-click}])]))
