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
  [steps step-index step-component-props]
  (if (< step-index (count steps))
    (let [step-key (->> step-index (nth steps) (second))
          component-props (merge step-component-props
                                 {:data-key step-key})]
      (case step-key
        :choose-template (choose-template/get-step component-props)
        :fill-template (fill-template/get-step component-props)
        :name-activity (name-activity/get-step component-props)
        :skills (skills/get-step component-props)))
    (final-step/get-step step-component-props)))

(defn wizard
  []
  (r/with-let [data (r/atom {})
               steps (get-steps)
               current-step-idx (r/atom 2)
               {:keys [valid?] :as validator} (validator/init data)
               handle-back (fn [] (reset! current-step-idx (dec @current-step-idx)))
               handle-next (fn []
                             (when (valid?)
                               (reset! current-step-idx (inc @current-step-idx))))
               styles (get-styles)]
    (let [current-step (get-step-content steps @current-step-idx {:data      data
                                                                  :validator validator})
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
                  [ui/step-label (:label (get-step-content steps idx {:data      data
                                                                      :validator validator}))]])
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
