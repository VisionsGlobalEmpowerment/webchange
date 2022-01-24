(ns webchange.parent-dashboard.layout.views-greeting)

(defn greeting
  []
  [:div.greeting
   [:div.message
    [:h1 "Welcome to TabSchool!"]
    [:p
     "Thank you for joining us in our mission to provide quality educational resources for all students!
    We are excited to have Beta versions of our Pre-K English literacy games available for your student(s) to play!"]]
   [:img {:src "/images/parent_dashboard/greeting.png"}]])
