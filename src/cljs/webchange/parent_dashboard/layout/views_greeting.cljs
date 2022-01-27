(ns webchange.parent-dashboard.layout.views-greeting
  (:require
    [webchange.parent-dashboard.data :refer [feedback-form-url]]))

(defn greeting
  []
  [:div.greeting
   [:div.message
    [:h1 "Thank You for Beta Testing our Games!"]
    [:p "Please help us improve our games by sending comments, bug reports, and suggestions using our feedback form."]
    [:a {:href       feedback-form-url
         :target     "_blank"
         :rel        "noopener noreferrer"
         :class-name "button"}
     "Give feedback"]]])
