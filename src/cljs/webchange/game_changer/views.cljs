(ns webchange.game-changer.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.ui-framework.components.index :refer [button message timeline]]
    [webchange.ui-framework.layout.views :refer [layout]]
    [webchange.game-changer.views-layout :as game-changer]
    [webchange.game-changer.template-options.update-timeline :refer [update-timeline]]
    [webchange.game-changer.create-activity.state :as state-create-activity]
    [webchange.game-changer.create-activity.views :refer [create-activity]]
    [webchange.game-changer.templates-list.views :refer [templates-list]]))

(declare game-changer-steps)
(def game-changer-steps [{:title          "Choose from a variety of activities..."
                          :timeline-label "Choose Activity"
                          :component      templates-list
                          :next-enabled?  (fn [{:keys [data]}] (some? (get-in data [:template :id])))
                          :handle-next    (fn [{:keys [data steps callback]}]
                                            (reset! steps (update-timeline game-changer-steps @data))
                                            (callback))}
                         {:title          "Name New Activity"
                          :timeline-label "Name Activity"
                          :component      create-activity}
                         {:title                 "Add Content"
                          :replace-with-options? true}])

(defn- not-defined-component
  []
  [message {:type    "warn"
            :message "Component not defined"}])

(defn- get-timeline-items
  [step-idx steps]
  (->> steps
       (map-indexed vector)
       (map (fn [[idx {:keys [title timeline-label]}]]
              {:title      (or timeline-label title)
               :active?    (= idx step-idx)
               :completed? (< idx step-idx)}))))

(defn- get-current-step-data
  [data step-idx steps]
  (let [step-data (nth steps step-idx)
        next-enabled-handler (get step-data :next-enabled? (fn [] true))]
    {:title         (get step-data :title "")
     :timeline      (get-timeline-items step-idx steps)
     :component     (get step-data :component not-defined-component)
     :handle-next   (get step-data :handle-next)
     :next-enabled? (next-enabled-handler {:data data})}))

(defn- form
  []
  (r/with-let [current-step (r/atom 0)
               current-data (r/atom {})
               steps (r/atom game-changer-steps)

               handle-prev-step (fn [] (swap! current-step dec))
               handle-next-step (fn [handle-next]
                                  (if (fn? handle-next)
                                    (handle-next {:data     current-data
                                                  :steps    steps
                                                  :callback #(swap! current-step inc)})
                                    (swap! current-step inc)))
               handle-finish (fn [data]
                               (re-frame/dispatch [::state-create-activity/create-activity data]))]
    (let [current-step-data (get-current-step-data @current-data @current-step @steps)
          {:keys [component title timeline handle-next next-enabled?]} current-step-data

          first-step? (= @current-step 0)
          last-step? (= @current-step (dec (count timeline)))

          actions (cond-> []
                          (not first-step?) (conj {:id      :prev-step
                                                   :text    "Previous"
                                                   :handler handle-prev-step
                                                   :props   {:variant "outlined"}})
                          (not last-step?) (conj {:id      :next-step
                                                  :text    "Next"
                                                  :props   {:disabled? (not next-enabled?)}
                                                  :handler #(handle-next-step handle-next)})
                          last-step? (conj {:id      :finish
                                            :text    "Finish"
                                            :handler #(handle-finish current-data)}))]
      [game-changer/layout {:title          title
                            :timeline-items timeline
                            :actions        actions}
       [component {:data current-data}]])))

(defn index
  []
  [layout {:show-navigation? false}
   [form]])
