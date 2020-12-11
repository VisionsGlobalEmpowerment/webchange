(ns webchange.editor-v2.course-table.keyboard-control)

(def key-codes {:arrow-left  37
                :arrow-up    38
                :arrow-right 39
                :arrow-down  40
                :enter       13
                :esc         27
                :shift       16
                :tab         9})

(defn- is?
  [code name]
  (-> key-codes
      (get name)
      (= code)))

(defn- call-handler
  [handlers handler-key & args]
  (when (and (some? handler-key)
             (contains? handlers handler-key))
    (apply (get handlers handler-key) args)))

(defn handle-event
  [event handlers]
  (let [key-code (.-which event)
        shifted? (.-shiftKey event)
        handle (partial call-handler handlers)]
    (cond
      (is? key-code :tab) (if shifted?
                            (handle :move-selection :left)
                            (handle :move-selection :right))
      (is? key-code :enter) (handle :enter)
      (is? key-code :arrow-left) (handle :move-selection :left)
      (is? key-code :arrow-up) (handle :move-selection :up)
      (is? key-code :arrow-right) (handle :move-selection :right)
      (is? key-code :arrow-down) (handle :move-selection :down)
      (is? key-code :esc) (handle :reset-selection)
      :else nil)))
