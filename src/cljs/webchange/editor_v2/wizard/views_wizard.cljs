(ns webchange.editor-v2.wizard.views-wizard
  (:require
    [reagent.core :as r]
    [cljs-react-material-ui.reagent :as ui]
    [webchange.editor-v2.components.page-layout.views :refer [layout]]
    [webchange.editor-v2.wizard.steps.choose-template :as choose-template]
    [webchange.editor-v2.wizard.steps.fill-template :as fill-template]
    [webchange.editor-v2.wizard.steps.final-step :as final-step]
    [webchange.editor-v2.wizard.steps.name-activity :as name-activity]
    [webchange.editor-v2.wizard.steps.skills :as skills]
    [webchange.editor-v2.wizard.steps.welcome :as welcome]
    [webchange.editor-v2.wizard.validator :as validator :refer [connect-data]]
    [webchange.editor-v2.components.breadcrumbs.views :refer [root-breadcrumbs]]))

(defn- get-styles
  []
  {:actions     {:padding "12px"}
   :next-button {:margin-left "auto"}})

(def available-steps
  {:welcome         welcome/data
   :final           final-step/data
   :choose-template choose-template/data
   :name-activity   name-activity/data
   :skills          skills/data
   :fill-template   fill-template/data})

(defn- get-steps-labels
  [steps-keys]
  (->> steps-keys
       (map second)
       (map #(get-in available-steps [% :label]))))

(defn- get-steps
  []
  (->> [:choose-template
        :name-activity
        :skills
        :fill-template]
       (map-indexed vector)))

(defn- get-step-content
  [steps step-index data validator go-next-step]
  (cond
    (= step-index -1) (let [component (get-in available-steps [:welcome :component])]
                        {:content [component {:steps          (get-steps-labels steps)
                                              :on-start-click go-next-step}]})

    (< step-index (count steps)) (let [step-key (->> step-index (nth steps) (second))
                                       step (get available-steps step-key)
                                       component (get step :component)
                                       component-props (merge {:data      data
                                                               :validator validator}
                                                              (case step-key
                                                                :choose-template {:data-key :template}
                                                                :fill-template {:data-key :template-data
                                                                                :template (get-in @data [:template])}
                                                                :name-activity {:data-key :activity-data}
                                                                :skills {:data-key :skills}))]
                                   (-> step
                                       (dissoc :component)
                                       (assoc :content [component component-props])))

    :else (let [step (get available-steps :final)
                component (get step :component)]
            (-> step
                (dissoc :component)
                (assoc :content [component {:data data}])))))

(defn wizard
  []
  (r/with-let [data (r/atom {:template      {:id   24
                                             :name "flipbook"}
                             :activity-data {:language "q"
                                             :name     "q"
                                             :course   "q"}
                             :skills        {:level   "pre-k"
                                             :subject "english-native"
                                             :skills  [4]
                                             :topics  ["foundational-literacy--phonemic-awareness"]
                                             :strands ["foundational-literacy"]
                                             :concept 1}
                             :template-data {:cover-layout "title-top"
                                             :cover-title  "qqq"
                                             :cover-image  {:src "/upload/EKRTVCNPSMTGKTTG.png"}
                                             :authors      ["r"]
                                             :illustrators ["t"]}})
               steps (get-steps)
               current-step-idx (r/atom 4)
               {:keys [valid?] :as validator} (validator/init data)
               handle-back (fn [] (reset! current-step-idx (dec @current-step-idx)))
               handle-next (fn []
                             (when (valid?)
                               (reset! current-step-idx (inc @current-step-idx))))
               styles (get-styles)]
    (print "@data" @data)
    (print "@current-step-idx" @current-step-idx)
    (let [current-step (get-step-content steps @current-step-idx data validator handle-next)
          first-step? (= @current-step-idx 0)
          last-step? (->> (count steps) (dec) (= @current-step-idx))
          started? (> @current-step-idx -1)
          finished? (= @current-step-idx (count steps))]
      [layout {:breadcrumbs (root-breadcrumbs "Create Activity Wizard")}
       [:div {:style {:display "flex"}}
        [:div {:style {:flex      "0 1"
                       :min-width "180px"}}
         (when-not (= @current-step-idx -1)
           [ui/stepper {:active-step @current-step-idx
                        :orientation "vertical"}
            (->> (get-steps-labels steps)
                 (map (fn [step]
                        ^{:key step}
                        [ui/step [ui/step-label step]]))
                 (doall))])]
        [:div {:style {:flex      "1 1"
                       :max-width "1100px"}}
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
              (if last-step? "Finish" "Next")]])]]]
       ])))
