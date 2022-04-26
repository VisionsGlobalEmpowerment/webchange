(ns webchange.admin.pages.schools.state
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
  ::schools-list
  (fn [db]
    ;; (get-in db [:dashboard :schools])
    ;;#############################
    [{:id 0 :name "School 00" :presentation 37 :users 90}
     {:id 1 :name "School 01" :presentation 37 :users 90}
     {:id 2 :name "School 02" :presentation 37 :users 90}
     {:id 3 :name "School 03" :presentation 37 :users 90}
     {:id 4 :name "School 04" :presentation 37 :users 90}
     ]
    ;;############################
    ))
