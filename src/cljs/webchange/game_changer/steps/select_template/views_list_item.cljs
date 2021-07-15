(ns webchange.game-changer.steps.select_template.views-list-item
  (:require
   [reagent.core :as r]
   [webchange.game-changer.steps.select_template.views-preview :refer [activity-preview]]
   [webchange.editor-v2.layout.components.sandbox.create-link :refer [create-link]]
   [webchange.ui-framework.components.index :refer [button]]
   [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn- preview-button
  [{:keys [activity-slug course-slug]}]
  (let [link (create-link {:course-slug course-slug
                           :scene-slug  activity-slug})]
    [button {:href       link
             :class-name "preview-button"}
     "Preview"]))

(defn- start-button
  ;; [handler props]
  [props]
  (js/console.log "props------------------->>>>>>>>" props)
  [button
   (merge {:class-name "preview-button"
           :on-click   (:on-click props)
           :color      "primary"}
          props)
   "Start"])

(defn template-list-item
  [{:keys [template on-click selected? actions]}]
  (r/with-let [slideshow-visible? (r/atom false)
               handle-mouse-enter (fn [] (reset! slideshow-visible? true))
               handle-mouse-leave (fn [] (reset! slideshow-visible? false))]
    (let [{:keys [description name props] :or {props {}}} template
          {:keys [preview preview-activity preview-anim]} props]
      [:div.templates-list-item-place-holder
       [:div {:class-name     (get-class-name {"templates-list-item" true
                                               "selected"            selected?})
              :on-click       #(on-click template)
              :on-mouse-enter handle-mouse-enter
              :on-mouse-leave handle-mouse-leave}
        [:div {:class-name (get-class-name {"preview"     true
                                            "placeholder" (not (some? preview))})
               :style      (cond-> {}
                             (some? preview) (assoc :background-image (str "url(" preview ")")))}
         (when (and (some? preview-anim)
                    @slideshow-visible?)
           [activity-preview {:slides preview-anim}])]
        [:div.title
         [:span name]
         [:div.title-button
          (when (some? preview-activity)
            [preview-button preview-activity])
          (when selected?
            (for [{:keys [id text handler props] :or {props {}}} actions]
              ^{:key id}
              ;; [start-button handler props]
              [start-button (merge props {:on-click handler})]))]
         [:div.description.clearfix
          description]]]])))
