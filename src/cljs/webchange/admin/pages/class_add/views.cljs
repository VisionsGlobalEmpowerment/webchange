(ns webchange.admin.pages.class-add.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.class-add.state :as state]
    [webchange.admin.widgets.class-form.views :refer [class-add-form]]
    [webchange.admin.widgets.page.views :as page]))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn [{:keys [school-id]}]
    (let [open-classes-list #(re-frame/dispatch [::state/open-classes-list])]
      [page/single-page {:class-name        "page--class-add"
                         :header            {:title    "Add New Class"
                                             :icon     "classes"
                                             :on-close open-classes-list}
                         :background-image? true
                         :form-container?   true}
       [class-add-form {:school-id school-id
                        :on-save   open-classes-list}]])))
