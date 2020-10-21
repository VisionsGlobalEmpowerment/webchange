(ns webchange.editor-v2.wizard.views-wizard
  (:require
    [reagent.core :as r]
    [cljs-react-material-ui.reagent :as ui]
    [webchange.editor-v2.layout.views :refer [layout]]
    [webchange.editor-v2.wizard.steps.test :as test]
    [webchange.editor-v2.wizard.steps.final-step :as final-step]))

(defn- get-styles
  []
  {:next-button {:margin-left "auto"}})

(defn- get-steps
  []
  (->> [:test]
       (map-indexed vector)))

(defn- get-step-content
  [steps step-index data]
  (if (< step-index (count steps))
    (case (->> step-index (nth steps) (second))
      :test (test/get-step {:data data}))
    (final-step/get-step {:data data})))

(defn wizard
  []
  (r/with-let [data (r/atom {})
               steps (get-steps)
               current-step-idx (r/atom 0)
               handle-back (fn [] (reset! current-step-idx (dec @current-step-idx)))
               handle-next (fn [] (reset! current-step-idx (inc @current-step-idx)))
               handle-finish (fn []
                               (reset! current-step-idx (inc @current-step-idx))
                               (print @data))
               styles (get-styles)]
    (let [current-step (get-step-content steps @current-step-idx data)
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
                  [ui/step-label (:label (get-step-content steps idx data))]])
               steps)]]
        [ui/grid {:item true :xs 9}
         [ui/card
          [ui/card-header {:title     (:header current-step)
                           :subheader (:sub-header current-step)}]
          [ui/card-content
           (:content current-step)]
          (when-not finished?
            [ui/card-actions
             (when-not first-step?
               [ui/button {:on-click handle-back} "Back"])
             (if last-step?
               [ui/button {:on-click handle-finish :style (:next-button styles)} "Finish"]
               [ui/button {:on-click handle-next :style (:next-button styles)} "Next"])])]]]])))
