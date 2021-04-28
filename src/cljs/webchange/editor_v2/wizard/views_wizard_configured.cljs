(ns webchange.editor-v2.wizard.views-wizard-configured
  (:require
    [reagent.core :as r]
    [cljs-react-material-ui.reagent :as ui]
    [webchange.editor-v2.components.page-layout.views :refer [layout]]
    [webchange.editor-v2.wizard.steps.choose-template :as choose-template]
    [webchange.editor-v2.wizard.steps.fill-template :as fill-template]
    [webchange.editor-v2.wizard.steps.final-step-configured :as final-step]
    [webchange.editor-v2.wizard.validator :as validator :refer [connect-data]]
    [webchange.editor-v2.components.breadcrumbs.views :refer [course-breadcrumbs root-breadcrumbs]]))

(defn- get-styles
  []
  {:actions     {:padding "12px"}
   :next-button {:margin-left "auto"}})

(def available-steps
  {:final           final-step/data
   :choose-template choose-template/data
   :fill-template   fill-template/data})

(defn- get-steps-labels
  [steps-keys]
  (->> steps-keys
       (map second)
       (map #(get-in available-steps [% :label]))))

(defn- get-steps
  []
  (->> [:choose-template
        :fill-template]
       (map-indexed vector)))

(defn- get-step-content
  [steps step-index data validator]
  (cond
    (< step-index (count steps)) (let [step-key (->> step-index (nth steps) (second))
                                       step (get available-steps step-key)
                                       component (get step :component)
                                       component-props (merge {:data      data
                                                               :validator validator}
                                                              (case step-key
                                                                :choose-template {:data-key :template}
                                                                :fill-template {:data-key    :template-data
                                                                                :template (get-in @data [:template])}))]
                                   (-> step
                                       (dissoc :component)
                                       (assoc :content [component component-props])))

    :else (let [step (get available-steps :final)
                component (get step :component)]
            (-> step
                (dissoc :component)
                (assoc :content [component {:data data}])))))

(defn wizard
  [course-slug scene-slug]
  (r/with-let [data (r/atom {:course-slug course-slug :scene-slug scene-slug})
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
          started? (> @current-step-idx -1)
          finished? (= @current-step-idx (count steps))]
      [layout {:breadcrumbs (course-breadcrumbs course-slug "Create Activity Wizard")}
       [ui/grid {:container true
                 :spacing   32}
        [ui/grid {:item true :xs 2}
         (when-not (= @current-step-idx -1)
           [ui/stepper {:active-step @current-step-idx
                        :orientation "vertical"}
            (->> (get-steps-labels steps)
                 (map (fn [step]
                        ^{:key step}
                        [ui/step [ui/step-label step]]))
                 (doall))])]
        [ui/grid {:item true :xs 8}
         [ui/card
          [ui/card-header {:title     (:header current-step)
                           :subheader (:sub-header current-step)}]
          [ui/card-content
           (:content current-step)]
          (when (and started?
                     (not finished?))
            [ui/card-actions {:style (:actions styles)}
             (when-not first-step?
               [ui/button {:on-click handle-back} "Back"])
             [ui/button {:on-click handle-next
                         :color    "primary"
                         :style    (:next-button styles)}
              (if last-step? "Finish" "Next")]])]]]])))
