(ns webchange.dashboard.students.student-modal.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [cljs-react-material-ui.icons :as ic]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.common.core :as common]
    [webchange.dashboard.common.form-controls :as wui]
    [webchange.subs :as ws]
    [webchange.ui.theme :refer [w-colors]]
    [webchange.validation.specs.student :as spec]
    [webchange.dashboard.classes.subs :as classes-subs]
    [webchange.dashboard.students.events :as students-events]
    [webchange.dashboard.students.subs :as students-subs]))

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

(defn- student-form
  [props]
  (let [show-code (r/atom (-> @props :access-code boolean))]
    (fn [props]
      (let [classes @(re-frame/subscribe [::classes-subs/classes-list])
            generated-code @(re-frame/subscribe [::students-subs/generated-code])
            form-errors @(re-frame/subscribe [::ws/entity-errors :student])
            access-code (or generated-code (:access-code @props))
            _ (swap! props assoc :access-code access-code)
            date-of-birth (common/format-date-string (:date-of-birth @props))]
        [:form
         [ui/grid {:container true}
          [ui/grid {:item true :xs 12}
           [wui/select-validated
            {:label        "Class"
             :value        (or (:class-id @props) "")
             :on-change    #(swap! props assoc :class-id (->> % .-target .-value))
             :spec         ::spec/class-id
             :custom-error (:class-id form-errors)}
            (for [class classes]
              (class->menu-item class))]]
          [ui/grid {:item true :xs 12}
           [wui/select-validated
            {:label        "Gender"
             :value        (or (:gender @props) "")
             :on-change    #(swap! props assoc :gender (->> % .-target .-value))
             :spec         ::spec/gender
             :custom-error (:gender form-errors)}
            (for [gender genders]
              (gender->menu-item gender))]]
          [ui/grid {:item true :xs 12}
           [wui/text-field-validated {:label         "First Name"
                                      :default-value (:first-name @props)
                                      :required      true
                                      :spec          ::spec/first-name
                                      :custom-error  (:first-name form-errors)
                                      :on-change     #(swap! props assoc :first-name (->> % .-target .-value))}]]
          [ui/grid {:item true :xs 12}
           [wui/text-field-validated {:label         "Last Name"
                                      :default-value (:last-name @props)
                                      :required      true
                                      :spec          ::spec/last-name
                                      :custom-error  (:last-name form-errors)
                                      :on-change     #(swap! props assoc :last-name (->> % .-target .-value))}]]
          [ui/grid {:item true :xs 12}
           [wui/text-field-validated
            {:required        true
             :label           "Date of Birth"
             :type            "date"
             :default-value   date-of-birth
             :on-change       #(swap! props assoc :date-of-birth (->> % .-target .-value))
             :InputLabelProps {:shrink true}
             :spec            ::spec/date-of-birth
             :custom-error    (:date-of-birth form-errors)}]]
          [ui/grid {:item true :xs 12}
           [ui/form-control {:margin "normal" :full-width true}
            [ui/text-field
             {:label         "Access-code"
              :auto-complete "new-password"
              :type          (if @show-code "text" "password")
              :value         (or (:access-code @props) "")
              :InputProps    {:end-adornment (r/as-element [ui/input-adornment
                                                            {:position "end"}
                                                            [ui/icon-button
                                                             {:on-click #(swap! show-code not)}
                                                             (if @show-code [ic/visibility-off] [ic/visibility])]
                                                            [ui/icon-button
                                                             {:on-click #(re-frame/dispatch [::students-events/generate-access-code])}
                                                             [ic/update]]])}}]]]
          (if-let [other-errors (:other form-errors)]
            [ui/grid {:item true :xs 12}
             [:ul {:style {:color (:secondary w-colors)}}
              (map (fn [error] ^{:key error} [:li error]) other-errors)]
             ])]]))))

(defn student-modal
  []
  (let [class-id @(re-frame/subscribe [::classes-subs/current-class-id])
        {{first-name :first-name last-name :last-name} :user :as student} @(re-frame/subscribe [::students-subs/current-student])
        student-modal-state @(re-frame/subscribe [::students-subs/student-modal-state])
        student-data (r/atom (assoc student :first-name first-name
                                            :last-name last-name
                                            :class-id (if (= :add student-modal-state)
                                                        class-id
                                                        (:class-id student))))
        handle-save (if (= :edit student-modal-state)
                      (fn [student-data] (re-frame/dispatch [::students-events/edit-student (:class-id student-data) (:id student-data) student-data]))
                      (fn [student-data] (re-frame/dispatch [::students-events/add-student (:class-id student-data) student-data])))
        handle-close #(re-frame/dispatch [::students-events/close-student-modal])
        loading @(re-frame/subscribe [::students-subs/student-loading])]
    [ui/dialog
     {:open     (boolean student-modal-state)
      :on-close handle-close}
     [ui/dialog-title
      (case student-modal-state
        :add "Add New Student"
        :edit "Edit Student"
        "")]
     [ui/dialog-content
      (if loading
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
        :on-click #(handle-save @student-data)}
       "Save"]]]))

(defn student-remove-from-class-modal
  []
  (let [modal-state @(re-frame/subscribe [::students-subs/remove-from-class-modal-state])
        is-loading? @(re-frame/subscribe [::students-subs/student-loading])
        {{first-name :first-name last-name :last-name} :user student-id :id class-id :class-id} @(re-frame/subscribe [::students-subs/current-student])]
    (when modal-state
      [ui/dialog {:open true}
       (if is-loading?
         [ui/linear-progress]
         [:div
          [ui/dialog-title "Are you sure?"]
          [ui/dialog-content
           [ui/dialog-content-text (str "You are about to remove " first-name " " last-name " from its class")]]
          [ui/dialog-actions
           [ui/button {:on-click #(re-frame/dispatch [::students-events/close-remove-from-class-modal])} "Cancel"]
           [ui/button
            {:variant  "contained"
             :color    "primary"
             :on-click #(re-frame/dispatch [::students-events/confirm-remove class-id student-id])}
            "Confirm"]]])
       ])))

(defn student-delete-modal
  []
  (let [modal-state @(re-frame/subscribe [::students-subs/delete-modal-state])
        is-loading? @(re-frame/subscribe [::students-subs/student-loading])
        {{first-name :first-name last-name :last-name} :user student-id :id} @(re-frame/subscribe [::students-subs/current-student])]
    (when modal-state
      [ui/dialog {:open true}
       (if is-loading?
         [ui/linear-progress]
         [:div
          [ui/dialog-title "Are you sure?"]
          [ui/dialog-content
           [ui/dialog-content-text (str "You are about to delete " first-name " " last-name)]]
          [ui/dialog-actions
           [ui/button {:on-click #(re-frame/dispatch [::students-events/close-remove-from-class-modal])} "Cancel"]
           [ui/button
            {:variant  "contained"
             :color    "primary"
             :on-click #(re-frame/dispatch [::students-events/confirm-delete student-id])}
            "Confirm"]]])
       ])))

(defn student-complete-modal
  []
  (r/with-let [lesson (r/atom nil)]
    (let [modal-state @(re-frame/subscribe [::students-subs/complete-modal-state])
          is-loading? @(re-frame/subscribe [::students-subs/student-loading])
          {{first-name :first-name last-name :last-name} :user student-id :id} @(re-frame/subscribe [::students-subs/current-student])]
      (when modal-state
        [ui/dialog {:open true}
         (if is-loading?
           [ui/linear-progress]
           [:div
            [ui/dialog-title "Are you sure?"]
            [ui/dialog-content
             [ui/dialog-content-text (str "You are about to complete progress for " first-name " " last-name)]
             [ui/text-field
              {:label         "Lesson to complete"
               :type          "text"
               :helper-text   "Leave blank to complete all lessons"
               :on-change #(reset! lesson (-> % .-target .-value js/parseInt))}]]
            [ui/dialog-actions
             [ui/button {:on-click #(re-frame/dispatch [::students-events/close-complete-modal])} "Cancel"]
             [ui/button
              {:variant  "contained"
               :color    "primary"
               :on-click #(re-frame/dispatch [::students-events/confirm-complete student-id @lesson])}
              "Confirm"]]])
         ]))))