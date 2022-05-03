(ns webchange.admin.pages.school-profile.views
  (:require 
    [reagent.core :as r]
    [re-frame.core :as re-frame]
    [webchange.admin.widgets.page.views :as page]
    [webchange.admin.widgets.counter.views :refer [counter]]
    [webchange.admin.widgets.no-data.views :refer [no-data]]
    [webchange.admin.pages.school-profile.state :as state]
    [webchange.ui-framework.components.index :refer [button input circular-progress]]))

(defn- school-counter
  []
  (let [{:keys [stats]} @(re-frame/subscribe [::state/school-data])]
    [counter {:items [{:id     :teachers
                       :value  (:teachers stats)
                       :title  "Teachers"
                       :action {:title "Teachers"
                                :icon  "add"}}
                      {:id     :students
                       :value  (:students stats)
                       :title  "Students"
                       :action {:title "Students"
                                :icon  "add"}}
                      {:id     :courses
                       :value  (:courses stats)
                       :title  "Courses"
                       :action {:title "Courses"
                                :icon  "add"}}
                      {:id     :classes
                       :value  (:classes stats)
                       :title  "Classes"
                       :action {:title "Classes"
                                :icon  "add"}}]}]))

(defn- statistics
  []
  [page/block {:title "Statistics"}
   [no-data]])

(defn- school-form
  [initial-data]
  (r/with-let [data (r/atom initial-data)]
    (let [errors @(re-frame/subscribe [::state/errors])
          handle-save-click #(re-frame/dispatch [::state/edit-school @data])]
      [:<>
       [input {:label "School Name"
               :error (when (:name errors)
                        (:name errors))
               :value (:name @data)
               :on-change #(swap! data assoc :name %)}]
       [input {:label "Location"
               :error (when (:location errors)
                        (:location errors))
               :value (:location @data)
               :on-change #(swap! data assoc :location %)}]
       [input {:label "About"
               :error (when (:about errors)
                        (:about errors))
               :value (:about @data)
               :on-change #(swap! data assoc :about %)}]
       [button {:on-click  handle-save-click}
        "Save"]])))

(defn- school-info
  []
  (let [school-data @(re-frame/subscribe [::state/school-data])]
    [page/block {:title "School info"}
     (if (empty? school-data)
       [circular-progress]
       [school-form school-data])]))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn []
    [page/page
     [page/main-content {:title  "School Profile"}
      [school-counter]
      [statistics]]
     [page/side-bar
      [school-info]]]))
