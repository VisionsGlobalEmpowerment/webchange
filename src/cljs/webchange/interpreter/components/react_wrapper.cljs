(ns webchange.interpreter.components.react-wrapper
  (:require
    [react-konva :refer [Group]]
    [reagent.core :as r]))

(defn create-or-update
  [component-name params layer {:keys [get-component
                                       create-component
                                       update-component]}]
  (let [component (get-component component-name)]
    (if component
      (update-component component params)
      (.add layer (:node (create-component component-name params))))))

(defn component-react-wrapper-render
  [_ name params component-api]
  [:> Group
   {:x   0
    :y   0
    :ref #(when % (create-or-update name params (.getLayer %) component-api))}])

(defn component-react-wrapper-will-unmount
  [this]
  (let [props (rest (r/argv this))
        name (second props)
        {:keys [remove-component]} (last props)]
    (remove-component name)))

(def component-react-wrapper
  (with-meta component-react-wrapper-render
             {:component-will-unmount component-react-wrapper-will-unmount}))
