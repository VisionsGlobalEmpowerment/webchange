(ns webchange.parent-dashboard.help.views-about
  (:require
    [webchange.parent-dashboard.data :refer [attributions-url]]
    [webchange.parent-dashboard.help.views-common :as c]))

(defn about
  []
  [c/block {:title "About us"}
   [c/qa {:q "What is TabSchool and what does TabSchool do?"
          :a "TabSchool is a US-based non-profit organization with a mission to provide quality educational resources for all students.
          We develop fun, free, customizable e-learning games for students in online and offline settings.
          Our platform, which is currently in development, will allow anyone to easily translate and
          localize game content by changing characters, voiceovers, visuals, and backgrounds."}]
   [c/qa {:q "What do the games teach?"
          :a "The games teach foundational literacy skills that align with the Head Start Early Learning Outcomes Framework.
          They have been designed for native English speakers.
          Students play activities that follow a learning progression created by preschool teachers.
          The topics covered in the games include letter recognition, letter sound correspondence, vocabulary,
          listening, speaking, concepts of print, rhyming, and an introduction to syllables."}]
   [c/qa {:q "What is the TabSchool Beta Test?"
          :a "The TabSchool Beta Test is an initial release of our e-learning literacy activities.
          With your help and feedback, we will continue to improve these games.
          We will also add new activities for Beta testing in the coming months!"}]
   [c/qa {:q "Who should play TabSchool games?"
          :a "All ages are welcome to play, but the games were designed for students ages 3-6."}]
   [c/qa {:q "Who created the games?"}
    [c/p "Our team of education and IT experts created the games with contributions from educators,
          partner organizations and individuals who have licensed their work under "
     [c/link {:url "https://creativecommons.org/faq/"} "Creative Commons"]
     ". You can access our attributions list "
     [c/link {:url attributions-url} "here"]]]])
