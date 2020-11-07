(ns webchange.auth.views-auth-page
  (:require
    [webchange.auth.theme :refer [w-colors theme]]
    [webchange.ui.theme :refer [with-mui-theme]]))

(defn- translate
  [path]
  (get-in {:name            {:first "Tab"
                             :last  "School"}
           :forgot-password "Forgot Password?"
           :welcome         {:first "Welcome to"
                             :last  "TabSchool"}}
          path))

(def default-color (:default w-colors))
(def primary-color (:primary w-colors))
(def primary-darken-color (:primary-darken w-colors))
(def secondary-color (:secondary w-colors))
(def disabled-color (:disabled w-colors))

(def logo-size 44)
(def window-size {:height 650
                  :width  1000})
(def border-radius 15)
(def form-padding 40)
(def welcome-text-size 32)

(def wrapper-style {:align-items      "center"
                    :background-color primary-darken-color
                    :display          "flex"
                    :flex-direction   "column"
                    :height           "100%"
                    :justify-content  "center"})
(def window-style {:display        "flex"
                   :flex           "0 0 auto"
                   :flex-direction "row"
                   :height         (str (:height window-size) "px")
                   :width          (str (:width window-size) "px")})
(def main-content-style {:background-color default-color
                         :border-radius    (str border-radius "px 0 0 " border-radius "px")
                         :display          "flex"
                         :flex             "0 0 auto"
                         :flex-direction   "column"
                         :justify-content  "space-between"
                         :padding          (str form-padding "px")
                         :width            "36%"})
(def header-style {:display "flex"
                   :flex    "0 0 auto"
                   :margin  (str "0 0 " form-padding "px 0")})
(def logo-style {:flex                "0 0 auto"
                 :height              (str logo-size "px")
                 :width               (str logo-size "px")
                 :background-size     "100%"
                 :background-position "center"
                 :background-repeat   "no-repeat"})
(def company-name-style {:flex           "1 1 auto"
                         :font-family    "monospace"
                         :font-size      "22px"
                         :line-height    (str logo-size "px")
                         :margin         "0 0 0 10px"
                         :text-transform "uppercase"})
(def welcome-screen-style {:align-items      "center"
                           :background-color primary-color
                           :border-radius    (str "0 " border-radius "px " border-radius "px 0")
                           :display          "flex"
                           :flex             "1 1 auto"
                           :flex-direction   "column"
                           :justify-content  "center"})
(def welcome-text-1-style {:margin      "0"
                           :padding     "0"
                           :font-size   welcome-text-size
                           :font-weight "normal"})
(def welcome-text-2-style {:margin         "0"
                           :padding        "0"
                           :font-size      (* welcome-text-size 1.2)
                           :font-weight    "bold"
                           :text-transform "uppercase"})
(def footer-style {:flex           "0 0 auto"
                   :flex-direction "row"
                   :font-size      "13px"
                   :margin         (str form-padding "px 0 0 0")
                   :text-align     "right"})
(def forgot-password-style {:color  disabled-color
                            :cursor "not-allowed"})

(defn- header
  [{:keys [logo-url name-first name-second]}]
  [:header {:style header-style}
   [:div {:style (merge logo-style {:background-image (str "url(" logo-url ")")})}]
   [:h1 {:style company-name-style}
    [:span {:style {:color primary-color}} name-first]
    [:span {:style {:color secondary-color}} name-second]]])

(defn- welcome-screen
  [{:keys [first-line second-line]}]
  [:div {:style welcome-screen-style}
   [:div {:style {:color default-color}}
    [:h1 {:style welcome-text-1-style} first-line]
    [:h2 {:style welcome-text-2-style} second-line]]])

(defn- footer
  [{:keys [forgot-password]}]
  [:footer {:style footer-style}
   [:a {:style forgot-password-style} forgot-password]])

(defn auth-page
  [children]
  [with-mui-theme {:theme theme}
   [:div {:style wrapper-style}
    [:div {:style window-style}
     [:div {:style main-content-style}
      [header {:logo-url    "/raw/img/teacher-login/logo.png"
               :name-first  (translate [:name :first])
               :name-second (translate [:name :last])}]
      [:main {:style {:flex "0 0 auto"}}
       children]
      [footer {:forgot-password (translate [:forgot-password])}]]
     [welcome-screen {:first-line  (translate [:welcome :first])
                      :second-line (translate [:welcome :last])}]]]])
