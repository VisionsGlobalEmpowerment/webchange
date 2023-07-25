(ns webchange.admin.pages.schools-archived.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.schools-archived.state :as state]
    [webchange.admin.widgets.page.views :as page]
    [webchange.ui.index :as ui]))

(defn school-item
  [{:keys [id name]}]
  (let [handle-click #(do (.stopPropagation %)
                          (re-frame/dispatch [::state/restore-school id]))]
    [ui/list-item {:name name
                   :actions [{:icon     "restore"
                              :title    "Restore school"
                              :on-click handle-click}]}]))

(defn page
  []
  (re-frame/dispatch [::state/init])
  (fn []
    (let [schools @(re-frame/subscribe [::state/schools-list])]
      [page/single-page {:class-name "page--schools-archived"
                         :header     {:title   "Archived Schools"
                                      :icon    "school"}}
       [ui/list {:class-name "schools-list"}
        (for [school schools]
          ^{:key (:id school)}
          [school-item school])]])))
