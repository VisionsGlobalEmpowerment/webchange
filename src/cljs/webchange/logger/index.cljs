(ns webchange.logger.index
  (:require
    [webchange.logger.levels :refer [allowed? dev-mode?]]
    [webchange.logger.printer :refer [print!]]))

(defn- do-print
  [method args]
  (when (allowed? method)
    (print! method args)))

(def log-enable? dev-mode?)

(defn trace [& args] (do-print :trace args))
(defn log [& args] (do-print :log args))
(defn warn [& args] (do-print :warn args))
(defn error [& args] (do-print :error args))

(defn group [& args] (do-print :group args))
(defn group-folded [& args] (do-print :group-folded args))
(defn group-end [& args] (do-print :group-end args))

(defn trace-list [list] (if (empty? list) (trace "--empty--") (doseq [item list] (trace item))))
(defn trace-list-folded [message list] (group-folded message) (trace-list list) (group-end message))

(defn ->>with-trace [message value] (trace message value) value)
(defn ->>with-trace-folded [message value] (group-folded message) (trace value) (group-end message) value)

(defn with-trace-list [list] (trace-list list) list)
(defn ->>with-trace-list-folded [message list] (group-folded message) (trace-list list) (group-end message) list)

(defn ->with-group-end [value message] (group-end message) value)
(defn ->>with-group-end [message value] (group-end message) value)

(defn- do-print-folded
  ([method caption args]
   (do-print-folded method caption args false))
  ([method caption args partitions?]
   (let [group-name (if (sequential? caption) caption [caption])]
     (apply group-folded group-name)
     (if partitions?
       (doseq [pair (partition 2 args)]
         (apply method pair))
       (apply method args))
     (apply group-end group-name))))

(defn trace-folded [caption & args] (do-print-folded trace caption args))
(defn trace-folded-defs [caption & args] (do-print-folded trace caption args true))
