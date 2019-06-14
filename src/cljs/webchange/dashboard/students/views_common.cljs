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

(defn gender->menu-item [{:keys [value text]}]
  [ui/menu-item {:key value :value value :primary-text text}])

(defn class->menu-item [{:keys [id name]}]
  [ui/menu-item {:key id :value id :primary-text name}])

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
            date-of-birth (if (clojure.string/blank? (:date-of-birth @props)) nil (js/Date. (:date-of-birth @props)))]
        [:form
         [ui/select-field {:value (:class-id @props)
                           :hint-text     "Class"
                           :on-change    #(swap! props assoc :class-id %3)}
          (for [class classes]
            (class->menu-item class))]
         [ui/text-field {:hint-text     "First Name"
                         :default-value (:first-name @props)
                         :on-change     #(swap! props assoc :first-name (->> % .-target .-value))}]
         [ui/text-field {:hint-text     "Last Name"
                         :default-value (:last-name @props)
                         :on-change     #(swap! props assoc :last-name (->> % .-target .-value))}]
         [ui/select-field {:value (:gender @props)
                           :hint-text     "Gender"
                           :on-change    #(swap! props assoc :gender %3)}
          (for [gender genders]
            (gender->menu-item gender))]
         [ui/date-picker {:hint-text "Date of Birth"
                          :value date-of-birth
                          :container "inline"
                          :open-to-year-selection true
                          :on-change     #(swap! props assoc :date-of-birth (format-date %2))}]
         [ui/text-field {:hint-text     "Access-code"
                         :type          (if @show-code "text" "password")
                         :value (:access-code @props)}]
         [ui/icon-button {:on-click #(swap! show-code not)}
          (if @show-code [ic/action-visibility-off] [ic/action-visibility])]
         [ui/icon-button {:on-click #(re-frame/dispatch [::dse/generate-access-code])}
          [ic/action-update]]
         ]))))

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
    [ui/dialog {:title            (case student-modal-state
                                    :add "Add New Student"
                                    :edit "Edit Student"
                                    "")
                :actions          [(r/as-element [ui/flat-button {:default  true
                                                                  :on-click handle-close} "Cancel"])
                                   (r/as-element [ui/raised-button {:primary  true
                                                                    :on-click #(do (handle-save @student-data)
                                                                                   (handle-close))} "Save"])]
                :modal            false
                :content-style    {:width "400px"}
                :open             (boolean student-modal-state)
                :on-request-close handle-close}
     (if (:student loading)
       [ui/circular-progress {:size 80}]
       [student-form student-data])
     ]))

