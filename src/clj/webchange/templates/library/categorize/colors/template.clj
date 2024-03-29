(ns webchange.templates.library.categorize.colors.template
  (:require
    [webchange.templates.utils.common :as common]
    [webchange.templates.utils.merge :as utils-merge]
    [webchange.templates.library.categorize.colors.round-0 :as round-0]
    [webchange.templates.library.categorize.colors.round-1 :as round-1]
    [webchange.templates.library.categorize.colors.round-2 :as round-2]
    [webchange.templates.library.categorize.colors.round-3 :as round-3]
    [webchange.templates.core :as core]))

(def m {:id          22
        :name        "Categorize - Colors"
        :tags        ["Independent Practice"]
        :description "Categorize"})

(defn prepare-templates
  []
  (let [pt0 (utils-merge/prepare-template round-0/template "r0" round-0/params-object-names round-0/var-object-names round-0/var-action-names round-0/all-vars-in-actions)
        pt1 (utils-merge/prepare-template round-1/template "r1" round-1/params-object-names round-1/var-object-names round-1/var-action-names round-1/all-vars-in-actions)
        pt2 (utils-merge/prepare-template round-2/template "r2" round-2/params-object-names round-2/var-object-names round-2/var-action-names round-2/all-vars-in-actions)
        pt3 (utils-merge/prepare-template round-3/template "r3" round-3/params-object-names round-3/var-object-names round-3/var-action-names round-3/all-vars-in-actions)
        rounds [pt0 pt1 pt2 pt3]]
    (utils-merge/basic-merge rounds)))

(defn f
  [args]
  (common/init-metadata m (prepare-templates) args))

(core/register-template m f)
