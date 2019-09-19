(ns webchange.interpreter.components.text
  (:require
    [konva :refer [Text]]
    [webchange.interpreter.components.utils :refer [add-events-listeners
                                                    map->konva-params]]))

;; ToDo: implement the rest functionality
;; - apply origin offset
;; - support transitions
;; - support drag-and-drop
;; - support chunked text:
;;        (if (:chunks object)
;;          (let [on-mount #(re-frame/dispatch [::ie/register-text %])]
;;            [chunked-text scene-id name (assoc object :on-mount on-mount)])
;;          [:> Text (dissoc object :x :y)])

(defn create
  [params]
  (let [{:keys [actions attributes]} (map->konva-params params)
        text-node (Text. attributes)]
    (add-events-listeners text-node actions)))

(defn set-attr
  [node params]
  (let [{:keys [attributes]} (map->konva-params params)]
    (.setAttrs node attributes)))

(defn destroy
  [node]
  (.destroy node))
