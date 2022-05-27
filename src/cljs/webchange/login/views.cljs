(ns webchange.login.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.login.routes :as routes]
    [webchange.login.sign-in.views :refer [sign-in-form]]
    [webchange.login.state :as state]
    [webchange.ui-framework.components.index :as ui]))

(defn index
  []
  (r/create-class
    {:display-name "Log In Index"

     :component-did-mount
     (fn [this]
       (let [{:keys [route]} (r/props this)]
         (routes/init! (:path route))
         (re-frame/dispatch [::state/load-current-user])))

     :reagent-render
     (fn []
       (let [current-user-loaded? @(re-frame/subscribe [::state/current-user-loaded?])
             {:keys [handler props]} @(re-frame/subscribe [::state/current-page])]
         (if current-user-loaded?
           [:div#tabschool-login
            (case handler
              [sign-in-form props])]
           [ui/loading-overlay])))}))