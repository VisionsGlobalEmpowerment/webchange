(ns webchange.admin.pages.school-add.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.school-add.state :as state]
    [webchange.admin.widgets.page.views :as page]
    [webchange.admin.widgets.school-form.views :refer [add-school-form]]))

(defn page
  []
  (let [handle-close-click #(re-frame/dispatch [::state/open-school-list])]
    [page/single-page {:class-name        "page--add-school"
                       :header            {:title    "Add School"
                                           :icon     "school"
                                           :on-close handle-close-click}
                       :background-image? true
                       :form-container?   true}
     [add-school-form {:class-name "school-form"
                       :on-save    #(re-frame/dispatch [::state/open-school-profile %])}]]))
