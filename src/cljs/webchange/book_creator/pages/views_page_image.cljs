(ns webchange.book-creator.pages.views-page-image
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [webchange.book-creator.pages.state :as state-pages]
    [webchange.state.state-flipbook :as state-flipbook]
    [webchange.ui-framework.components.utils :refer [get-class-name]]))

(defn- page-image-view
  [{:keys [class-name draggable? ref side src stage]}]
  [:div (cond-> {:class-name (get-class-name (-> {"stage-image" true}
                                                 (merge class-name)
                                                 (assoc (str " stage-image-" side) true)))
                 :style      {:background-image (str "url(" src ")")}
                 :data-side  side
                 :data-stage stage
                 :draggable  draggable?}
                (fn? ref) (assoc :ref #(when (some? %) (ref %))))])

(defn- parse-int
  [value]
  (.parseInt js/Number value))

(defn drag-event->page-side
  [event]
  (if-let [target (.. event -target)]
    (let [offset-x (.-offsetX event)
          target-width (.. target -clientWidth)]
      (if (< offset-x (/ target-width 2))
        "before"
        "after"))))

(defn- page-image-draggable
  [props]
  (r/with-let [drop-area (atom nil)
               drag-active? (r/atom nil)

               prevent-defaults #(do (.preventDefault %)
                                     (.stopPropagation %))

               handle-drag-start #(let [target (.-target %)
                                        data-transfer (.-dataTransfer %)
                                        stage (.getAttribute target "data-stage")
                                        side (.getAttribute target "data-side")]
                                    (.setData data-transfer "stage" stage)
                                    (.setData data-transfer "side" side))
               handle-document-drag-enter #(prevent-defaults %)
               handle-drag-over #(do (prevent-defaults %)
                                     (reset! drag-active? (drag-event->page-side %)))
               handle-area-drag-leave #(do (prevent-defaults %)
                                           (reset! drag-active? nil))
               handle-area-drop #(do (prevent-defaults %)
                                     (let [target (.-target %)
                                           data-transfer (.-dataTransfer %)
                                           source-page {:stage (-> data-transfer (.getData "stage") (parse-int))
                                                        :side  (.getData data-transfer "side")}
                                           target-page {:stage (-> target (.getAttribute "data-stage") (parse-int))
                                                        :side  (.getAttribute target "data-side")}]
                                       (re-frame/dispatch [::state-pages/move-page source-page target-page @drag-active?])
                                       (reset! drag-active? nil)))

               init-state (fn [ref]
                            (reset! drop-area ref)
                            (.addEventListener @drop-area "dragstart" handle-drag-start)
                            (.addEventListener @drop-area "dragenter" handle-document-drag-enter)
                            (.addEventListener @drop-area "dragleave" handle-area-drag-leave)
                            (.addEventListener @drop-area "dragover" handle-drag-over)
                            (.addEventListener @drop-area "drop" handle-area-drop))]
    [page-image-view (merge props
                            {:class-name {"drag-active"   (some? @drag-active?)
                                          "drag-on-left"  (= @drag-active? "before")
                                          "drag-on-right" (= @drag-active? "after")}
                             :draggable? true
                             :ref        init-state})]
    (finally
      (.removeEventListener @drop-area "dragstart" handle-drag-start)
      (.removeEventListener @drop-area "dragenter" handle-document-drag-enter)
      (.removeEventListener @drop-area "dragleave" handle-area-drag-leave)
      (.removeEventListener @drop-area "dragover" handle-drag-over)
      (.removeEventListener @drop-area "drop" handle-area-drop))))

(defn page-image
  [{:keys [side stage] :as props}]
  (let [draggable? @(re-frame/subscribe [::state-flipbook/page-draggable? (keyword side) stage])]
    (if draggable?
      [page-image-draggable props]
      [page-image-view props])))
