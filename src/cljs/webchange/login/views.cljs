(ns webchange.login.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.login.header.views :refer [header]]
    [webchange.login.routes :as routes]
    [webchange.login.sign-in.views :refer [sign-in-form]]
    [webchange.login.pages.views :refer [registration-success]]
    [webchange.login.registration.views :refer [registration]]
    [webchange.login.state :as state]
    [webchange.ui.index :as ui]))

(defn index
  []
  (r/create-class
   {:display-name "Log In Index"

    :component-did-mount
    (fn [this]
      (let [{:keys [route]} (r/props this)]
        (routes/init! (:path route))))

    :reagent-render
    (fn []
      (let [{:keys [handler props]} @(re-frame/subscribe [::state/current-page])]
        [:div#tabschool-login {:class-name (ui/get-class-name {"background-with-image" (= handler :sign-in)})}
         [header]
         (case handler
           :sign-up-success [registration-success props]
           :sign-up [registration props]
           [sign-in-form props])]))}))
