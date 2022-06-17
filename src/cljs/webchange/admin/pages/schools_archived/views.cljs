(ns webchange.admin.pages.schools-archived.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.components.list.views :as l]
    [webchange.admin.pages.schools-archived.state :as state]
    [webchange.admin.widgets.page.views :as page]
    [webchange.ui-framework.components.index :as c]))

(defn- restore-button
  [{:keys [id]}]
  (let [handle-click #(do (.stopPropagation %)
                          (re-frame/dispatch [::state/restore-school id]))]
    [c/icon-button {:icon     "restore"
                    :title    "Restore School"
                    :variant  "light"
                    :on-click handle-click}]))

(defn school-item
  [{:keys [id] :as props}]
  [l/list-item (merge props
                      {:actions [restore-button {:id id}]})])

(defn page
  []
  (re-frame/dispatch [::state/init])
  (fn []
    (let [schools @(re-frame/subscribe [::state/schools-list])]
      [page/page {:class-name "page--schools-archived"}
       [page/_header {:title "Archived Schools"
                     :icon  "archive"}]
       [page/main-content
        [l/list {:class-name "schools-list"}
         (for [school schools]
           ^{:key (:id school)}
           [school-item school])]]])))
