(ns webchange.lesson-builder.tools.object-form.views
  (:require
    [reagent.core :as r]
    [re-frame.core :as re-frame]
    [webchange.lesson-builder.tools.object-form.state :as state]
    [webchange.lesson-builder.tools.object-form.animation-form.views :as animation-form]
    [webchange.lesson-builder.tools.object-form.text-tracing-pattern-form.views :as text-tracing-pattern-form]
    [webchange.lesson-builder.tools.object-form.text-form.views :as text-form]
    [webchange.lesson-builder.tools.object-form.image-form.views :as image-form]
    [webchange.lesson-builder.tools.object-form.video-form.views :as video-form]
    [webchange.ui.index :as ui]))

(def form-components {"animation"            animation-form/fields
                      "text-tracing-pattern" text-tracing-pattern-form/fields
                      "text"                 text-form/fields
                      "image"                image-form/fields
                      "video"                video-form/fields})

(defn- available-object-type?
  [object-type]
  (contains? form-components object-type))

(defn- object-panel
  [{:keys [class-name target]}]
  (let [{object-type :type} @(re-frame/subscribe [::state/object target])]
    (when (available-object-type? object-type)
      (let [component (get form-components object-type)]
        [component {:target     target
                    :class-name class-name}]))))

(defn- group-panel
  [{:keys [group]}]
  (re-frame/dispatch [::state/init-group group])
  (fn [{:keys [class-name group]}]
    [:div {:class-name (ui/get-class-name {"group-panel" true
                                           class-name    (some? class-name)})}
     (for [child (:children group)]
       [object-panel {:target (keyword child)}])]))

(defn- panels
  [target]
  (r/with-let [_ (re-frame/dispatch [::state/init-object target])]
    (let [target @(re-frame/subscribe [::state/target])
          object @(re-frame/subscribe [::state/object target])
          group? (= "group" (:type object))]
      [:div {:class-name "widget--object-form"}
       [:h1 "Edit"]
       (if group?
         [group-panel {:group      object
                       :class-name "object-form--form"}]
         [object-panel {:target     target
                        :class-name "object-form--form"}])
       [ui/button {:class-name "object-form--apply"
                   :on-click   #(re-frame/dispatch [::state/apply])}
        "Apply"]])
    (finally
      (re-frame/dispatch [::state/reset]))))

(defn object-form
  []
  (let [target @(re-frame/subscribe [::state/target])]
    ^{:key target}
    [panels target]))
