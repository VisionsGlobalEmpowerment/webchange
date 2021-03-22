(ns webchange.editor-v2.wizard.steps.fill-template-flipbook.views
  (:require
    [webchange.editor-v2.wizard.steps.fill-template-flipbook.views-layout :refer [layout]]))

(defn- key->option
  [option-key template]
  (->> (get template :options [])
       (some (fn [{:keys [key] :as option}]
               (and (= key option-key) option)))))

(defn template-form
  [{:keys [data template validator] :as props}]
  (print "props" props)
  (let [layout-option (key->option "cover-layout" template)]
    (print "layout-option" layout-option)
    [:div.flipbook-template-form
     [:h1 "Create Book"]
     [:div.content
      [:div.left-side
       [layout {:option    layout-option
                :data      data
                :validator validator}]]
      [:div.right-side]]]))
