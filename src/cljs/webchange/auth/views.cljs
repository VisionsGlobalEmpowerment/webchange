(ns webchange.auth.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.auth.events :as auth.events]
    [webchange.auth.views-teacher-login :as teacher-login]))

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
   :num-pad             {:width          "400px"}
  :bckspc-div           {:margin-left    "86px"
                         :display        "flex"
                         :align-items    "center"
                         :justify-content "space-between"}
   :bckspc-btn          {:background     "#fff"
                         :border-radius  "40px"
                         :margin-left    "10px"
                         :height         "78px"
                         :width          "78px"
                         :padding        "18px 0px 0px 18px"
                         :cursor         "pointer"}
   :bckspc-img          {:width          "40px"
                         :height         "40px"}})

(defn teacher-login
  [& args]
  (apply teacher-login/teacher-login-page args))

(defn animal-form [image on-click]
  [:div {:on-click on-click :style {:width "101px" :height "101px" :background-image "url(/images/auth/form.png)"}}
   [:div {:style {:width "101px" :height "101px" :background (str "url(/images/auth/" image ") no-repeat center center")}}]])

(defn number-form
  [value on-click]
  (let [styles (get-styles)]
    [:div {:on-click on-click
           :style    (:button styles)}
     value]))

(defn is-number-code? [c] (re-matches #"\d" c))

(defn show-code-value [c]
  (let [styles (get-styles)]
    (if (is-number-code? c)
      [:div {:style (:code-item styles)} c]
      [:div {:style (merge (:code-item styles)
                           {:background (str "no-repeat center/30px url(/images/auth/animal-" c ".png)")})}])))

(defn show-code-ph []
  [:div {:style {:margin "14px 30px" :width "38px" :height "38px" :background-image "url(/images/auth/asset-11.png" :display "inline-block"}}])

(defn show-code [c]
  (if c
    [show-code-value c]
    [show-code-ph]))

(defn code-form [code]
  (let [[c1 c2 c3 c4] code]
    [:div {:style {:width            "490px" :height "78px" :margin "0 auto" :padding-left "48px" :padding-top "8px"
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
                    styles (get-styles)
                    img-src "/icons/backspace.svg"]
                (cond
                  (:init-current-school loading) [ui/circular-progress]
                  (:student-login loading) [ui/circular-progress]
                  :else
                  [:div {:style (:student-access-form styles)}
                   [:div
                    [ui/typography {:variant "h2"
                                    :style   (:title styles)}
                     "Student Access"]
                    (when (:student-login errors)
                      [ui/chip {:color "secondary"
                                :label (-> errors :student-login :form)}])]

                   ;;  Back Button
                   [:div {:style (:bckspc-div styles)}
                    [code-form @code]
                    [:div {:style (:bckspc-btn styles)
                           :onClick (fn [e]
                                      (.preventDefault e)
                                      (reset! code (subs @code 0 (- (count @code) 1))))}
                     [:img {:style (:bckspc-img styles)
                            :src img-src}]]]

                   [:div {:style (:num-pad styles)}
                    [ui/grid {:container     true
                              :align-content "center"
                              :align-items   "center"}
                     (for [number ["1" "2" "3" "4" "5" "6" "7" "8" "9"]]
                       ^{:key number}
                       [ui/grid {:item true :xs 4 :style (:button-container styles)} [number-form number #(enter-code code number)]])
                     [ui/grid {:item true :xs 12 :style (:button-container styles)} [number-form "0" #(enter-code code "0")]]]]]))))
