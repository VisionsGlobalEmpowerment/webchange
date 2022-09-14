(ns webchange.lesson-builder.tools.flipbook-add-page.index
  (:require
    [webchange.lesson-builder.tools.flipbook-add-page.views :refer [layout-form select-layout]]))

(def toolbox select-layout)
(def menu layout-form)

(def data {:toolbox true
           :menu    true
           :focus   #{:toolbox :menu}})
