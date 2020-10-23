(ns webchange.editor-v2.wizard.views-activity-skills
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.editor-v2.wizard.common-styles :as styles]
    [webchange.editor-v2.wizard.state.activity :as state-activity]))

(defn- get-styles
  []
  (merge (styles/activity)
         {:skills-list      {:padding 0}
          :skills-list-item {:padding-left 0
                             :white-space  "normal"}}))

(defn- render-selected
  [{:keys [values options]}]
  (let [value->option (fn [current-value]
                        (some (fn [{:keys [value] :as option}] (and (= value current-value) option)) options))
        selected-items (->> values
                            (map value->option)
                            (remove nil?))
        styles (get-styles)]
    [ui/list {:style (:skills-list styles)}
     (for [{:keys [value text]} selected-items]
       ^{:key value}
       [ui/list-item {:style (:skills-list-item styles)}
        text])]))

(defn- select-control
  [{:keys [label value options multiple? on-change on-close]
    :or   {label     "Label"
           options   []
           multiple? false
           on-change #()
           on-close  #()}}]
  (r/with-let [open? (r/atom false)]
    (let [styles (get-styles)]
      [ui/form-control {:full-width true
                        :style      (:control-container styles)}
       [ui/input-label label]
       [ui/select {:value        (or value (if multiple? [] ""))
                   :multiple     multiple?
                   :render-value (fn [value]
                                   (->> (fn []
                                          (let [value (js->clj value)]
                                            [render-selected {:values  (if (sequential? value) value [value])
                                                              :options options}]))
                                        (r/reactify-component)
                                        (r/create-element)))
                   :open         @open?
                   :on-open      #(reset! open? true)
                   :on-close     #(do (reset! open? false)
                                      (on-close))
                   :on-change    #(on-change (js->clj (-> % .-target .-value)))}
        (for [{:keys [value text]} options]
          ^{:key value}
          [ui/menu-item {:value value} text])]])))

(defn skills
  [data]
  (r/with-let [current-strands (r/atom [])
               current-topics (r/atom [])
               panels-expanded (r/atom {:strands true
                                        :topics  false
                                        :skills  false})]
    (let [{:keys [strands topics skills] :as skills-data} @(re-frame/subscribe [::state-activity/skills])

          strands-options (->> strands
                               (map (fn [[key name]] {:value (clojure.core/name key) :text name})))
          handle-strand-changed (fn [strands]
                                  (reset! current-strands (if (sequential? strands) strands [strands]))
                                  (reset! current-topics [])
                                  (swap! data assoc :skills []))

          topics-options (->> topics
                              (filter (fn [[_ {:keys [strand]}]]
                                        (some #{strand} @current-strands)))
                              (map (fn [[key {:keys [name]}]] {:value (clojure.core/name key) :text name}))
                              (doall))
          handle-topic-changed (fn [topics]
                                 (reset! current-topics (if (sequential? topics) topics [topics]))
                                 (swap! data assoc :skills []))
          skills-options (->> skills
                              (filter (fn [{:keys [topic]}]
                                        (some #{topic} @current-topics)))
                              (map (fn [{:keys [id name]}] {:value id :text name}))
                              (doall))
          handle-skills-changed (fn [skills] (swap! data assoc :skills skills))]
      (if (some? skills-data)
        [:div
         [ui/expansion-panel {:expanded  (get @panels-expanded :strands)
                              :on-change #(swap! panels-expanded update :strands not)}
          [ui/expansion-panel-summary
           [ui/typography "Strand"]]
          [ui/expansion-panel-details
           [select-control {:label     "Select Strands"
                            :value     @current-strands
                            :options   strands-options
                            :multiple? true
                            :on-change handle-strand-changed
                            :on-close  #(do (swap! panels-expanded assoc :strands false)
                                            (swap! panels-expanded assoc :topics true))}]]]
         [ui/expansion-panel {:expanded  (get @panels-expanded :topics)
                              :on-change #(swap! panels-expanded update :topics not)}
          [ui/expansion-panel-summary
           [ui/typography "Topic"]]
          [ui/expansion-panel-details
           [select-control {:label     "Select Topics"
                            :value     @current-topics
                            :options   topics-options
                            :multiple? true
                            :on-change handle-topic-changed
                            :on-close  #(do (swap! panels-expanded assoc :topics false)
                                            (swap! panels-expanded assoc :skills true))}]]]
         [ui/expansion-panel {:expanded  (get @panels-expanded :skills)
                              :on-change #(swap! panels-expanded update :skills not)}
          [ui/expansion-panel-summary
           [ui/typography "Skills"]]
          [ui/expansion-panel-details
           [select-control {:label     "Select Skills"
                            :value     (get @data :skills [])
                            :options   skills-options
                            :multiple? true
                            :on-change handle-skills-changed}]]]]
        [ui/circular-progress]))))
