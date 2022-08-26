(ns webchange.auth.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.auth.events :as auth.events]
    [webchange.auth.views-teacher-login :as teacher-login]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn- get-styles
  []
  {:title               {:color          "#ffffff"
                         :font-size      "40pt"
                         :font-family    "Roboto"
                         :user-select    "none"
                         :text-transform "uppercase"
                         :margin-bottom  "20px"}
   :student-access-form {:background-image "url(/images/dashboard/bg_02.jpg)"
                         :height           "100%"
                         :display          "flex"
                         :flex-direction   "column"
                         :justify-content  "space-between"
                         :padding          "40px"
                         :align-items      "center"}
   :button              {:padding          "30px"
                         :width            "101px"
                         :height           "101px"
                         :background-image "url(/images/auth/form.png)"
                         :font-size        "40pt"
                         :font-family      "Roboto"
                         :user-select      "none"
                         :display          "inline-block"
                         :line-height      "38px"}
   :button-container    {:padding    "15px"
                         :text-align "center"}
   :code-item           {:margin         "14px 30px"
                         :vertical-align "bottom"
                         :padding-bottom "14px"
                         :width          "38px"
                         :height         "38px"
                         :display        "inline-block"
                         :font-size      "40pt"
                         :line-height    "23px"}
   :num-pad             {:width "400px"}
   :bckspc-div          {:margin-left     "86px"
                         :display         "flex"
                         :align-items     "center"
                         :justify-content "space-between"}
   :bckspc-btn          {:background    "#fff"
                         :border-radius "40px"
                         :margin-left   "10px"
                         :height        "78px"
                         :width         "78px"
                         :padding       "18px 0px 0px 18px"
                         :cursor        "pointer"}
   :bckspc-img          {:width  "40px"
                         :height "40px"}})

(defn teacher-login
  [& args]
  (apply teacher-login/teacher-login-page args))

(defn enter-code [code-ratom symbol]
  (swap! code-ratom str symbol)
  (when (<= 4 (count @code-ratom))
    (re-frame/dispatch [::auth.events/student-login @code-ratom])
    (reset! code-ratom "")))

(defn- current-code-item
  [{:keys [value]}]
  [:div {:class-name (get-class-name {"current-code-item"        true
                                      "current-code-item--empty" (nil? value)})}
   (when (some? value)
     value)])

(defn- current-code
  [{:keys [code]}]
  (let [code-values (concat (clojure.string/split @code "")
                            (->> (repeat nil)
                                 (take (- 4 (count @code)))))
        handle-remove-click #(reset! code (subs @code 0 (- (count @code) 1)))]
    [:div {:class-name "current-code"}
     (for [[idx value] (map-indexed vector code-values)]
       ^{:key idx}
       [current-code-item {:value value}])
     [:button {:class-name "remove-button"
               :on-click   handle-remove-click}]]))

(defn- num-pad-key
  [{:keys [code value]}]
  (let [handle-click #(enter-code code value)]
    [:button {:on-click   handle-click
              :class-name (get-class-name {"num-pad-key"                     true
                                           (str "num-pad-key--value-" value) true})}
     value]))

(defn- num-pad
  [{:keys [code]}]
  [:div.num-pad
   (for [value [1 2 3 4 5 6 7 8 9 0]]
     ^{:key value}
     [num-pad-key {:code  code
                   :value value}])])

(defn- student-access-form
  []
  (r/with-let [code (r/atom "")]
    [:div {:class-name "student--sign-in-page"}
     [:div {:class-name "sign-in-form"}
      [:h1.header "Student Access"]
      [current-code {:code code}]
      [num-pad {:code code}]]]))
