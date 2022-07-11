(ns webchange.parent.widgets.layout.banner.views
  (:require
    [webchange.ui.index :as ui]))

(defn banner
  []
  [:div.parent--layout--banner
   [:div.banner--message
    [:h1 "Thank You for Beta Testing our Games!"]
    [:p "Please help us improve our games by sending comments, bug reports, and suggestions using our feedback form."]
    [ui/button {:href       "https://docs.google.com/forms/d/1Vjs6mT785A0UvVOumWdayXNwPCKx0_7f3uPXMo6hutM/edit"
                :class-name "feedback"}
     "Give feedback"]]])
