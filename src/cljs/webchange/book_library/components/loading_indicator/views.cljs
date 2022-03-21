(ns webchange.book-library.components.loading-indicator.views
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.i18n.translate :as i18n]))

(defn- init-timer
  [callback]
  (js/setInterval callback 500))

(defn- reset-timer
  [timer-id]
  (js/clearInterval timer-id))

(defn loading-indicator
  []
  (r/with-let [counter (r/atom 0)
               handle-tick (fn []
                             (if (= @counter 3)
                               (reset! counter 0)
                               (swap! counter inc)))
               timer-id (atom (init-timer handle-tick))]
    [:div.loading-indicator
     (apply str (concat [@(re-frame/subscribe [::i18n/t [:loading]])] (take @counter (repeat "."))))]
    (finally
      (reset-timer @timer-id))))
