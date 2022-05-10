(ns webchange.admin.pages.teacher-profile.views
  (:require [webchange.ui-framework.components.index :as ui]
            [re-frame.core :as re-frame]
            [reagent.core :as r]
            [webchange.admin.pages.teacher-profile.state :as state]
            [webchange.admin.widgets.page.views :as page]))

(def teacher-type-options
  [{:text "Admin"
    :value "admin"}
   {:text "Teacher"
    :value "teacher"}])

(defn- form
  [teacher]
  (r/with-let [data (r/atom teacher)]
    (let [errors @(re-frame/subscribe [::state/errors])
          handle-save-click #(re-frame/dispatch [::state/edit-teacher @data])]
      [:<>
       [ui/input {:label "First Name"
                  :error (when (:first-name errors)
                           (:first-name errors))
                  :value (:first-name @data)
                  :on-change #(swap! data assoc :first-name %)}]
       [ui/input {:label "Last Name"
                  :error (when (:last-name errors)
                           (:last-name errors))
                  :value (:last-name @data)
                  :on-change #(swap! data assoc :last-name %)}]
       [ui/input {:label "Password"
                  :type "password"
                  :error (when (:password errors)
                           (:password errors))
                  :value (:password @data)
                  :on-change #(swap! data assoc :password %)}]
       [ui/select {:placeholder "Teacher Type"
                   :value      (:type @data)
                   :options    teacher-type-options
                   :on-change  #(swap! data assoc :type %)
                   :class-name "type-input"
                   :variant    "outlined"
                   :error      (when (:type errors)
                                 (:type errors))}]
       [ui/button {:on-click  handle-save-click}
        "Save"]])))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn [props]
    (let [teacher @(re-frame/subscribe [::state/teacher])]
      [page/page
       [page/main-content {:title "Edit Teacher Account"}
        (if-not (empty? teacher)
          [form teacher]
          [ui/circular-progress])]])))
