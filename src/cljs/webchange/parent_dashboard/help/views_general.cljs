(ns webchange.parent-dashboard.help.views-general
  (:require
    [webchange.parent-dashboard.help.views-common :as c]))

(defn general
  []
  [c/block {:title "General questions"}
   [c/qa {:q "What type of feedback would be most helpful?"}
    [c/p "All feedback is welcome – the more, the better!
     If you would like to report an issue, it will be most helpful for us
     if you can explain the exact place in a game where something went wrong and describe in detail what happened.
     Feel free to send an image or video of any issues by emailing us at "
     [c/link {:url "mailto:Info@TabSchool.com"} "Info@TabSchool.com"]
     "."]
    [c/p "Below are just a few examples of the types of issues to look out for and report."]
    [c/list-items {:title "Display issues"}
     "Something in the game appears visually strange or distorted"
     "The sizing of the game on your screen is too small or otherwise incorrect"
     "Videos or character movements seem to be lagging"]
    [c/list-items {:title "Mechanics issues"}
     "The ability to tap or drag is not working properly or seems difficult"
     "The game gets stuck or freezes"
     "Characters mouths do not move properly while they talk"]
    [c/list-items {:title "Learning related concerns"}
     "A learner is confused about what to tap, drag or move"
     "A learner does not understand how to play the game"
     "A game seems too difficult for a 3 to 6 year old learner"
     "A learner seems bored or tired within the first 10 minutes of playing game"]]

   [c/qa {:q "What device works best for playing TabSchool’s e-learning games?"
          :a "The games were designed to be played on Android tablets.
          They can be played on other devices, but will work best on Android tablets."}]

   [c/qa {:q "When will the games be available in more languages?"}
    [c/p
     "Currently, our games have been designed for native English speakers.
     Soon, we will release the same set of e-learning games in Spanish.
     In the near future, we aim to use our easy translation tool to serve users of many more languages.
     If you are a professional translator or educator who would like to assist us with game translations,
     please send us a form on the ‘"
     [c/link {:url "https://www.tabschool.com/"} "Collaborate with TabSchool"]
     "’ section of our website’s home page."]]

   [c/qa {:q "When will more content become available?"
          :a "As we continue to build and user test games, we will continue to grow the number of literacy activities available in the online Beta Test.
          By the end of 2022, we expect to release over 300 foundational literacy games in English and Spanish.
          Following that, we will continue to build more levels of foundational literacy activities"}]])
