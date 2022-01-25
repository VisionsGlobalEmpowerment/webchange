(ns webchange.parent-dashboard.help.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.parent-dashboard.help.state :as state]
    [webchange.parent-dashboard.layout.views :refer [layout]]
    [webchange.parent-dashboard.ui.index :refer [button]]))

(defn- sub-title
  []
  (into [:h3]
        (-> (r/current-component)
            (r/children))))

(defn- p
  [{:keys [title]}]
  [:div.paragraph
   (when (some? title)
     [:h3 title])
   (into [:p]
         (-> (r/current-component)
             (r/children)))])

(defn link
  [{:keys [url]}]
  (into [:a.link {:href url}]
        (-> (r/current-component)
            (r/children))))

(defn- qa
  [{:keys [q a]}]
  [:dl.qa
   [:dt "Q: " q]
   [:dd "A: " a]])

(defn- help-form
  []
  [:div.help-form
   [sub-title "About us"]
   [qa {:q "What is TabSchool and what does TabSchool do?"
        :a "TabSchool is a US-based non-profit organization with a mission to provide quality educational resources for all students.  We develop fun, free, customizable e-learning games for students in online and offline settings.  Our platform, which is currently in development, will allow anyone to easily translate and localize game content by changing characters, voiceovers, visuals, and backgrounds."}]
   [qa {:q "What do the games teach?"
        :a "The games teach foundational literacy skills that align with the Head Start Early Learning Outcomes Framework. They have been designed for native English speakers.  Students play activities that follow a learning progression created by preschool teachers.  The topics covered in the games include letter recognition, letter sound correspondence, vocabulary, listening, speaking, concepts of print, rhyming, and an introduction to syllables."}]
   [qa {:q "What is the TabSchool Beta Test?"
        :a "The TabSchool Beta Test is an initial release of our e-learning literacy activities. With your help and feedback, we will continue to improve these games.  We will also add new activities for Beta testing in the coming months!"}]
   [qa {:q "Who should play TabSchool games?"
        :a "All ages are welcome to play, but the games were designed for students ages 3-6"}]
   [qa {:q "Who created the games?"
        :a "Our team of education and IT experts created the games with contributions from educators, partner organizations and individuals who have licensed their work under Creative Commons."}]])

(defn help-page
  []
  (let [handle-back-click #(re-frame/dispatch [::state/open-dashboard])]
    [layout {:title   "Help"
             :actions [[button {:on-click handle-back-click
                                :variant  "text"}
                        "< Back"]]}
     [help-form]]))
