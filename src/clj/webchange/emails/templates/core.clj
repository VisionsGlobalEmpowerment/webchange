(ns webchange.emails.templates.core
  (:require
    [webchange.emails.templates.email-confirmation :refer [email-confirmation-template]]
    [webchange.emails.templates.reset-password :refer [reset-password-template]]))

(def templates
  {:email-confirmation {:subject "Blue Brick School Verify Account"
                        :template email-confirmation-template}
   :reset-password {:subject "Blue Bricks Account Password Reset"
                    :template reset-password-template}})
