(ns webchange.admin.pages.activities.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.components.list.views :as l]
    [webchange.admin.pages.activities.state :as state]
    [webchange.admin.widgets.page.views :as page]
    [webchange.ui-framework.components.index :as ui]))

#_(defn- courses-list-item
  [{:keys [name slug]}]
  (let [handle-edit-click #(re-frame/dispatch [::state/edit-course slug])]
    [l/list-item {:name    name
                  :actions [:<>
                            [ui/icon-button {:icon     "edit"
                                             :title    "Edit"
                                             :variant  "light"
                                             :on-click handle-edit-click}]]}]))

#_(defn- courses-list
  []
  (let [loading? @(re-frame/subscribe [::state/courses-loading?])
        courses @(re-frame/subscribe [::state/courses])]
    (if loading?
      [ui/loading-overlay]
      [l/list {:class-name "courses-list"}
       (for [{:keys [id] :as course} courses]
         ^{:key id}
         [courses-list-item course])])))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn []
    [page/page {:class-name "page--activities"}
     [page/header {:title "Activities"
                   :icon  "activity"}]
     [page/main-content
      ;[courses-list]
      ]]))