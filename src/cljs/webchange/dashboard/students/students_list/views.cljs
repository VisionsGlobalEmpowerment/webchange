(ns webchange.dashboard.students.students-list.views
  (:require
    [cljsjs.material-ui]
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [reagent.core :as r]
    [webchange.dashboard.students.common.actions-menu :refer [actions-menu]]
    [webchange.dashboard.students.common.check-icon :refer [check-icon]]))

(defn translate
  [path]
  (get-in {:items   {:age    {:title       "Age"
                              :not-defined "---"}
                     :class  {:title "Class"}
                     :course {:title       "Course"
                              :not-defined "Course is not defined"}
                     :tablet {:title "Tablet"}}
           :actions {:edit   "Edit"
                     :remove "Remove"}}
          path))

(def no-defined-color "#c1c1c1")
(def avatar-male-color "#70adff")
(def avatar-female-color "#ff7070")
(def avatar-unknown-color no-defined-color)

(def styles
  {:content {:container {:display     "flex"
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
                         :white-space   "nowrap"}}})

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
  [{:keys [title text style text-style]} children]
  [:div
   {:style (merge (get-in styles [:content :container]) style)}
   (when title
     [:span {:style (get-in styles [:content :title])}
      (str title ": ")])
   (when text
     [:span {:title text
             :style (merge (get-in styles [:content :text]) text-style)}
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
  [{:keys [on-click on-edit-click on-remove-click]}
   {:keys [first-name last-name age gender class course tablet?] :as student}]
  [ui/list-item
   {:align-items "flex-start"
    :button      true
    :on-click    #(on-click student)}
   [list-item-avatar {:first-name first-name
                      :last-name  last-name
                      :gender     gender}]
   [list-item-content {:text  (str first-name " " last-name)
                       :style {:width 200}}]
   [list-item-content {:title      (translate [:items :age :title])
                       :text       (or age (translate [:items :age :not-defined]))
                       :style      {:width 60}
                       :text-style (if-not age {:color no-defined-color} {})}]
   [list-item-content {:title (translate [:items :class :title])
                       :text  class
                       :style {:width 100}}]
   [list-item-content {:title      (translate [:items :course :title])
                       :text       (or course (translate [:items :course :not-defined]))
                       :style      {:width 200}
                       :text-style (if-not course {:color no-defined-color} {})}]
   [list-item-content {:title   (translate [:items :tablet :title
                                            ])
                       :content tablet?
                       :style   {:width 100}}
    [check-icon {:value tablet?}]]
   [actions-menu
    {:on-edit-click   #(on-edit-click student)
     :on-remove-click #(on-remove-click student)}]]
  )

(defn students-list
  [{:keys [style] :as props} students]
  [ui/list {:style style}
   (for [student students]
     ^{:key (:id student)}
     [list-item props student])])
