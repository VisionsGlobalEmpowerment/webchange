(ns webchange.login.pages.views
  (:require
    [webchange.ui.index :as ui]))

(defn- subfooter
  []
  [:div.subfooter
   [:div.subfooter-left
    [:h2.subfooter-title "Ready to get started?"]
    [:div.subfooter-subtitle "Sign up free for Beta or contact us"]]
   [:div.subfooter-right
    [ui/button {:on-click #()
                :color "blue-1"}
     "Sign Up Free"]
    [ui/button {:on-click #()
                :color "blue-1"}
     "Contact Us"]]])

(defn registration-success
  [props]
  [:div.pages-page
   [:div.registration-success
    [:h2.title "Thank you for creating your account!"]
    [:div.subtitle "Please check your email (and junk folder just in case!) and click the link to verify and activate your account in order to get started."]
    [ui/button {:color "green-1"}
     "Login Now"]
    [ui/image {:class-name "vaca-image"
               :src "/images/auth/vaca.svg"}]]
   [subfooter]])


(defn email-confirmation-success
  [props]
  [:div "Email confirmation success"])

(defn email-confirmation-failure
  [props]
  [:div "Email confirmation failure"])
