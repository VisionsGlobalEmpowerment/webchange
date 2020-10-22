(ns webchange.editor-v2.wizard.views-wizard
  (:require
    [reagent.core :as r]
    [cljs-react-material-ui.reagent :as ui]
    [webchange.editor-v2.layout.views :refer [layout]]
    [webchange.editor-v2.wizard.steps.choose-template :as choose-template]
    [webchange.editor-v2.wizard.steps.fill-template :as fill-template]
    [webchange.editor-v2.wizard.steps.final-step :as final-step]
    [webchange.editor-v2.wizard.steps.name-activity :as name-activity]
    [webchange.editor-v2.wizard.steps.skills :as skills]
    [webchange.editor-v2.wizard.validator :as validator]))

(defn- get-styles
  []
  {:actions     {:padding "12px"}
   :next-button {:margin-left "auto"}})

(defn- get-steps
  []
  (->> [:choose-template
        :name-activity
        :skills
        :fill-template]
       (map-indexed vector)))

(defn- get-step-content
  [steps step-index data validator]
  (if (< step-index (count steps))
    (let [step-key (->> step-index (nth steps) (second))
          component-props {:data      data
                           :validator validator}]
      (case step-key
        :choose-template (->> {:data-key :template-id}
                              (merge component-props)
                              (choose-template/get-step))
        :fill-template (->> {:data-key    :template-data
                             :template-id (get-in @data [:template-id])}
                            (merge component-props)
                            (fill-template/get-step))
        :name-activity (->> {:data-key :activity-data}
                            (merge component-props)
                            (name-activity/get-step))
        :skills (->> {:data-key :skills}
                     (merge component-props)
                     (skills/get-step))))
    (final-step/get-step {:data      data
                          :validator validator})))

(defn wizard
  []
  (r/with-let [data (r/atom {})
               steps (get-steps)
               current-step-idx (r/atom 0)
               {:keys [valid?] :as validator} (validator/init data)
               handle-back (fn [] (reset! current-step-idx (dec @current-step-idx)))
               handle-next (fn []
                             (when (valid?)
                               (reset! current-step-idx (inc @current-step-idx))))
               styles (get-styles)]
    (let [current-step (get-step-content steps @current-step-idx data validator)
          first-step? (= @current-step-idx 0)
          last-step? (->> (count steps) (dec) (= @current-step-idx))
          finished? (= @current-step-idx (count steps))]
      [layout {:title "Create Activity Wizard"}
       [ui/grid {:container true
                 :spacing   32}
        [ui/grid {:item true :xs 3}
         [ui/stepper {:active-step @current-step-idx
                      :orientation "vertical"}
          (map (fn [[idx]]
                 ^{:key idx}
                 [ui/step
                  [ui/step-label (:label (get-step-content steps idx data validator))]])
               steps)]]
        [ui/grid {:item true :xs 9}
         [ui/card
          [ui/card-header {:title     (:header current-step)
                           :subheader (:sub-header current-step)}]
          [ui/card-content
           (:content current-step)]
          (when-not finished?
            [ui/card-actions {:style (:actions styles)}
             (when-not first-step?
               [ui/button {:on-click handle-back} "Back"])
             [ui/button {:on-click handle-next
                         :color    "primary"
                         :style    (:next-button styles)}
              (if last-step? "Finish" "Next")]])]]]])))
