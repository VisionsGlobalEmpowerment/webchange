(ns webchange.editor-v2.activity-dialogs.form.action-unit.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.activity-dialogs.form.action-unit.views-menu :refer [unit-menu]]
    [webchange.editor-v2.activity-dialogs.form.action-unit.views-animation :refer [animation-unit]]
    [webchange.editor-v2.activity-dialogs.form.action-unit.views-background-music :refer [background-music-unit]]
    [webchange.editor-v2.activity-dialogs.form.action-unit.views-effect :refer [effect-unit]]
    [webchange.editor-v2.activity-dialogs.form.action-unit.views-guide :refer [guide-unit]]
    [webchange.editor-v2.activity-dialogs.form.action-unit.views-movement :refer [movement-unit]]
    [webchange.editor-v2.activity-dialogs.form.action-unit.views-skip :refer [skip-unit]]
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

(defn- inside-parallel-action?
  [parallel-mark]
  (not= parallel-mark :none))

(defn- drag-event->drop-target
  [event parallel-mark]
  (if-let [target (.. event -target)]
    (let [offset-y (.-offsetY event)
          offset-x (.-offsetX event)
          target-height (.. target -clientHeight)]
      (if-not (inside-parallel-action? parallel-mark)
        (cond
          (< offset-y (/ target-height 3)) :before
          (> offset-y (* (/ target-height 3) 2)) :after
          :else :parallel)
        (let [order (if (< offset-y (/ target-height 2))
                      :before :after)
              nesting (if (and (< offset-x 100)
                               (or (and (= order :before)
                                        (= parallel-mark :start))
                                   (and (= order :after)
                                        (= parallel-mark :end))))
                        :outside :inside)]
          (case nesting
            :inside (case order
                      :after :after-inside
                      :before :before-inside)
            :outside order))))))

(defn- get-drop-position
  [path drop-target parallel-mark]
  {:target-path       (if (and (inside-parallel-action? parallel-mark)
                               (some #{drop-target} [:after :before]))
                        (drop-last 2 path)
                        path)
   :relative-position (case drop-target
                        :after-inside :after
                        :before-inside :before
                        drop-target)})

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
                                  (prevent-defaults event)
                                  (reset! drop-target (drag-event->drop-target event parallel-mark)))
               handle-drop #(do (prevent-defaults %)
                                (let [{:keys [target-path relative-position]} (get-drop-position path @drop-target parallel-mark)]
                                  (re-frame/dispatch [::state/handle-drag-n-drop (merge (utils/get-transfer-data %)
                                                                                        {:target-type       type
                                                                                         :target-path       target-path
                                                                                         :relative-position relative-position})])
                                  (reset! drop-target nil)))

               init-dnd (fn []
                          (.addEventListener @container-ref "dragenter" handle-drag-enter)
                          (.addEventListener @container-ref "dragleave" handle-drag-leave)
                          (.addEventListener @container-ref "dragover" handle-drag-over true)
                          (.addEventListener @container-ref "drop" handle-drop))]
    [:div {:ref        #(when (and (nil? @container-ref) (some? %))
                          (reset! container-ref %)
                          (init-dnd))
           :on-click   handle-click
           :class-name (get-class-name {"action-unit"     true
                                        "parallel"        (not= parallel-mark :none)
                                        "parallel-start"  (= parallel-mark :start)
                                        "parallel-middle" (= parallel-mark :middle)
                                        "parallel-end"    (= parallel-mark :end)
                                        "selected"        selected?
                                        "drop-target"     (some? @drop-target)
                                        "drop-before"     (some #{@drop-target} [:before :before-inside])
                                        "drop-after"      (some #{@drop-target} [:after :after-inside])
                                        "drop-inside"     (some #{@drop-target} [:after-inside :before-inside])
                                        "drop-parallel"   (= @drop-target :parallel)})}
     (case type
       :character-animation [animation-unit props]
       :character-movement [movement-unit props]
       :effect [effect-unit props]
       :phrase [phrase-unit props]
       :text-animation [text-animation-unit props]
       :skip [skip-unit props]
       :background-music [background-music-unit props]
       :guide [guide-unit props]
       [unknown-element props])
     [unit-menu {:idx         idx
                 :action-data props}]]
    (finally
      (.removeEventListener @container-ref "dragenter" handle-drag-enter)
      (.removeEventListener @container-ref "dragleave" handle-drag-leave)
      (.removeEventListener @container-ref "dragover" handle-drag-over)
      (.removeEventListener @container-ref "drop" handle-drop))))
