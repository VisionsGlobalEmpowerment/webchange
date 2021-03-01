(ns webchange.logger.printer)

(def log-methods {:group        js/console.group
                  :group-folded js/console.groupCollapsed
                  :group-end    js/console.groupEnd
                  :trace        js/console.log
                  :log          js/console.info
                  :warn         js/console.warn
                  :error        js/console.error})

(defn print!
  [method args]
  (let [print-func (get log-methods method #())]
    (apply print-func args)))
