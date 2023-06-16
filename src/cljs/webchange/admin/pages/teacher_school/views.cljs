(ns webchange.admin.pages.teacher-school.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.teacher-school.state :as state]
    [webchange.admin.widgets.page.views :as page]
    [webchange.admin.pages.school-profile.views :as school-profile]
    [webchange.admin.widgets.school-form.views :refer [add-school-form]]
    [webchange.ui.index :as ui]))

(defn- init-school-form
  []
  [page/single-page {:class-name        "page--add-school"
                     :header            {:title    "Add School"
                                         :icon     "school"}
                     :background-image? true
                     :form-container?   true}
   [add-school-form {:class-name "school-form"
                     :on-save    #(re-frame/dispatch [::state/open-school-profile %])}]])

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn [_]
    (if-let [current-school @(re-frame/subscribe [::state/current-school])]
      [school-profile/page {:school-id current-school}]
      [init-school-form])))
