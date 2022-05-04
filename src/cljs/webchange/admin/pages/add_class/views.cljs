(ns webchange.admin.pages.add-class.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.admin.pages.add-class.state :as state]
    [webchange.admin.widgets.page.views :as page]
    [webchange.ui-framework.components.index :as ui]))

(defn- class-form
  []
  (r/with-let [data (r/atom {})]
    (let [errors @(re-frame/subscribe [::state/errors])
          handle-save-click #(re-frame/dispatch [::state/create-class @data])]
      [:<>
       [ui/input {:label "Class Name"
                  :error (when (:name errors)
                           (:name errors))
                  :value (:name @data)
                  :on-change #(swap! data assoc :name %)}]
       [ui/input {:label "Course"
                  :error (when (:course errors)
                           (:course errors))
                  :value (:course @data)
                  :on-change #(swap! data assoc :course %)}]
       [ui/button {:on-click  handle-save-click}
        "Save"]])))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn []
    [page/page
     [page/main-content {:title  "Add New Class"}
      [class-form]]]))
