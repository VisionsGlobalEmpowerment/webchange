(ns webchange.utils.alphabets)

(def en ["a" "b" "c" "d" "e" "f" "g" "h" "i" "j" "k" "l" "m" "n" "o" "p" "q" "r" "s" "t" "u" "v" "w" "x" "y" "z"])

(defn lang->alphabet
  [lang]
  (case lang
    "english" en
    "spanish" en))

(defn options-for
  [lang]
  (->> lang
       lang->alphabet
       (map (fn [letter] {:text letter :value letter}))))
