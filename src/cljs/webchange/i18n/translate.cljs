(ns webchange.i18n.translate
  (:require
    [re-frame.core :as re-frame]
    [taoensso.tempura :refer [tr]]
    [webchange.state.core :as state]))

(def translations
  {"english" {:book-library {:book-library "Book library"}}
   "spanish" {:book-library {:book-library "Biblioteca"}}})

(re-frame/reg-sub
  ::t
  (fn []
    (re-frame/subscribe [::state/current-course-id]))
  (fn [language [_ key]]
    (tr {:dict translations} [language "english"] key)))
