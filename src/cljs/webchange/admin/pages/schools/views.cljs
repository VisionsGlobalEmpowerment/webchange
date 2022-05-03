(ns webchange.admin.pages.schools.views
  (:require [re-frame.core :as re-frame]
            [webchange.admin.components.list.views :as l]
            [webchange.admin.pages.schools.state :as state]
            [webchange.admin.widgets.page.views :as page]
            [webchange.ui-framework.components.index :as c]))

(defn- header
  []
  (let [handle-add-click #(re-frame/dispatch [::state/add-school])]
    [page/header {:title   "Schools"
                  :icon    "school"
                  :actions [{:text     "New School"
                             :icon     "add"
                             :on-click handle-add-click}]}]))

(defn school-item
  [{:keys [id stats] :as props}]
  (let [handle-edit-click #(re-frame/dispatch [::state/edit-school id])
        handle-remove-click #(re-frame/dispatch [::state/remove-school id])]
    [l/list-item (merge props
                        {:actions [:<>
                                   [c/icon-button {:icon     "remove"
                                                   :title    "Remove"
                                                   :variant  "light"
                                                   :on-click handle-remove-click}]
                                   [c/icon-button {:icon     "edit"
                                                   :title    "Edit"
                                                   :variant  "light"
                                                   :on-click handle-edit-click}]]})
     [l/content-right
      [:div.school-stats
       [:div.school-stats
        [c/icon {:icon "users"}]
        (:students stats 0)]
       [:div.school-stats
        [c/icon {:icon "presentation"}]
        (:courses stats 0)]]]]))

(defn- content
  []
  (let [schools @(re-frame/subscribe [::state/schools-list])]
    [page/main-content {:title "Schools"}
     [l/list {:class-name "schools-list"}
      (for [school schools]
        ^{:key (:id school)}
        [school-item school])]]))

(defn page
  []
  (re-frame/dispatch [::state/init])
  (fn []
    [page/page {:class-name "page--schools"}
     [header]
     [content]]))
