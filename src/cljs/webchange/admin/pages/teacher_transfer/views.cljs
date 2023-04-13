(ns webchange.admin.pages.teacher-transfer.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.admin.pages.teacher-transfer.state :as state]
    [webchange.admin.widgets.page.views :as page]
    [webchange.ui.index :as ui]))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn [_]
    (let [email-value @(re-frame/subscribe [::state/email-value])
          error-value @(re-frame/subscribe [::state/error-value])]
      [page/single-page {:class-name        "page--transfer-teacher"
                         :header            {:title    "Transfer Teacher to School"
                                             :icon     "teachers"
                                             :on-close #(re-frame/dispatch [::state/open-teachers-list])}
                         :background-image? true
                         :form-container?   true}
       [:div.transfer-teacher-form
        [ui/input {:label       "Email"
                   :placeholder "Teacher email"
                   :required?   true
                   :value       email-value
                   :on-change   #(re-frame/dispatch [::state/set-email %])}]
        [ui/input-error error-value]
        [ui/button {:class-name "apply-button"
                    :on-click   #(re-frame/dispatch [::state/apply])}
         "Apply"]]])))
