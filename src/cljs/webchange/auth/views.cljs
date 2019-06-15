(ns webchange.auth.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.auth.views-teacher-login :as teacher-login]
    [webchange.auth.events :as auth.events]
    [soda-ash.core :as sa]
    [sodium.core :as na]
    [reagent.core :as r]))

(defn teacher-login
  [& args]
  (apply teacher-login/teacher-login-page args))

(defn animal-form [image on-click]
  [:div {:on-click on-click :style {:width "101px" :height "101px" :background-image "url(/raw/img/auth/form.png)"}}
   [:div {:style {:width "101px" :height "101px" :background (str "url(/raw/img/auth/" image ") no-repeat center center")}}]])

(defn number-form [value on-click]
  [:div {:on-click on-click :style {:padding "38px"
                                    :width "101px" :height "101px"
                                    :background-image "url(/raw/img/auth/form.png)"
                                    :font-size "40pt" :font-family "Roboto"}}
   value])

(defn is-number-code? [c] (re-matches #"\d" c))

(defn show-code-value [c]
  (if (is-number-code? c)
    [:div {:style {:margin "14px 30px" :vertical-align "bottom" :padding-bottom "14px" :width "38px" :height "38px" :display "inline-block" :font-size "40pt"}} c]
    [:div {:style {:margin "14px 30px" :width "38px" :height "38px" :display "inline-block"
                   :background (str "no-repeat center/30px url(/raw/img/auth/animal-" c ".png)")}}]
    ))

(defn show-code-ph []
  [:div {:style {:margin "14px 30px" :width "38px" :height "38px" :background-image "url(/raw/img/auth/asset-11.png" :display "inline-block"}}])

(defn show-code [c]
  (if c
    [show-code-value c]
    [show-code-ph]))

(defn code-form [code]
  (let [[c1 c2 c3 c4] code]
    [:div {:style {:width "490px" :height "78px"  :margin "0 auto" :padding-left "48px" :padding-top "8px"
                   :background-image "url(/raw/img/auth/asset-12.png"}}
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
    (let [loading  @(re-frame/subscribe [:loading])
          errors   @(re-frame/subscribe [:errors])]
      (cond
        (:init-current-school loading) [sa/Loader {:active true :inline "centered"}]
        (:student-login loading) [sa/Loader {:active true :inline "centered"}]
        :else
        [:div {:class-name "student-access-form"}
         [:style "body {background-color: #00d2ff}"]
         [na/grid {:text-align "center" :centered? true}

          [na/grid-row {:centered? true :style {:margin-top "90px"}}
           [na/grid-column {:style {:height "80px"} :text-align "center" }
            [na/header {:as "h2" :text-align "center" :style {:color "#ffffff" :font-size "40pt" :font-family "Roboto"}
                        :content "STUDENT ACCESS"}]
            (when (:student-login errors)
              [:div [sa/Message {:negative true :compact true} [:p (-> errors :student-login :form)]]])]]

          [na/grid-row {:centered? true :style {:margin-top "50px"}}
           [na/grid-column {}
            [code-form @code]]]

          [na/grid-row {:divided? false :style {:margin-top "90px"}}



           [na/grid-column {}]

           [na/grid-column {:style {:min-width 363}}

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
              [na/grid-column {} [number-form "0" #(enter-code code "0")]]]
             ]
            ]
           [na/grid-column {}]
           ]]]))))