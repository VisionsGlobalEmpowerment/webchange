(ns webchange.views-login-switch
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.db :refer [default-db]]
    [webchange.events :as events]
    [webchange.interpreter.events :as ie]
    [webchange.ui.theme :refer [get-in-theme]]))

(def courses
  [{:key :spanish :value "spanish" :text "Español"}
   {:key :english :value "english" :text "English"}
   {:key :demo :value "demo" :text "Demo"}])

(defn- get-styles
  []
  {:card             {:width    "450px"
                      :margin   "auto"
                      :position "relative"
                      :top      "30%"}
   :title            {:padding "20px 20px 30px 20px"}
   :main-content     {:margin-bottom "30px"}
   :divider          {:width        "50%"
                      :height       "100%"
                      :border-right "solid 1px"
                      :border-color (get-in-theme [:palette :border :default])}
   :button-container {:text-align "center"}
   :course-label     {:margin-right "15px"}
   :course-selector  {:width "120px"}
   :footer-row       {:text-align "center"
                      :padding    "10px"}})

(defn login-switch                                          ;; ToDo: Remove
  []
  (r/with-let [current-course (r/atom (:current-course default-db))]
    (let [translation-module-url (str "/courses/" @current-course "/editor-v2")
          styles (get-styles)]
      [ui/card {:style (:card styles)}
       [ui/card-content
        [ui/typography {:variant "h2"
                        :align   "center"
                        :style   (:title styles)}
         "Login as"]
        [ui/grid {:container true
                  :style     (:main-content styles)}
         [ui/grid {:item true :xs 5 :style (:button-container styles)}
          [ui/button {:on-click #(re-frame/dispatch [::events/redirect :login])} "Teacher"]]
         [ui/grid {:item true :xs 2}
          [:div {:style (:divider styles)}]]
         [ui/grid {:item true :xs 5 :style (:button-container styles)}
          [ui/button {:on-click #(re-frame/dispatch [::events/redirect :student-login])} "Student"]]]
        [ui/grid {:container true}
         [ui/grid {:item true :xs 12 :style (:footer-row styles)}
          [ui/typography {:variant "subtitle1"
                          :inline  true
                          :style   (:course-label styles)}
           "Course"]
          [ui/select {:value     @current-course
                      :variant   "outlined"
                      :on-change (fn [event]
                                   (let [selected-course (->> event .-target .-value)]
                                     (reset! current-course selected-course)
                                     (re-frame/dispatch [::ie/set-current-course selected-course])))
                      :style     (:course-selector styles)}
           (for [{:keys [key value text]} courses]
             ^{:key key}
             [ui/menu-item {:value value} text])]]
         [ui/grid {:item true :xs 12 :style (:footer-row styles)}
          [ui/button {:href translation-module-url}
           "Translation Module"]]]]])))
