(ns webchange.i18n.translate
  (:require
    [re-frame.core :as re-frame]
    [taoensso.tempura :refer [tr]]
    [webchange.state.core :as state]))

(def translations
  {"english" {:book-library "Book library"
              :categories   {:animals            "Animals"
                             :family-and-friends "Family & Friends" ;; <- non-breaking space before "Friends"
                             :science-stem       "Science"
                             :sports             "Sports"
                             :vehicles           "Vehicles"}}
   "spanish" {:book-library "Biblioteca"
              :categories   {:animals            "Animales"
                             :family-and-friends "Familia y Amigos" ;; <- non-breaking space before "Amigos"
                             :science-stem       "Ciencia"
                             :sports             "Deportes"
                             :vehicles           "Vehículos"}}})

(re-frame/reg-sub
  ::t
  (fn []
    (re-frame/subscribe [::state/current-course-id]))
  (fn [language [_ key]]
    (tr {:dict translations} [language "english"] key)))
