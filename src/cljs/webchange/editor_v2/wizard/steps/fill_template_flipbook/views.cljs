(ns webchange.editor-v2.wizard.steps.fill-template-flipbook.views
  (:require
    [webchange.editor-v2.wizard.steps.fill-template-flipbook.views-authors :refer [authors]]
    [webchange.editor-v2.wizard.steps.fill-template-flipbook.views-cover-image :refer [cover-image]]
    [webchange.editor-v2.wizard.steps.fill-template-flipbook.views-illustrators :refer [illustrators]]
    [webchange.editor-v2.wizard.steps.fill-template-flipbook.views-layout :refer [layout]]
    [webchange.editor-v2.wizard.steps.fill-template-flipbook.views-title :refer [title]]
    [webchange.ui-framework.components.index :refer [button]]))

(defn- key->option
  [option-key template]
  (->> (get template :options [])
       (some (fn [{:keys [key] :as option}]
               (and (= key option-key) option)))))

(defn template-form
  [{:keys [data template validator]}]
  (let [template-options (get template :options)
        common-props {:data      data
                      :validator validator}]
    [:div.flipbook-template-form
     [:h1 "Create Book"]
     (when (some? template-options)
       [:div.content
        [layout (assoc common-props :option (key->option "cover-layout" template))]
        [:div
         [title (assoc common-props :option (key->option "cover-title" template))]
         [cover-image (assoc common-props :option (key->option "cover-image" template))]
         [authors (assoc common-props :option (key->option "authors" template))]
         [illustrators (assoc common-props :option (key->option "illustrators" template))]]])]))
