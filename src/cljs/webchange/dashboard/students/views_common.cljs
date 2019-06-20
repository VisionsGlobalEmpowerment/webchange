(ns webchange.dashboard.students.views-common
  (:require
    [cljsjs.material-ui]
    [cljs-react-material-ui.reagent :as ui]
    [reagent.core :as r]
    [re-frame.core :as re-frame]
    [webchange.dashboard.classes.subs :as dcs]
    [webchange.dashboard.students.subs :as dss]
    [webchange.dashboard.students.events :as dse]
    [cljs-react-material-ui.icons :as ic]))

(def not-nil? (complement nil?))
(def not-equal (complement =))

(defn- check-password
  [props errors]
  (let [pass (:password @props)
        confirm-pass (:confirm-password @props)]
    (swap! errors assoc :password (if (and (not-nil? pass) (not-nil? confirm-pass) (not-equal pass confirm-pass))
                                    "Password and Confirm Password should be equal" ""))))

(defn- check-form
  [props errors]
  (check-password props errors))

(def genders [{:value 1 :text "Male"}
              {:value 2 :text "Female"}])

(defn gender->menu-item
  [{:keys [value text]}]
  [ui/menu-item
   {:key value :value value}
   text])

(defn class->menu-item
  [{:keys [id name]}]
  [ui/menu-item
   {:key id :value id}
   name])

(defn format-date [date]
  (let [pad (fn [number] (if (< number 10) (str "0" number) (str number)))
        year (.getUTCFullYear date)
        month (inc (.getUTCMonth date))
        day (.getUTCDate date)]
    (str year "-" (pad month) "-" (pad day))))

(defn- student-form
  []
  (let [classes @(re-frame/subscribe [::dcs/classes-list])
        show-code (r/atom false)]
    (fn [props]
      (let [generated-code @(re-frame/subscribe [::dss/generated-code])
            access-code (or generated-code (:access-code @props))
            _ (swap! props assoc :access-code access-code)
            date-of-birth (if (clojure.string/blank? (:date-of-birth @props)) (format-date (js/Date.)) (js/Date. (:date-of-birth @props)))]
        [:form
         [ui/grid {:container true}
          [ui/grid {:item true :xs 12}
           [ui/form-control {:margin "normal" :full-width true}
            [ui/input-label "Class"]
            [ui/select
             {:value     (or (:class-id @props) "")
              :on-change #(swap! props assoc :class-id (->> % .-target .-value))}
             (for [class classes]
               (class->menu-item class))]]]
          [ui/grid {:item true :xs 12}
           [ui/form-control {:margin "normal" :full-width true}
            [ui/input-label "Gender"]
            [ui/select
             {:value     (or (:gender @props) "")
              :on-change #(swap! props assoc :gender (->> % .-target .-value))}
             (for [gender genders]
               (gender->menu-item gender))]]]
          [ui/grid {:item true :xs 12}
           [ui/form-control {:margin "normal" :full-width true}
            [ui/text-field {:label         "First Name"
                            :default-value (:first-name @props)
                            :on-change     #(swap! props assoc :first-name (->> % .-target .-value))}]]]
          [ui/grid {:item true :xs 12}
           [ui/form-control {:margin "normal" :full-width true}
            [ui/text-field {:label         "Last Name"
                            :default-value (:last-name @props)
                            :on-change     #(swap! props assoc :last-name (->> % .-target .-value))}]]]
          [ui/grid {:item true :xs 12}
           [ui/form-control {:margin "normal" :full-width true}
            [ui/text-field
             {:label         "Date of Birth"
              :type          "date"
              :default-value date-of-birth
              :on-change     #(swap! props assoc :date-of-birth (->> % .-target .-value))}]]]
          [ui/grid {:item true :xs 8}
           [ui/form-control {:margin "normal" :full-width true}
            [ui/text-field {:label "Access-code"
                            :type  (if @show-code "text" "password")
                            :value (or (:access-code @props) "")}]]]
          [ui/grid {:item true :xs 1}
           [ui/form-control {:margin "normal"}
            [ui/icon-button {:on-click #(swap! show-code not)}
             (if @show-code [ic/visibility-off] [ic/visibility])]]]
          [ui/grid {:item true :xs 1}
           [ui/form-control {:margin "normal"}
            [ui/icon-button {:on-click #(re-frame/dispatch [::dse/generate-access-code])}
             [ic/update]]]]]]))))

(defn student-modal
  []
  (let [{{first-name :first-name last-name :last-name} :user :as student} @(re-frame/subscribe [::dss/current-student])
        student-data (r/atom (assoc student :first-name first-name :last-name last-name))
        student-modal-state @(re-frame/subscribe [::dss/student-modal-state])
        handle-save (if (= :edit student-modal-state)
                      (fn [student-data] (re-frame/dispatch [::dse/edit-student (:class-id student-data) (:id student-data) student-data]))
                      (fn [student-data] (re-frame/dispatch [::dse/add-student (:class-id student-data) student-data])))
        handle-close #(re-frame/dispatch [::dse/close-student-modal])
        loading @(re-frame/subscribe [:loading])]
    [ui/dialog
     {:open     (boolean student-modal-state)
      :on-close handle-close}
     [ui/dialog-title
      (case student-modal-state
        :add "Add New Student"
        :edit "Edit Student"
        "")]
     [ui/dialog-content
      (if (:student loading)
        [ui/circular-progress
         {:size  80
          :color "secondary"}]
        [student-form student-data])]
     [ui/dialog-actions
      [ui/button
       {:on-click handle-close}
       "Cancel"]
      [ui/button
       {:variant  "contained"
        :color    "primary"
        :on-click #(do (handle-save @student-data)
                       (handle-close))}
       "Save"]]]))
