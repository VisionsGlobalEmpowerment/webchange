(ns webchange.admin.pages.school-courses.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.admin.pages.school-courses.state :as state]
    [webchange.admin.widgets.page.views :as page]
    [webchange.ui.index :as ui]))

(defn- list-item
  [{:keys [id lang name]}]
  (let [removing? @(re-frame/subscribe [::state/course-removing? id])
        handle-remove-click #(re-frame/dispatch [::state/remove-course id])]
    [ui/list-item {:name    name
                   :info    [{:key   "Language"
                              :value (clojure.string/capitalize lang)}]
                   :actions [{:icon     "trash"
                              :title    "Remove"
                              :variant  "light"
                              :loading? removing?
                              :on-click handle-remove-click}]}]))

(defn- school-courses-list
  []
  (let [courses @(re-frame/subscribe [::state/courses])]
    [ui/list {:class-name "courses-list"}
     (for [{:keys [id] :as course-data} courses]
       ^{:key id}
       [list-item course-data])]))

(defn- assign-list-item
  [{:keys [id lang name selected?]}]
  (let [handle-click #(re-frame/dispatch [::state/select-available-course id (not selected?)])]
    [ui/list-item {:name    name
                   :info    [{:key   "Language"
                              :value (clojure.string/capitalize lang)}]
                   :actions [(cond-> {:icon     "check"
                                      :title    (if selected? "Deselect" "Select")
                                      :on-click handle-click}
                                     selected? (assoc :color "blue-1"))]}]))

(defn- assign-courses-list
  []
  (let [courses @(re-frame/subscribe [::state/available-course-options])]
    [ui/list {:class-name "courses-list"}
     (for [{:keys [id] :as course-data} courses]
       ^{:key id}
       [assign-list-item course-data])]))

(defn- footer
  []
  (let [selected-courses-number @(re-frame/subscribe [::state/selected-courses-number])
        loading? @(re-frame/subscribe [::state/assign-loading?])
        handle-add-click #(re-frame/dispatch [::state/assign-courses])]
    [:<>
     (str selected-courses-number " Courses Selected")
     [ui/button {:color     "yellow-1"
                 :icon      "plus"
                 :disabled? (= selected-courses-number 0)
                 :loading?  loading?
                 :on-click  handle-add-click}
      "Add To School"]]))

(defn page
  [props]
  (re-frame/dispatch [::state/init props])
  (fn []
    (let [school-name @(re-frame/subscribe [::state/school-name])
          courses-number @(re-frame/subscribe [::state/courses-number])
          show-assign-list? @(re-frame/subscribe [::state/show-assign-course-list?])
          handle-add-click #(re-frame/dispatch [::state/show-assign-course-list])
          handle-school-click #(re-frame/dispatch [::state/open-school-profile])
          handle-close-click #(re-frame/dispatch [::state/close-assign-course-list])]
      (print "show-assign-list?" show-assign-list?)
      [page/single-page (cond-> {:class-name "page--classes"
                                 :header     (cond-> {:title    school-name
                                                      :icon     "school"
                                                      :on-click handle-school-click
                                                      :stats    [{:icon       "courses"
                                                                  :icon-color "blue-2"
                                                                  :counter    courses-number
                                                                  :label      "Courses"}]
                                                      :actions  (cond-> []
                                                                        (not show-assign-list?) (conj {:text     "Assign Course"
                                                                                                       :icon     "plus"
                                                                                                       :on-click handle-add-click}))}
                                                     show-assign-list? (assoc :on-close handle-close-click))}
                                show-assign-list? (assoc :footer [footer]))
       (if show-assign-list?
         [assign-courses-list]
         [school-courses-list])])))
