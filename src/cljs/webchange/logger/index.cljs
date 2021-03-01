(ns webchange.logger.index
  (:require
    [webchange.logger.levels :refer [allowed?]]
    [webchange.logger.printer :refer [print!]]))

(defn- do-print
  [method args]
  (when (allowed? method)
    (print! method args)))

(defn trace [& args] (do-print :trace args))
(defn log [& args] (do-print :log args))
(defn warn [& args] (do-print :warn args))
(defn error [& args] (do-print :error args))

(defn group [& args] (do-print :group args))
(defn group-folded [& args] (do-print :group-folded args))
(defn group-end [& args] (do-print :group-end args))

(defn- do-print-folded
  [method caption args]
  (let [group-name (if (sequential? caption) caption [caption])]
    (apply group-folded group-name)
    (apply method args)
    (apply group-end group-name)))

(defn trace-folded [caption & args] (do-print-folded trace caption args))
