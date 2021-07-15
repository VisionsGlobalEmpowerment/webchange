(ns webchange.game-changer.views
  (:require
   [reagent.core :as r]
   [webchange.ui-framework.components.index :refer [button message timeline]]
   [webchange.ui-framework.layout.views :refer [layout]]
   [webchange.game-changer.views-layout :as game-changer]
   [webchange.game-changer.steps.create-activity.index :as create-activity]
   [webchange.game-changer.steps.fill-template.index :as fill-template]
   [webchange.game-changer.steps.select-background.index :as select-background]
   [webchange.game-changer.steps.select-background-music.index :as select-background-music]
   [webchange.game-changer.steps.select-character.index :as select-character]
   [webchange.game-changer.steps.select-template.index :as select-template]
   [webchange.game-changer.steps.welcome-message.index :as welcome-message]
   [webchange.logger.index :as logger]))

(def game-changer-steps [welcome-message/data
                         select-template/data
                         select-character/data
                         create-activity/data
                         fill-template/data
                         select-background/data
                         select-background-music/data])

(defn- not-defined-component
  []
  [message {:type    "warn"
            :message "Component not defined"}])

(defn- get-timeline-items
  [step-idx steps data]
  (->> steps
       (map-indexed vector)
       (map (fn [[idx {:keys [title timeline-label available?]
                       :or   {available? (constantly true)}}]]
              {:title      (or timeline-label title)
               :active?    (= idx step-idx)
               :completed? (< idx step-idx)
               :disabled?  (not (available? {:data data}))}))))

(defn- get-current-step-data
  [data step-idx steps]
  (let [step-data (nth steps step-idx)
        next-enabled-handler (get step-data :passed? (constantly true))
        timeline (get-timeline-items step-idx steps data)]
    {:step-id       (get step-data :step-id)
     :title         (get step-data :title "")
     :timeline      timeline
     :component     (get step-data :component not-defined-component)
     :handle-next   (get step-data :handle-next)
     :next-enabled? (next-enabled-handler {:data data})
     :next-step-idx (->> (map-indexed vector timeline)
                         (some (fn [[index {:keys [disabled?]}]]
                                 (and (> index step-idx)
                                      (not disabled?)
                                      index))))
     :prev-step-idx (->> (map-indexed vector timeline)
                         (reverse)
                         (some (fn [[index {:keys [disabled?]}]]
                                 (and (< index step-idx)
                                      (not disabled?)
                                      index))))}))

(defn- form
  []
  (r/with-let [current-step (r/atom 0)
               current-data (r/atom {})
               steps (r/atom game-changer-steps)

               handle-prev-step (fn [step-idx] (reset! current-step step-idx))
               handle-next-step (fn [next-idx handle-next]
                                  (if (fn? handle-next)
                                    (handle-next {:data     current-data
                                                  :callback #(reset! current-step next-idx)})
                                    (reset! current-step next-idx)))]
    (logger/trace-folded "Game changer data" @current-data)
    (let [current-step-data (get-current-step-data @current-data @current-step @steps)
          {:keys [component step-id title timeline handle-next next-enabled? next-step-idx prev-step-idx]} current-step-data
          first-step? (= @current-step 0)
          last-step? (= @current-step (dec (count timeline)))
          select-template-step? (= step-id 2)
          actions (cond->> [{:id      :next-step
                             :text    (if last-step? "Finish" "Next")
                             :props   {:disabled? (not next-enabled?)}
                             :handler #(handle-next-step next-step-idx handle-next)}]
                    (and (not first-step?) (not select-template-step?)) (concat [{:id      :prev-step
                                                                                  :text    "Previous"
                                                                                  :handler #(handle-prev-step prev-step-idx)
                                                                                  :props   {:variant "outlined"}}]))]
      [game-changer/layout {:step-id        step-id
                            :title          title
                            :timeline-items timeline
                            :actions        actions}
       [component {:data current-data :actions actions}]])))

(defn index
  []
  [layout {:show-navigation? false}
   [form]])
