(ns webchange.admin.widgets.course-form.view
  (:require
    [webchange.admin.widgets.no-data.views :refer [no-data]]))

(defn course-form
  []
  [:div.course-form
   [no-data]])
