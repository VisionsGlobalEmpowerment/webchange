(ns webchange.auth.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.auth.views-teacher-login :as teacher-login]
    [webchange.auth.events :as auth.events]
    [soda-ash.core :as sa]
    [sodium.core :as na]
    [reagent.core :as r]))

(defn- get-styles
  []
  {:student-access-form {:background-image "url(/images/dashboard/bg_02.jpg)"
                         :height           "100%"
                         :display          "flex"
                         :flex-direction   "column"
                         :justify-content  "space-between"
                         :padding          "40px"
                         :align-items      "center"}
   :num-pad             {:width "370px"}})

(defn teacher-login
  [& args]
  (apply teacher-login/teacher-login-page args))

(defn animal-form [image on-click]
  [:div {:on-click on-click :style {:width "101px" :height "101px" :background-image "url(/images/auth/form.png)"}}
   [:div {:style {:width "101px" :height "101px" :background (str "url(/images/auth/" image ") no-repeat center center")}}]])

(defn number-form [value on-click]
  [:div {:on-click on-click :style {:padding          "38px"
                                    :width            "101px"
                                    :height           "101px"
                                    :background-image "url(/images/auth/form.png)"
                                    :font-size        "40pt"
                                    :font-family      "Roboto"
                                    :user-select      "none"}}
   value])

(defn is-number-code? [c] (re-matches #"\d" c))

(defn show-code-value [c]
  (if (is-number-code? c)
    [:div {:style {:margin "14px 30px" :vertical-align "bottom" :padding-bottom "14px" :width "38px" :height "38px" :display "inline-block" :font-size "40pt"}} c]
    [:div {:style {:margin "14px 30px" :width "38px" :height "38px" :display "inline-block"
                   :background (str "no-repeat center/30px url(/images/auth/animal-" c ".png)")}}]
    ))

(defn show-code-ph []
  [:div {:style {:margin "14px 30px" :width "38px" :height "38px" :background-image "url(/images/auth/asset-11.png" :display "inline-block"}}])

(defn show-code [c]
  (if c
    [show-code-value c]
    [show-code-ph]))

(defn code-form [code]
  (let [[c1 c2 c3 c4] code]
    [:div {:style {:width "490px" :height "78px"  :margin "0 auto" :padding-left "48px" :padding-top "8px"
                   :background-image "url(/images/auth/asset-12.png"}}
     [show-code c1]
     [show-code c2]
     [show-code c3]
     [show-code c4]]))

(defn enter-code [code-ratom symbol]
  (swap! code-ratom str symbol)
  (when (<= 4 (count @code-ratom))
    (re-frame/dispatch [::auth.events/student-login @code-ratom])
    (reset! code-ratom "")))

(defn student-access-form []
  (r/with-let [code (r/atom "")]
              (let [loading @(re-frame/subscribe [:loading])
                    errors @(re-frame/subscribe [:errors])
                    styles (get-styles)]
                (cond
                  (:init-current-school loading) [sa/Loader {:active true :inline "centered"}]
                  (:student-login loading) [sa/Loader {:active true :inline "centered"}]
                  :else
                  [:div {:style (:student-access-form styles)}
                   [:div
                    [na/header {:as         "h2"
                                :text-align "center"
                                :style      {:color       "#ffffff"
                                             :font-size   "40pt"
                                             :font-family "Roboto"
                                             :user-select "none"}
                                :content    "STUDENT ACCESS"}]
                    (when (:student-login errors)
                      [:div [sa/Message {:negative true :compact true} [:p (-> errors :student-login :form)]]])]

                   [:div
                    [code-form @code]]

                   [:div {:style (:num-pad styles)}
                    [na/grid {}
                     [na/grid-row {:columns 3 :centered? true}
                      [na/grid-column {} [number-form "1" #(enter-code code "1")]]
                      [na/grid-column {} [number-form "2" #(enter-code code "2")]]
                      [na/grid-column {} [number-form "3" #(enter-code code "3")]]]
                     [na/grid-row {:columns 3 :centered? true}
                      [na/grid-column {} [number-form "4" #(enter-code code "4")]]
                      [na/grid-column {} [number-form "5" #(enter-code code "5")]]
                      [na/grid-column {} [number-form "6" #(enter-code code "6")]]]
                     [na/grid-row {:columns 3 :centered? true}
                      [na/grid-column {} [number-form "7" #(enter-code code "7")]]
                      [na/grid-column {} [number-form "8" #(enter-code code "8")]]
                      [na/grid-column {} [number-form "9" #(enter-code code "9")]]]
                     [na/grid-row {:columns 3 :centered? true}
                      [na/grid-column {} [number-form "0" #(enter-code code "0")]]]]]]))))
