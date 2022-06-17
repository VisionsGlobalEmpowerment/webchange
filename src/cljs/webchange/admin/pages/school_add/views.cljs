(ns webchange.admin.pages.school-add.views
  (:require [re-frame.core :as re-frame]
            [webchange.admin.pages.school-add.state :as state]
            [webchange.admin.widgets.page.views :as page]
            [webchange.admin.widgets.school-form.views :refer [add-school-form]]
            [webchange.ui-framework.components.index :as ui]))

(defn- header
  []
  (let [handle-close-click #(re-frame/dispatch [::state/open-school-list])]
    [page/header {:title    "Add School"
                  :icon     "school"
                  :on-close handle-close-click}]))

(defn page
  []
  [page/page {:class-name "page--add-school"}
   [header]
   [page/main-content {:class-name "add-school-content"}
    [add-school-form {:class-name "school-form"
                      :on-save    #(re-frame/dispatch [::state/open-school-profile %])}]]])
