(ns webchange.student-dashboard.toolbar.views
  (:require
   [cljs-react-material-ui.reagent :as ui]
   [webchange.student-dashboard.toolbar.auth.views :refer [auth]]
   [webchange.student-dashboard.toolbar.course-slug.views :refer [course-slug]]
   [webchange.student-dashboard.toolbar.logo.views :refer [logo]]
   [webchange.student-dashboard.toolbar.sync.views :refer [sync]]
   [webchange.ui.theme :refer [get-in-theme]]))

(defn- get-styles
  []
  {:placeholder {:margin-bottom "64px"}
   :toolbar     {:background-color (get-in-theme [:palette :background :darken])}})

(defn toolbar
  []
  (let [styles (get-styles)]
    [:div {:style (:placeholder styles)}
     [ui/app-bar {:position "fixed"}
      [ui/toolbar {:style (:toolbar styles)}
       [logo]
       [auth]
       [course-slug]
       [sync]]]]))
