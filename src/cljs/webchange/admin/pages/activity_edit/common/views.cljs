(ns webchange.admin.pages.activity-edit.common.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.admin.pages.activity-edit.common.state :as state]
    [webchange.admin.widgets.page.views :as page]
    [webchange.ui.index :as ui]))

(defn content-wrapper
  [{:keys [class-name]}]
  (let [loading? @(re-frame/subscribe [::state/activity-loading?])]
    [page/page {:class-name    (ui/get-class-name {"page--activity-edit" true
                                                   class-name            (some? class-name)})
                :align-content "center"}
     (into [page/content {:class-name   "page--activity-edit--content"
                          :transparent? true}]
           (if loading?
             [[ui/loading-overlay]]
             (->> (r/current-component)
                  (r/children))))]))
