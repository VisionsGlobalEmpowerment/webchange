(ns webchange.emails.templates.core
  (:require
    [webchange.emails.templates.email-confirmation :refer [email-confirmation-template]]))

(def templates
  {:email-confirmation {:subject "Blue Bricks Account Activation"
                        :template email-confirmation-template}})
