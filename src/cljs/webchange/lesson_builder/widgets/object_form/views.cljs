(ns webchange.lesson-builder.widgets.object-form.views
  (:require
    [webchange.lesson-builder.widgets.object-form.animation-form.views :refer [animation-form]]
    [webchange.lesson-builder.widgets.object-form.image-form.views :refer [image-form]]
    [webchange.lesson-builder.widgets.object-form.text-form.views :refer [text-form]]
    [webchange.lesson-builder.widgets.object-form.text-tracing-pattern-form.views :refer [text-tracing-pattern-form]]
    [webchange.lesson-builder.widgets.object-form.video-form.views :refer [video-form]]))

(def form-components {"animation"            animation-form
                      "image"                image-form
                      "text"                 text-form
                      "text-tracing-pattern" text-tracing-pattern-form
                      "video"                video-form})

(defn object-form
  [{:keys [data] :as props}]
  (let [{object-type :type} data]
    [:div.widget--object-form
     (if-let [component (get form-components object-type)]
       [component props])]))
