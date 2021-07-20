(ns webchange.interpreter.renderer.scene.components.question.wrapper
  (:require
    [webchange.interpreter.renderer.scene.components.question.create-multiple-choice-image :as multiple-choice-image]
    [webchange.interpreter.renderer.scene.components.wrapper :refer [create-wrapper]]))

(defn- get-children
  [{:keys [question-type] :as props}]
  (case question-type
    "multiple-choice-image" (multiple-choice-image/create props)
    []))

(defn wrap
  [type name container props]
  (create-wrapper {:name         name
                   :type         type
                   :object       container
                   :container    container
                   :get-children #(get-children props)
                   :set-src (fn [src options]
                              (print "Set question src")
                              (print "src" src)
                              (print "options" options))}))
