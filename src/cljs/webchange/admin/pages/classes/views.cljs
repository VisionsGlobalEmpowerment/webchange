(ns webchange.admin.pages.classes.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.classes.state :as state]
    [webchange.admin.components.list.views :as l]
    [webchange.admin.widgets.no-data.views :refer [no-data]]
    [webchange.admin.widgets.page.views :as page]
    [webchange.ui-framework.components.index :as c]))

(defn- header
  []
  (let [school-name @(re-frame/subscribe [::state/school-name])
        handle-add-click #(re-frame/dispatch [::state/add-class])]
    [page/header {:title   school-name
                  :icon    "school"
                  :actions [{:text     "New Class"
                             :icon     "add"
                             :on-click handle-add-click}]}]))

(defn- list-item
  [{:keys [id stats] :as props}]
  (let [handle-edit-click #(re-frame/dispatch [::state/edit-class id])
        handle-remove-click #(re-frame/dispatch [::state/remove-class id])]
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
      [:div.class-stats
       [c/icon {:icon "students"}]
       (:students stats)]]]))

(defn- content
  []
  (let [classes-data @(re-frame/subscribe [::state/classes-list-data])]
    [page/main-content {:title "Classes"}
     [l/list {:class-name "classes-list"}
      (for [{:keys [id] :as class-data} classes-data]
        ^{:key id}
        [list-item class-data])]]))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn []
    [page/page {:class-name "page--classes"}
     [header]
     [content]]))
