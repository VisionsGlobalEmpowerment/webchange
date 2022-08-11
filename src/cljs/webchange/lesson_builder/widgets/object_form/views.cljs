(ns webchange.lesson-builder.widgets.object-form.views
  (:require
    [webchange.lesson-builder.widgets.object-form.image-form.views :refer [image-form]]))

(def form-components {"image" image-form})

(defn object-form
  [{:keys [data] :as props}]
  (let [{object-type :type} data]
    [:div.widget--object-form
     (if-let [component (get form-components object-type)]
       [component props])]))
