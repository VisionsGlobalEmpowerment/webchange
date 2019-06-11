(ns webchange.auth.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.config :as config]
    [webchange.routes :as routes]
    [webchange.events :as events]
    [webchange.subs :as subs]
    [webchange.auth.events :as auth.events]
    [webchange.auth.views-registration :as registration]
    [webchange.auth.views-teacher-login :as teacher-login]
    [sodium.core :as na]
    [soda-ash.core :as sa]
    [reagent.core :as r]))

(defn teacher-login
  [& args]
  (apply teacher-login/teacher-login-page args))

(defn register-form
  [& args]
  (apply registration/registration-page args))

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
    [:div {:style {:margin "14px 14px 14px 14px" :vertical-align "bottom" :padding-bottom "14px" :width "38px" :height "38px" :display "inline-block" :font-size "40pt"}} c]
    [:div {:style {:margin "14px" :width "38px" :height "38px" :display "inline-block"
                   :background (str "no-repeat center/30px url(/raw/img/auth/animal-" c ".png)")}}]
    ))

(defn show-code-ph []
  [:div {:style {:margin "14px" :width "38px" :height "38px" :background-image "url(/raw/img/auth/asset-11.png" :display "inline-block"}}])

(defn show-code [c]
  (if c
    [show-code-value c]
    [show-code-ph]))

(defn code-form [code]
  (let [[c1 c2 c3 c4 c5 c6] code]
    [:div {:style {:width "490px" :height "78px"  :margin "0 auto" :padding-left "48px" :padding-top "8px"
                   :background-image "url(/raw/img/auth/asset-12.png"}}
     [show-code c1]
     [show-code c2]
     [show-code c3]
     [show-code c4]
     [show-code c5]
     [show-code c6]]))

(defn student-access-form []
  (r/with-let [code (r/atom "")]
              (let [loading  @(re-frame/subscribe [:loading])
                    errors   @(re-frame/subscribe [:errors])]
                [:div {:class-name "login-form"}
                 [:style "body {background-color: #00d2ff}"]
                    [na/grid {:text-align "center" :centered? true}

                     [na/grid-row {:centered? true :style {:margin-top "90px"}}
                       [na/grid-column {}
                        [na/header {:as "h2" :text-align "center" :style {:color "#ffffff" :font-size "40pt" :font-family "Roboto"}
                                    :content "STUDENT ACCESS"}]]]

                     [na/grid-row {:centered? true :style {:margin-top "50px"}}
                       [na/grid-column {}
                        [code-form @code]]]

                     [na/grid-row {:divided? false :style {:margin-top "90px"}}

                      [na/grid-column {:style {:min-width 363}}

                         [na/grid {}
                          [na/grid-row {:columns 3 :centered? true}
                           [na/grid-column {} [animal-form "animal-b.png" #(swap! code str "b")]]
                           [na/grid-column {} [animal-form "animal-c.png" #(swap! code str "c")]]
                           [na/grid-column {} [animal-form "animal-d.png" #(swap! code str "d")]]]
                          [na/grid-row {:columns 3 :centered? true}
                           [na/grid-column {} [animal-form "animal-f.png" #(swap! code str "f")]]
                           [na/grid-column {} [animal-form "animal-k.png" #(swap! code str "k")]]
                           [na/grid-column {} [animal-form "animal-l.png" #(swap! code str "l")]]]
                          [na/grid-row {:columns 3 :centered? true}
                           [na/grid-column {} [animal-form "animal-s.png" #(swap! code str "s")]]
                           [na/grid-column {} [animal-form "animal-v.png" #(swap! code str "v")]]
                           [na/grid-column {} [animal-form "animal-w.png" #(swap! code str "w")]]]
                          ]
                         ]

                      [na/grid-column {:style {:width 110}}
                        [:div {:style {:margin "0 auto" :width "4px" :height "345px" :background-image "url(/raw/img/auth/asset-13.png)"}}]]

                      [na/grid-column {:style {:min-width 363}}

                        [na/grid {}
                         [na/grid-row {:columns 3 :centered? true}
                          [na/grid-column {} [number-form "1" #(swap! code str "1")]]
                          [na/grid-column {} [number-form "2" #(swap! code str "2")]]
                          [na/grid-column {} [number-form "3" #(swap! code str "3")]]]
                         [na/grid-row {:columns 3 :centered? true}
                          [na/grid-column {} [number-form "4" #(swap! code str "4")]]
                          [na/grid-column {} [number-form "5" #(swap! code str "5")]]
                          [na/grid-column {} [number-form "6" #(swap! code str "6")]]]
                         [na/grid-row {:columns 3 :centered? true}
                          [na/grid-column {} [number-form "7" #(swap! code str "7")]]
                          [na/grid-column {} [number-form "8" #(swap! code str "8")]]
                          [na/grid-column {} [number-form "9" #(swap! code str "9")]]]
                         [na/grid-row {:columns 3 :centered? true}
                          [na/grid-column {} [number-form "0" #(swap! code str "0")]]]
                         ]]
                       ]]])))