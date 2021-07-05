(ns webchange.editor-v2.dialog.dialog-text-form.action-unit.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.dialog.dialog-text-form.menu.views :refer [unit-menu]]
    [webchange.editor-v2.dialog.dialog-text-form.action-unit.views-effect :refer [effect-unit]]
    [webchange.editor-v2.dialog.dialog-text-form.action-unit.views-phrase :refer [phrase-unit]]
    [webchange.editor-v2.dialog.dialog-text-form.action-unit.views-text-animation :refer [text-animation-unit]]
    [webchange.editor-v2.dialog.dialog-text-form.state :as state]
    [webchange.logger.index :as logger]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn- unknown-element
  [{:keys [type] :as props}]
  (logger/warn "Unknown element type: " type)
  (logger/trace-folded "Props" props)
  [:div.unknown-unit "Not editable action"])

(defn action-unit
  [{:keys [idx parallel-mark type selected?] :as props}]
  (r/with-let [container-ref (r/atom nil)
               handle-click #(re-frame/dispatch [::state/set-selected-action props])]
    [:div {:ref        #(when (some? %) (reset! container-ref %))
           :on-click   handle-click
           :class-name (get-class-name {"action-unit"     true
                                        "parallel"        (not= parallel-mark :none)
                                        "parallel-start"  (= parallel-mark :start)
                                        "parallel-middle" (= parallel-mark :middle)
                                        "parallel-end"    (= parallel-mark :end)
                                        "selected"        selected?})}
     (case type
       :effect [effect-unit props]
       :phrase [phrase-unit props]
       :text-animation [text-animation-unit props]
       [unknown-element props])
     [unit-menu {:idx         idx
                 :parent-ref  @container-ref
                 :action-data props}]]))
