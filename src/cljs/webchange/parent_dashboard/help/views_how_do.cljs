(ns webchange.parent-dashboard.help.views-how-do
  (:require
    [webchange.parent-dashboard.data :refer [feedback-form-url]]
    [webchange.parent-dashboard.help.views-common :as c]))

(defn how-do
  []
  [c/block {:title "How do I..."}
   [c/qa {:q "How do I play TabSchool’s games?"
          :a "You must add a student in order to start playing TabSchool games.
          To add a student go to your dashboard and find the ‘+ Add Student’ button.
          Click this button and enter the student’s first name, age, and type of device they will use.
          Repeat this step for each student that will need an account. You can add as many students as you want.
          Each student account will track its own progress separately.
          Once the student account is created, click on the ‘PLAY’ button to access
          the student’s home screen and start going through our course!"}]
   [c/qa {:q "How do I delete my student’s account?"
          :a "To delete a student account, go to your dashboard and find the account to be deleted.
          Hit the trash can icon at the top right of the box with the student’s name.
          Keep in mind that once an account is deleted, the student’s progress in the game will also be deleted."}]
   [c/qa {:q "How do I delete my TabSchool User Profile?"
          :a "To permanently delete your account, please send an email to Info@TabSchool.com.
          Please refer to the Privacy Policy for more information regarding deletion requests."}]
   [c/qa {:q "How do I edit a student’s account name?"
          :a "We are working on that! Unfortunately, the name cannot be edited once created right now.
          If you want to edit a student’s account name, you will need to delete the student’s account.
          Then create a new account by hitting the ‘+ Add Student’ button.
          Keep in mind that once an account is deleted, the student’s progress in the game will also be deleted."}]
   [c/qa {:q "How do I report a bug or an issue I had while playing the games?"}
    [c/p
     "Please click the pink button titled ‘"
     [c/link {:url feedback-form-url} "Give Feedback"]
     "’ on the banner above.
     Fill out the form to report any bugs, issues, concerns, or anything else you would like to share with us.
     We would really appreciate hearing from you!"]]])
