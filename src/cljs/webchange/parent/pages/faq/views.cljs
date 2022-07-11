(ns webchange.parent.pages.faq.views
  (:require
    [re-frame.core :as re-frame]
    [webchange.parent.components.header-button.views :refer [header-button]]
    [webchange.parent.pages.faq.state :as state]))

(defn page
  []
  (let [handle-back-click #(re-frame/dispatch [::state/open-students-list-page])]
    [:div#page--faq
     [:h1 "Frequently Asked Questions (FAQs)" [header-button {:on-click handle-back-click}
                                               "Back"]]
     [:h2 "About Us"]
     [:dl
      [:dt "What is TabSchool and what does TabSchool do?"]
      [:dd "TabSchool is a US-based non-profit organization with a mission to provide quality educational resources for all students. We develop fun, free, customizable e-learning games for students in online and offline settings. Our platform, which is currently in development, will allow anyone to easily translate and localize game content by changing characters, voiceovers, visuals, and backgrounds."]

      [:dt "What do the games teach?"]
      [:dd "The games teach foundational literacy skills that align with the Head Start Early Learning Outcomes Framework. They have been designed for native English speakers. Students play activities that follow a learning progression created by preschool teachers. The topics covered in the games include letter recognition, letter sound correspondence, vocabulary, listening, speaking, concepts of print, rhyming, and an introduction to syllables."]

      [:dt "What is the TabSchool Beta Test?"]
      [:dd "The TabSchool Beta Test is an initial release of our e-learning literacy activities. With your help and feedback, we will continue to improve these games. We will also add new activities for Beta testing in the coming months!"]

      [:dt "Who should play TabSchool games?"]
      [:dd "All ages are welcome to play, but the games were designed for students ages 3-6."]

      [:dt "Who created the games?"]
      [:dd "Our team of education and IT experts created the games with contributions from educators, partner organizations and individuals who have licensed their work under Creative Commons. You can access our attributions list here."]]
     ]))
