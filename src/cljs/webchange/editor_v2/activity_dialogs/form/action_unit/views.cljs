(ns webchange.editor-v2.activity-dialogs.form.action-unit.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.activity-dialogs.form.action-unit.views-menu :refer [unit-menu]]
    [webchange.editor-v2.activity-dialogs.form.action-unit.views-animation :refer [animation-unit]]
    [webchange.editor-v2.activity-dialogs.form.action-unit.views-effect :refer [effect-unit]]
    [webchange.editor-v2.dialog.dialog-text-form.action-unit.views-skip :refer [skip-unit]]
    [webchange.editor-v2.activity-dialogs.form.action-unit.views-phrase :refer [phrase-unit]]
    [webchange.editor-v2.activity-dialogs.form.action-unit.views-text-animation :refer [text-animation-unit]]
    [webchange.editor-v2.activity-dialogs.form.state :as state]
    [webchange.logger.index :as logger]
    [webchange.ui-framework.components.utils :refer [get-class-name]]
    [webchange.utils.drag-and-drop :as utils]))

(defn- unknown-element
  [{:keys [type] :as props}]
  (logger/warn "Unknown element type: " type)
  (logger/trace-folded "Props" props)
  [:div.unknown-unit "Not editable action"])

(defn drag-event->drop-target
  [event parallel-available?]
  (if-let [target (.. event -target)]
    (let [offset-y (.-offsetY event)
          target-height (.. target -clientHeight)]
      (if parallel-available?
        (cond
          (< offset-y (/ target-height 3)) :before
          (> offset-y (* (/ target-height 3) 2)) :after
          :else :parallel)
        (if (< offset-y (/ target-height 2))
          :before
          :after)))))

(defn action-unit
  [{:keys [idx parallel-mark path type selected?] :as props}]
  (r/with-let [container-ref (r/atom nil)
               handle-click #(re-frame/dispatch [::state/set-selected-action props])

               ;; d&d
               drop-target (r/atom nil)

               prevent-defaults #(do (.preventDefault %)
                                     (.stopPropagation %))
               handle-drag-enter #(prevent-defaults %)
               handle-drag-leave #(do (prevent-defaults %) (reset! drop-target nil))
               handle-drag-over (fn [event]
                                  (let [parallel-action-available? (= parallel-mark :none)]
                                    (prevent-defaults event)
                                    (reset! drop-target (drag-event->drop-target event parallel-action-available?))))
               handle-drop #(do (prevent-defaults %)
                                (re-frame/dispatch [::state/handle-drag-n-drop (merge (utils/get-transfer-data %)
                                                                                      {:target-type       type
                                                                                       :target-path       path
                                                                                       :relative-position @drop-target})])
                                (reset! drop-target nil))

               init-dnd (fn []
                          (.addEventListener @container-ref "dragenter" handle-drag-enter)
                          (.addEventListener @container-ref "dragleave" handle-drag-leave)
                          (.addEventListener @container-ref "dragover" handle-drag-over)
                          (.addEventListener @container-ref "drop" handle-drop))]
    [:div {:ref        #(when (some? %)
                          (reset! container-ref %)
                          (init-dnd))
           :on-click   handle-click
           :class-name (get-class-name {"action-unit"     true
                                        "parallel"        (not= parallel-mark :none)
                                        "parallel-start"  (= parallel-mark :start)
                                        "parallel-middle" (= parallel-mark :middle)
                                        "parallel-end"    (= parallel-mark :end)
                                        "selected"        selected?
                                        "drop-before"     (= @drop-target :before)
                                        "drop-after"      (= @drop-target :after)
                                        "drop-parallel"   (= @drop-target :parallel)})}
     (case type
       :character-animation [animation-unit props]
       :effect [effect-unit props]
       :phrase [phrase-unit props]
       :text-animation [text-animation-unit props]
       :skip [skip-unit props]
       [unknown-element props])
     [unit-menu {:idx         idx
                 :action-data props}]]
    (finally
      (.removeEventListener @container-ref "dragenter" handle-drag-enter)
      (.removeEventListener @container-ref "dragleave" handle-drag-leave)
      (.removeEventListener @container-ref "dragover" handle-drag-over)
      (.removeEventListener @container-ref "drop" handle-drop))))
