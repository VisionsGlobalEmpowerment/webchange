(ns webchange.utils.alphabets)

(def english ["a" "b" "c" "d" "e" "f" "g" "h" "i" "j" "k" "l" "m" "n" "o" "p" "q" "r" "s" "t" "u" "v" "w" "x" "y" "z"])
(def spanish (concat english
                     ["á" "ch" "é" "í" "ll" "ó" "ú" "ü" "ñ"]))
(def tamil ["ௐ" "அ" "ஆ" "இ" "ஈ" "உ" "ஊ" "எ" "ஏ" "ஐ" "ஒ" "ஓ" "ஔ" "ஃ" "க" "ங" "ச" "ஞ" "ட" "ண" "த" "ந" "ப" "ம" "ய" "ர" "ல" "வ" "ழ" "ள" "ற" "ன" "ஜ" "ஷ" "ஸ" "ஹ"])
(defn lang->alphabet
  [lang]
  (case lang
    "english" english
    "spanish" spanish
    "tamil" tamil))

(defn options-for
  [lang]
  (->> lang
       lang->alphabet
       (map (fn [letter] {:text letter :value letter}))))
