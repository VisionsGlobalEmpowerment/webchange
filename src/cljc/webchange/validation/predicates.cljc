(ns webchange.validation.predicates)

(def date-string? #(->> % (re-matches #"([12]\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\d|3[01]))") (boolean)))
(def not-empty? (complement empty?))
(def not-empty-string? #(and (string? %)
                             (not-empty? %)))
(def not-defined? #(or (nil? %)
                       (and (string? %)
                            (empty? %))))
