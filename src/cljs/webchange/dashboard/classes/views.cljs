(ns webchange.dashboard.classes.views
  (:require
    [webchange.dashboard.classes.remove-form.views :as remove-form]
    [webchange.dashboard.classes.class-form.views :refer [class-form-modal]]
    [webchange.dashboard.classes.class-profile.views :as class-profile-views]
    [webchange.dashboard.classes.classes-list.views :as classes-list-views]
    [webchange.dashboard.classes.classes-menu.views :as classes-menu-views]))

(def class-modal class-form-modal)
(def class-delete-modal remove-form/class-delete-modal)
(def class-profile class-profile-views/class-profile-page)
(def classes-list classes-list-views/classes-list-page)
(def classes-menu classes-menu-views/classes-menu)
