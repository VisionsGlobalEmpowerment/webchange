(ns webchange.admin.pages.class-students-add.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.admin.pages.class-students-add.state :as state]
    [webchange.admin.widgets.add-class-students.views :refer [add-class-students]]
    [webchange.admin.widgets.page.views :as page]
    [webchange.ui.index :refer [get-class-name] :as ui]))

(defn page
  []
  (r/create-class
    {:display-name "Add Class Students"

     :component-did-mount
     (fn [this]
       (re-frame/dispatch [::state/init (r/props this)]))

     :component-will-unmount
     (fn [this]
       (re-frame/dispatch [::state/reset (r/props this)]))

     :reagent-render
     (fn [{:keys [class-id school-id]}]
       (let [class-name @(re-frame/subscribe [::state/class-name])
             selected-students-number @(re-frame/subscribe [::state/selected-students-number])
             handle-students-selected #(re-frame/dispatch [::state/set-selected-students %])
             handle-add-click #(re-frame/dispatch [::state/save])
             handle-open-class-students #(re-frame/dispatch [::state/open-class-students])]
         [page/single-page {:class-name "page--class-students-add"
                            :header     {:title    class-name
                                         :icon     "classes"
                                         :on-close handle-open-class-students}
                            :footer     (when (> selected-students-number 0)
                                          [:<>
                                           [:div (str selected-students-number " Accounts Selected")]
                                           [ui/button {:icon     "plus"
                                                       :on-click handle-add-click}
                                            "Add to Class"]])}
          [add-class-students {:class-id         class-id
                               :school-id        school-id
                               :show-actions?    false
                               :show-new-button? false
                               :on-change        handle-students-selected
                               :on-save          handle-open-class-students}]]))}))
