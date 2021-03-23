(ns webchange.editor-v2.wizard.steps.fill-template-flipbook.views
  (:require
    [webchange.editor-v2.wizard.steps.fill-template-flipbook.views-authors :refer [authors]]
    [webchange.editor-v2.wizard.steps.fill-template-flipbook.views-cover-image :refer [cover-image]]
    [webchange.editor-v2.wizard.steps.fill-template-flipbook.views-illustrators :refer [illustrators]]
    [webchange.editor-v2.wizard.steps.fill-template-flipbook.views-layout :refer [layout]]
    [webchange.editor-v2.wizard.steps.fill-template-flipbook.views-title :refer [title]]
    [webchange.editor-v2.wizard.steps.fill-template-flipbook.views-control-row :refer [control-row]]
    [webchange.ui-framework.index :refer [button]]))

(defn- key->option
  [option-key template]
  (->> (get template :options [])
       (some (fn [{:keys [key] :as option}]
               (and (= key option-key) option)))))

(defn template-form
  [{:keys [data template validator]}]
  (let [common-props {:data      data
                      :validator validator}]
    [:div.flipbook-template-form
     [:h1 "Create Book"]
     [:div.content
      [:div.left-side
       [layout (assoc common-props :option (key->option "cover-layout" template))]]
      [:div.right-side
       [title (assoc common-props :option (key->option "cover-title" template))]
       [cover-image (assoc common-props :option (key->option "cover-image" template))]
       [authors (assoc common-props :option (key->option "authors" template))]
       [illustrators (assoc common-props :option (key->option "illustrators" template))]
       ;[control-row {:control [button {:size "big"
       ;                                :class-name "save-button"}
       ;                        "Save"]}]
       ]]]))
