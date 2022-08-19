(ns webchange.lesson-builder.tools.background-image.index
  (:require
    [webchange.lesson-builder.tools.background-image.state :as state]
    [webchange.lesson-builder.tools.background-image.views :refer [background-image]]))

(def toolbox background-image)

(def data {:toolbox true
           :focus   #{:toolbox :stage}
           :init    [::state/init]})
