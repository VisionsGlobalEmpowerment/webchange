(ns webchange.dashboard.classes.class-modal.views
  (:require
   [cljs-react-material-ui.reagent :as ui]
   [re-frame.core :as re-frame]
   [reagent.core :as r]
   [webchange.dashboard.classes.subs :as classes-subs]
   [webchange.dashboard.students.subs :as students-subs]
   [webchange.dashboard.classes.events :as classes-events]
   [webchange.subs :as subs]))

;; ---------------------- My Work ------------------

(def courses
  [{:id 4 :name "english"}
   {:id 2 :name "spanish"}])

(defn- get-styles
  []
  {:main        {:width          "120px"
                 :margin         "0 20px"}
   :div-course  {:margin-bottom  "5px"
                 :margin-top     "30px"}
   :spn-course  {:margin-left    "20px"
                 :font-family    "Roboto, Helvetica, Arial, sans-serif"
                 :color          "#595959"}})

(defn- menu-item
  [{:keys [id name]}]
  [ui/menu-item {:key id :value name}
   [ui/typography {:inline  true
                   :variant "inherit"}
    name]])

(defn my-filter [str-input return-value filter-key]
  ((keyword return-value) (first
                           (filter (comp #{str-input} (keyword filter-key)) courses))))

(defn course-selector [props flag course]
  (let [styles (get-styles)]
    (if (= :add flag)
      (swap! props assoc :course-id
             (js/parseInt (my-filter @course "id" "name"))))
    [:div {:style  (:div-course styles)}
     [:span {:style  (:spn-course styles)} "Course"]
     [ui/select {:value     (if (= :edit flag)
                              (my-filter (:course-id @props) "name" "id")
                              @course)
                 :variant   "outlined"
                 :on-change (fn [e]
                              (reset! course (.-value (.-target e)))
                              (swap! props assoc :course-id
                                     (js/parseInt (my-filter (.-value (.-target e)) "id" "name"))))
                 :style     (:main styles)}
      (for [course courses]
        (menu-item course))]]))
;; ---------------------- My work ends --------------

(defn- class-form-inputs
  [props]
  [ui/text-field
   {:label         "Class Name"
    :default-value (:name @props)
    :on-change     #(swap! props assoc :name (->> % .-target .-value))}])

(defn class-modal
  []
  (let [current-class @(re-frame/subscribe [::classes-subs/current-class])
        class-data (r/atom current-class)
        class-modal-state @(re-frame/subscribe [::classes-subs/class-modal-state])
        handle-save (if (= :edit class-modal-state)
                      (fn [class-data current-course] (re-frame/dispatch [::classes-events/edit-class (:id class-data) class-data]))
                      (fn [class-data current-course] (re-frame/dispatch [::classes-events/add-class class-data current-course])))
        handle-close #(re-frame/dispatch [::classes-events/close-class-modal])
        loading @(re-frame/subscribe [:loading])
        current-course @(re-frame/subscribe [::subs/current-course])
        default-course (r/atom current-course)]
    [ui/dialog
     {:open     (boolean class-modal-state)
      :on-close handle-close}
     [:form
      [ui/dialog-title
       (case class-modal-state
         :add "Add New Class"
         :edit "Edit Class"
         "")]
      [ui/dialog-content
       (if (:class loading)
         [ui/circular-progress
          {:size  80
           :color "secondary"}]
         [:div
          [class-form-inputs class-data]
          [course-selector class-data class-modal-state default-course]])]
      [ui/dialog-actions
       [ui/button
        {:on-click handle-close}
        "Cancel"]
       [ui/button
        {:type     "submit"
         :variant  "contained"
         :color    "primary"
         :on-click #(do (.preventDefault %)
                        (handle-save @class-data)
                        (handle-close))}
        "Save"]]]]))

(defn class-delete-modal
  []
  (let [modal-state @(re-frame/subscribe [::classes-subs/delete-modal-state])
        class-id @(re-frame/subscribe [::classes-subs/current-class-id])
        students @(re-frame/subscribe [::students-subs/class-students class-id])
        is-loading? @(re-frame/subscribe [::students-subs/students-loading class-id])]
    (when modal-state
      [ui/dialog {:open true}
       (if is-loading?
         [ui/linear-progress]
         [:div
          [ui/dialog-title "Are you sure?"]
          [ui/dialog-content
           [ui/dialog-content-text "You are about to delete this class"]
           (when (not-empty students)
             [ui/dialog-content-text "This class contains students. Please, delete students first."])]
          [ui/dialog-actions
           [ui/button {:on-click #(re-frame/dispatch [::classes-events/close-delete-modal])} "Cancel"]
           [ui/button
            {:variant  "contained"
             :color    "primary"
             :disabled (boolean (not-empty students))
             :on-click #(re-frame/dispatch [::classes-events/confirm-delete class-id])}
            "Confirm"]]])])))
