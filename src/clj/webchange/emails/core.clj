(ns webchange.emails.core
  (:require
    [webchange.db.core :as db]
    [webchange.emails.templates.core :refer [templates]]
    [hiccup.core :as hiccup]
    [postal.core :as postal]
    [java-time :as jt]))

(defn- generate-code!
  [metadata]
  (let [code (java.util.UUID/randomUUID)
        created-at (jt/local-date-time)]
    (db/insert-code! {:code code :created_at created-at :metadata metadata})
    (.toString code)))

(def from-email "info@bluebrickschool.org")

(defn- send-email!
  [email subject html]
  (postal/send-message {:from from-email
                        :to email
                        :subject subject
                        :body [{:type "text/html"
                                :content html}]}))

(defn- send-email-template!
  [email template-id data]
  (let [{:keys [subject template]} (get templates template-id)
        content (template data)
        html (hiccup/html content)]
    (send-email! email subject html)))

(defn request-email-confirmation!
  [{:keys [id email first-name]}]
  (let [code (generate-code! {:user-id id :email email})]
    (send-email-template! email :email-confirmation {:email email :id id :code code :name first-name})))

(comment

  (generate-code! {:user-id 36 :email "demo@example.com"})
  (request-email-confirmation! {:id 1
                                :email "demo@example.com"
                                :first-name "Admin"})

  (let [email "demo@example.com"
        data {:id 36
              :email email
              :first-name "Ivan"}
        template-id :email-confirmation
        {:keys [subject template]} (get templates template-id)
        content (template data)
        html (hiccup/html content)]
    html)
  )
