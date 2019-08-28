(ns webchange.views-login-switch
  (:require
    [re-frame.core :as re-frame]
    [soda-ash.core :as sa]
    [webchange.events :as events]
    [webchange.interpreter.events :as ie]))

(def courses
  [{:key :test :value "test" :text "Español"}
   {:key :english :value "english" :text "English"}])

(defn login-switch
  []
  [sa/Grid {:centered       true
            :columns        2
            :container      true
            :vertical-align "middle"}
   [sa/GridRow {}
    [sa/GridColumn {}
     [sa/Segment {:placeholder true}
      [sa/Grid {:centered       true
                :vertical-align "middle"}
       [sa/GridRow {}
        [sa/GridColumn {:text-align "center"}
         [sa/Header {:as "h1" :content "Login as"}]]]
       [sa/GridRow {}
        [sa/GridColumn {}
         [sa/Grid {:stackable  true
                   :text-align "center"}
          [sa/Divider {:vertical true}
           "Or"]
          [sa/GridRow {:columns        2
                       :vertical-align "middle"}
           [sa/GridColumn {}
            [sa/Button {:basic    true
                        :on-click #(re-frame/dispatch [::events/redirect :login])}
             "Teacher"]]
           [sa/GridColumn {}
            [sa/Button {:basic    true
                        :on-click #(re-frame/dispatch [::events/redirect :student-login])}
             "Student"]]]]]]
       [sa/GridRow {}
        [sa/GridColumn {:text-align "center"}
           [sa/FormDropdown {:label "Course " :options courses :inline true :default-value "test"
                             :on-change #(re-frame/dispatch [::ie/set-current-course (.-value %2)])}]]]]]]]])
