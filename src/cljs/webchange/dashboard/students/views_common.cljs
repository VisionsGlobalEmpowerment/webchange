(ns webchange.dashboard.students.views-common
  (:require
    [cljsjs.material-ui]
    [cljs-react-material-ui.reagent :as ui]
    [reagent.core :as r]))

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

(defn- student-form
  []
  (let [errors (r/atom {:password nil})]
    (fn [props]
      [:form
       [ui/text-field {:hint-text     "First Name"
                       :default-value (:first-name @props)
                       :on-change     #(swap! props assoc :first-name (->> % .-target .-value))}]
       [ui/text-field {:hint-text     "Last Name"
                       :default-value (:last-name @props)
                       :on-change     #(swap! props assoc :last-name (->> % .-target .-value))}]
       [ui/text-field {:hint-text     "Email"
                       :default-value (:email @props)
                       :on-change     #(swap! props assoc :email (->> % .-target .-value))}]
       [ui/text-field {:hint-text     "Password"
                       :type          "password"
                       :default-value nil
                       :error-text    (:password @errors)
                       :on-change     #(do (swap! props assoc :password (->> % .-target .-value))
                                           (check-form props errors))}]
       [ui/text-field {:hint-text     "Confirm Password"
                       :type          "password"
                       :default-value nil
                       :error-text    (:password @errors)
                       :on-change     #(do (swap! props assoc :confirm-password (->> % .-target .-value))
                                           (check-form props errors))}]])))

(defn student-modal
  [student-data {:keys [title modal-open handle-save handle-close]}]
  [ui/dialog {:title            (case title
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
              :open             modal-open
              :on-request-close #(handle-close)}
   [student-form student-data]])
