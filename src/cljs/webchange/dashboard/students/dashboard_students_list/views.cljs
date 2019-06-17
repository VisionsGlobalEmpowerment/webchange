(ns webchange.dashboard.students.dashboard-students-list.views
  (:require
    [cljsjs.material-ui]
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [reagent.core :as r]))

(defn translate
  [path]
  (get-in {:actions {:edit   "Edit"
                     :remove "Remove"}}
          path))

(def avatar-male-color "#70adff")
(def avatar-female-color "#ff7070")
(def avatar-unknown-color "#c1c1c1")
(def completed-mark-color "#3d9a00")
(def failed-mark-color "#fd4142")

(def styles
  {:content     {:container {:display     "flex"
                             :flex-grow   1
                             :flex-shrink 1
                             :height      "48px"
                             :line-height "24px"
                             :margin      "0 12px"
                             :padding     "12px"}
                 :title     {:display      "inline-block"
                             :flex-grow    0
                             :flex-shrink  0
                             :font-weight  "bold"
                             :margin-right "5px"
                             :overflow     "hidden"}
                 :text      {:display       "inline-block"
                             :flex-grow     1
                             :flex-shrink   1
                             :overflow      "hidden"
                             :text-overflow "ellipsis"
                             :white-space   "nowrap"}}
   :tablet-icon {:main      {:display  "inline-block"
                             :height   17
                             :position "relative"
                             :top      3}
                 :completed {:color completed-mark-color}
                 :failed    {:color failed-mark-color}}})

(defn list-item-avatar
  [{:keys [first-name last-name gender]}]
  [ui/list-item-avatar
   [ui/avatar
    {:style {:background-color (case gender
                                 1 avatar-male-color
                                 2 avatar-female-color
                                 avatar-unknown-color)}}
    (get first-name 0)
    (get last-name 0)]])

(defn list-item-content
  [{:keys [title text style]} children]
  [:div
   {:style (merge (get-in styles [:content :container]) style)}
   (when title
     [:span {:style (get-in styles [:content :title])}
      (str title ": ")])
   (when text
     [:span {:title text
             :style (get-in styles [:content :text])}
      text])
   (when children
     children)])

(defn list-item-actions
  [{:keys [menu-open? menu-anchor on-edit-click on-remove-click]}]
  [ui/list-item-secondary-action
   [ui/icon-button
    {:on-click #(do (reset! menu-open? true)
                    (reset! menu-anchor (.-currentTarget %)))}
    [ic/more-horiz]]
   [ui/menu
    {:open      @menu-open?
     :on-close  #(reset! menu-open? false)
     :anchor-El @menu-anchor}
    [ui/menu-item
     {:on-click #(do (on-edit-click)
                     (reset! menu-open? false))}
     (translate [:actions :edit])]
    [ui/menu-item
     {:on-click #(do (on-remove-click)
                     (reset! menu-open? false))}
     (translate [:actions :remove])]]])

(defn list-item
  [{:keys [on-edit-click on-remove-click]}
   {:keys [first-name last-name age gender class course tablet?] :as student}]
  (let [menu-anchor (r/atom nil)
        menu-open? (r/atom false)]
    (fn []
      [ui/list-item
       {:align-items "flex-start"
        :button      true}
       [list-item-avatar {:first-name first-name
                          :last-name  last-name
                          :gender     gender}]
       [list-item-content {:text  (str first-name " " last-name)
                           :style {:width 200}}]
       [list-item-content {:title "Age"
                           :text  age
                           :style {:width 60}}]
       [list-item-content {:title "Class"
                           :text  class
                           :style {:width 100}}]
       [list-item-content {:title "Course"
                           :text  course
                           :style {:width 200}}]
       [list-item-content {:title   "Tablet"
                           :content tablet?
                           :style   {:width 100}}
        (if tablet?
          [ic/check-circle {:style (merge (get-in styles [:tablet-icon :main])
                                          (get-in styles [:tablet-icon :completed]))}]
          [ic/remove-circle {:style (merge (get-in styles [:tablet-icon :main])
                                           (get-in styles [:tablet-icon :failed]))}])]
       [list-item-actions
        {:menu-open?      menu-open?
         :menu-anchor     menu-anchor
         :on-edit-click   #(on-edit-click student)
         :on-remove-click #(on-remove-click student)}]]))
  )

(defn students-list
  [props students]
  [ui/list
   (for [student students]
     ^{:key (:id student)}
     [list-item props student])])
