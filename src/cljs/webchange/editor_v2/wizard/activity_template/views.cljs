(ns webchange.editor-v2.wizard.activity-template.views
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [webchange.editor-v2.wizard.activity-template.views-characters :refer [characters-option]]
    [webchange.editor-v2.wizard.activity-template.views-lookup :refer [lookup-option]]
    [webchange.editor-v2.wizard.activity-template.views-pages :refer [pages-option]]
    [webchange.editor-v2.wizard.activity-template.views-string :refer [string-option]]))

(defn- option-info
  [{:keys [option] :as props}]
  (case (:type option)
    "characters" [characters-option props]
    "lookup" [lookup-option props]
    "pages" [pages-option props]
    "string" [string-option props]
    nil))

(defn template
  [{:keys [template data validator]}]
  [ui/grid {:container   true
            :justify     "space-between"
            :spacing     24
            :align-items "center"}
   (for [[key option] (:options template)]
     ^{:key key}
     [ui/grid {:item true :xs 12}
      [option-info {:key       key
                    :option    option
                    :data      data
                    :validator validator}]])])
