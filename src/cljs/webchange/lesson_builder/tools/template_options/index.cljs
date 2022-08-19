(ns webchange.lesson-builder.tools.template-options.index
  (:require
    [webchange.lesson-builder.tools.template-options.rhyming-sides.views :refer [overlay]]
    [webchange.lesson-builder.tools.template-options.views :refer [template-options]]))

(def menu {:rhyming-side     overlay
           :template-options template-options})

(def data {:menu true})
