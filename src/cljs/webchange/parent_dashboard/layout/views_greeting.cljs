(ns webchange.parent-dashboard.layout.views-greeting)

(defn greeting
  []
  [:div.greeting
   [:div.message
    [:h1 "Thank You for Beta Testing our Games!"]
    [:p "Please help us improve our PreK literacy content by using the link below to send comments,
    bug reports, and suggestions."]
    [:a {:href       "https://www.google.com/"
         :target     "_blank"
         :rel        "noopener noreferrer"
         :class-name "button"}
     "Give feedback"]]])
