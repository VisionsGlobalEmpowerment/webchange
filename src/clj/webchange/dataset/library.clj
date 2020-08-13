(ns webchange.dataset.library
  (:require
    [webchange.dataset.core :as core]
    [clojure.tools.logging :as log]))

(def english-alphabet {:fields
                       [{:name "concept-name", :type "string"}
                        {:name "concept-rest", :type "string"}
                        {:name "image-src", :type "image"}
                        {:name "letter", :type "string"}
                        {:name "letter-big", :type "string"}
                        {:name "letter-path", :type "string"}
                        {:name "letter-src", :type "image"}
                        ],
                       :items
                       [{:name "a",
                         :data {:concept-name "ardilla",
                                :concept-rest "rdilla",
                                :image-src    "/raw/img/elements/squirrel.png",
                                :letter       "a",
                                :letter-big   "A",
                                :letter-path  "M144.76,92.43a37.5,37.5,0,1,0,0,39.28m0-57.21v75",
                                :letter-src   "/raw/img/letters/a.png",}}
                        {:name "b",
                         :data {:concept-name "bebé",
                                :concept-rest "ebé",
                                :image-src    "/raw/img/elements/baby.png",
                                :letter       "b",
                                :letter-big   "B",
                                :letter-path  "M77.77 0.71v149M77.77,92.64a37.5,37.5,0,1,1,0,39.28",
                                :letter-src   "/raw/img/letters/b.png",}}
                        {:name "c",
                         :data {:concept-name "casa",
                                :concept-rest "asa",
                                :image-src    "/raw/img/elements/home.png",
                                :letter       "c",
                                :letter-big   "C",
                                :letter-path  "M147.23,92.35a37.51,37.51,0,1,0,0,39.27",
                                :letter-src   "/raw/img/letters/c.png",}}
                        {:name "c (soft)",
                         :data {:concept-name "cepillo",
                                :concept-rest "epillo",
                                :image-src    "/raw/img/elements/comb.png",
                                :letter       "c",
                                :letter-big   "C",
                                :letter-path  "M147.23,92.35a37.51,37.51,0,1,0,0,39.27",
                                :letter-src   "/raw/img/letters/c.png",}}
                        {:name "ch",
                         :data {:concept-name         "chocolate",
                                :concept-rest         "ocolate",
                                :image-src            "/raw/img/elements/chocolate.png",
                                :letter               "ch",
                                :letter-big           "Ch",
                                :letter-path          "M95.73,92.59a37.5,37.5,0,1,0,0,39.28M129.27 0.66v149M129.27,92.59a37.51,37.51,0,0,1,69.46,19.64v37.5",
                                :letter-src           "/raw/img/letters/ch.png",
                                :letter-tutorial-path "M.5,0V44.5M.5,8S14.6-1,19.9,8s3.4,36.5,3.4,36.5"}}
                        {:name "d",
                         :data {:concept-name "diamante",
                                :concept-rest "iamante",
                                :image-src    "/raw/img/elements/diamond.png",
                                :letter       "d",
                                :letter-big   "D",
                                :letter-path  "M147.23,91.88a37.5,37.5,0,1,0,0,39.28M147.23,0v150",
                                :letter-src   "/raw/img/letters/d.png",}}
                        {:name "e",
                         :data {:concept-name "elefante",
                                :concept-rest "lefante",
                                :image-src    "/raw/img/elements/elephant.png",
                                :letter       "e",
                                :letter-big   "E",
                                :letter-path  "M77,112h73a37.5,37.5,0,1,0-11.21,26.74",
                                :letter-src   "/raw/img/letters/e.png",}}
                        {:name "f",
                         :data {:concept-name "flor",
                                :concept-rest "lor",
                                :image-src    "/raw/img/elements/flower.png",
                                :letter       "f",
                                :letter-big   "F",
                                :letter-path  "M156.12,13.21A36.35,36.35,0,0,0,128.36.29h0A36.49,36.49,0,0,0,92,36.68V149M68.88,74.78H116",
                                :letter-src   "/raw/img/letters/f.png",}}
                        {:name "g",
                         :data {:concept-name "gato",
                                :concept-rest "ato",
                                :image-src    "/raw/img/elements/cat.png",
                                :letter       "g",
                                :letter-big   "G",
                                :letter-path  "M147.23,92.62a37.5,37.5,0,1,0,0,39.28M147.23,74.86V181a34.73,34.73,0,0,1-67.65,11.09",
                                :letter-src   "/raw/img/letters/g.png",}}
                        {:name "g (soft)",
                         :data {:concept-name "gemelo",
                                :concept-rest "emelo",
                                :image-src    "/raw/img/elements/twins.png",
                                :letter       "g",
                                :letter-big   "G",
                                :letter-path  "M147.23,92.62a37.5,37.5,0,1,0,0,39.28M147.23,74.86V181a34.73,34.73,0,0,1-67.65,11.09",
                                :letter-src   "/raw/img/letters/g.png",}}
                        {:name "h",
                         :data {:concept-name "hoja",
                                :concept-rest "oja",
                                :image-src    "/raw/img/elements/leaf.png",
                                :letter       "h",
                                :letter-big   "H",
                                :letter-path  "M77.77 0.71v149M77.77,92.64a37.51,37.51,0,0,1,69.46,19.64v37.5",
                                :letter-src   "/raw/img/letters/h.png",}}
                        {:name "i",
                         :data {:concept-name "iman",
                                :concept-rest "man",
                                :image-src    "/raw/img/elements/magnet.png",
                                :letter       "i",
                                :letter-big   "I",
                                :letter-path  "M101.7,74.26v75M107.7,34.78a6,6,0,1,1-6-6A6,6,0,0,1,107.7,34.78Z",
                                :letter-src   "/raw/img/letters/i.png",}}
                        {:name "j",
                         :data {:concept-name "jardín",
                                :concept-rest "ardín",
                                :image-src    "/raw/img/elements/garden.png",
                                :letter       "j",
                                :letter-big   "J",
                                :letter-path  "M106.5,74V180.1a34.73,34.73,0,0,1-67.64,11.09M112.5,34.21a6,6,0,1,1-6-6A6,6,0,0,1,112.5,34.21Z",
                                :letter-src   "/raw/img/letters/j.png",}}
                        {:name "k",
                         :data {:concept-name "kimono",
                                :concept-rest "imono",
                                :image-src    "/raw/img/elements/kimono.png",
                                :letter       "k",
                                :letter-big   "K",
                                :letter-path  "M112.5-0.22v150M162.63,74.78l-48,29.06l61.55,45.94",
                                :letter-src   "/raw/img/letters/k.png",}}
                        {:name "l",
                         :data {:concept-name "leon",
                                :concept-rest "eon",
                                :image-src    "/raw/img/elements/lion.png",
                                :letter       "l",
                                :letter-big   "L",
                                :letter-path  "M112.5 0.19v150",
                                :letter-src   "/raw/img/letters/l.png",}}
                        {:name "ll",
                         :data {:concept-name         "llave",
                                :concept-rest         "ave",
                                :image-src            "/raw/img/elements/key.png",
                                :letter               "ll",
                                :letter-big           "Ll",
                                :letter-path          "M84.5 0.19v150M140.5 0.19v150",
                                :letter-src           "/raw/img/letters/ll.png",
                                :letter-tutorial-path "M.5,5.52S8.31-1.42,12.19,1,14.81,8.64,15,21.89"}}
                        {:name "m",
                         :data {:concept-name "mano",
                                :concept-rest "ano",
                                :image-src    "/raw/img/elements/hand.png",
                                :letter       "m",
                                :letter-big   "M",
                                :letter-path  "M71.86,75v75M71.85,91.79C79.19,80,93.46,76.45,101.17,78.27c11.89,2.8,19.93,12.51,19.93,23.27V150M119.85,91.79c7.34,-11.77,21.61,-15.34,29.32,-13.52c11.89,2.8,19.93,12.51,19.93,23.27V150",
                                :letter-src   "/raw/img/letters/m.png",}}
                        {:name "n",
                         :data {:concept-name "niño",
                                :concept-rest "iño",
                                :image-src    "/raw/img/elements/nino.png",
                                :letter       "n",
                                :letter-big   "N",
                                :letter-path  "M82.05,74.42v75M82.05,91.55A32.35,32.35,0,0,1,143,106.77v42.65",
                                :letter-src   "/raw/img/letters/n.png",}}
                        {:name "o",
                         :data {:concept-name "oso",
                                :concept-rest "so",
                                :image-src    "/raw/img/elements/bear.png",
                                :letter       "o",
                                :letter-big   "O",
                                :letter-path  "M110.65,74a37.5,37.5,0,1,0,37.5,37.5A37.5,37.5,0,0,0,110.65,74Z",
                                :letter-src   "/raw/img/letters/o.png",}}
                        {:name "p",
                         :data {:concept-name "pelota",
                                :concept-rest "elota",
                                :image-src    "/raw/img/elements/ball.png",
                                :letter       "p",
                                :letter-big   "P",
                                :letter-path  "M77.77,74.37v149M77.77,93.1a37.5,37.5,0,1,1,0,39.28",
                                :letter-src   "/raw/img/letters/p.png",}}
                        {:name "qu",
                         :data {:concept-name "queso",
                                :concept-rest "eso",
                                :image-src    "/raw/img/elements/cheese.png",
                                :letter       "qu",
                                :letter-big   "Qu",
                                :letter-path  "M148.58,92.61a37.5,37.5,0,1,0,0,39.27M148.58,74.37v149",
                                :letter-src   "/raw/img/letters/q.png",}}
                        {:name "r",
                         :data {:concept-name "rana",
                                :concept-rest "ana",
                                :image-src    "/raw/img/elements/frog.png",
                                :letter       "r",
                                :letter-big   "R",
                                :letter-path  "M85.86,75.66v75M85.85,92.79a32.37,32.37,0,0,1,53.3-5.63",
                                :letter-src   "/raw/img/letters/r.png",}}
                        {:name "s",
                         :data {:concept-name "serpiente",
                                :concept-rest "erpiente",
                                :image-src    "/raw/img/elements/snake.png",
                                :letter       "s",
                                :letter-big   "S",
                                :letter-path  "M131.91,90.75c1.33-9.39-10.49-14.17-18.28-14.39A21.71,21.71,0,0,0,97.69,82.7a16.08,16.08,0,0,0-3.89,16c1.65,5.12,6.13,8.58,10.93,10.57c6.67,2.77,13.9,4,20.26,7.62c7,3.94,10.77,12.34,8,20.14c-5.06,14.06-27.62,17.67-37.73,5.77a18.91,18.91,0,0,1-4.11-10.2",
                                :letter-src   "/raw/img/letters/s.png",}}
                        {:name "t",
                         :data {:concept-name "tomate",
                                :concept-rest "omate",
                                :image-src    "/raw/img/elements/tomato.png",
                                :letter       "t",
                                :letter-big   "T",
                                :letter-path  "M112.5,35.29v115M89.4,75.29h46.2",
                                :letter-src   "/raw/img/letters/t.png",}}
                        {:name "u",
                         :data {:concept-name "uvas",
                                :concept-rest "vas",
                                :image-src    "/raw/img/elements/grapes.png",
                                :letter       "u",
                                :letter-big   "U",
                                :letter-path  "M82.05,75v42.65A32.35,32.35,0,0,0,143,132.87M143,74.38V149.6",
                                :letter-src   "/raw/img/letters/u.png",}}
                        {:name "v",
                         :data {:concept-name "violin",
                                :concept-rest "iolin",
                                :image-src    "/raw/img/elements/violin.png",
                                :letter       "v",
                                :letter-big   "V",
                                :letter-path  "M79.56,75l32.88,75M112.56,150l32.88-75",
                                :letter-src   "/raw/img/letters/v.png",}}
                        {:name "w",
                         :data {:concept-name "web",
                                :concept-rest "eb",
                                :image-src    "/raw/img/elements/website.png",
                                :letter       "w",
                                :letter-big   "W",
                                :letter-path  "M73.71,75.66l18.58,75M93.71,150.66l18.58-75M112.71,75.66l18.58,75M132.71,150.66l18.58-75",
                                :letter-src   "/raw/img/letters/w.png",}}
                        {:name "x",
                         :data {:concept-name "xilofono",
                                :concept-rest "ilofono",
                                :image-src    "/raw/img/elements/xylophone.png",
                                :letter       "x",
                                :letter-big   "X",
                                :letter-path  "M81.53,75.66l61.94,75M143.47,75.66l-61.94,75",
                                :letter-src   "/raw/img/letters/x.png",}}
                        {:name "y",
                         :data {:concept-name "yoyo",
                                :concept-rest "oyo",
                                :image-src    "/raw/img/elements/yoyo.png",
                                :letter       "y",
                                :letter-big   "Y",
                                :letter-path  "M78.19,74.78l34.84,75M146.81,74.78,96.61,188.91",
                                :letter-src   "/raw/img/letters/y.png",}}
                        {:name "z",
                         :data {:concept-name "zapato",
                                :concept-rest "apato",
                                :image-src    "/raw/img/elements/boot.png",
                                :letter       "z",
                                :letter-big   "Z",
                                :letter-path  "M79.86,75.66h61.29l-57.29,75h61.28",
                                :letter-src   "/raw/img/letters/z.png",}}
                        {:name "ñ",
                         :data {:concept-name "ñandu",
                                :concept-rest "andu",
                                :image-src    "/raw/img/elements/ostrich.png",
                                :letter       "ñ",
                                :letter-big   "ñ",
                                :letter-path  "M82,74.83v75M82,92a32.36,32.36,0,0,1,60.9,15.23v42.65M86,48.06s4.12-11.83,13.63-7.88c3.71,1.54,7.49,2.94,11.29,4.27c10.27,3.6,22.52,2.17,28-8.67",
                                :letter-src   "/raw/img/letters/ñ.png",}}]
                       })
(def spanish-alphabet {:fields
                       [{:name "concept-name", :type "string"}
                        {:name "concept-rest", :type "string"}
                        {:name "image-src", :type "image"}
                        {:name "letter", :type "string"}
                        {:name "letter-big", :type "string"}
                        {:name "letter-path", :type "string"}
                        {:name "letter-src", :type "image"}
                        ],
                       :items
                       [{:name "a",
                         :data {:concept-name "ardilla",
                                :concept-rest "rdilla",
                                :image-src    "/raw/img/elements/squirrel.png",
                                :letter       "a",
                                :letter-big   "A",
                                :letter-path  "M144.76,92.43a37.5,37.5,0,1,0,0,39.28m0-57.21v75",
                                :letter-src   "/raw/img/letters/a.png",}}
                        {:name "b",
                         :data {:concept-name "bebé",
                                :concept-rest "ebé",
                                :image-src    "/raw/img/elements/baby.png",
                                :letter       "b",
                                :letter-big   "B",
                                :letter-path  "M77.77 0.71v149M77.77,92.64a37.5,37.5,0,1,1,0,39.28",
                                :letter-src   "/raw/img/letters/b.png",}}
                        {:name "c",
                         :data {:concept-name "casa",
                                :concept-rest "asa",
                                :image-src    "/raw/img/elements/home.png",
                                :letter       "c",
                                :letter-big   "C",
                                :letter-path  "M147.23,92.35a37.51,37.51,0,1,0,0,39.27",
                                :letter-src   "/raw/img/letters/c.png",}}
                        {:name "c (soft)",
                         :data {:concept-name "cepillo",
                                :concept-rest "epillo",
                                :image-src    "/raw/img/elements/comb.png",
                                :letter       "c",
                                :letter-big   "C",
                                :letter-path  "M147.23,92.35a37.51,37.51,0,1,0,0,39.27",
                                :letter-src   "/raw/img/letters/c.png",}}
                        {:name "ch",
                         :data {:concept-name         "chocolate",
                                :concept-rest         "ocolate",
                                :image-src            "/raw/img/elements/chocolate.png",
                                :letter               "ch",
                                :letter-big           "Ch",
                                :letter-path          "M95.73,92.59a37.5,37.5,0,1,0,0,39.28M129.27 0.66v149M129.27,92.59a37.51,37.51,0,0,1,69.46,19.64v37.5",
                                :letter-src           "/raw/img/letters/ch.png",
                                :letter-tutorial-path "M.5,0V44.5M.5,8S14.6-1,19.9,8s3.4,36.5,3.4,36.5"}}
                        {:name "d",
                         :data {:concept-name "diamante",
                                :concept-rest "iamante",
                                :image-src    "/raw/img/elements/diamond.png",
                                :letter       "d",
                                :letter-big   "D",
                                :letter-path  "M147.23,91.88a37.5,37.5,0,1,0,0,39.28M147.23,0v150",
                                :letter-src   "/raw/img/letters/d.png",}}
                        {:name "e",
                         :data {:concept-name "elefante",
                                :concept-rest "lefante",
                                :image-src    "/raw/img/elements/elephant.png",
                                :letter       "e",
                                :letter-big   "E",
                                :letter-path  "M77,112h73a37.5,37.5,0,1,0-11.21,26.74",
                                :letter-src   "/raw/img/letters/e.png",}}
                        {:name "f",
                         :data {:concept-name "flor",
                                :concept-rest "lor",
                                :image-src    "/raw/img/elements/flower.png",
                                :letter       "f",
                                :letter-big   "F",
                                :letter-path  "M156.12,13.21A36.35,36.35,0,0,0,128.36.29h0A36.49,36.49,0,0,0,92,36.68V149M68.88,74.78H116",
                                :letter-src   "/raw/img/letters/f.png",}}
                        {:name "g",
                         :data {:concept-name "gato",
                                :concept-rest "ato",
                                :image-src    "/raw/img/elements/cat.png",
                                :letter       "g",
                                :letter-big   "G",
                                :letter-path  "M147.23,92.62a37.5,37.5,0,1,0,0,39.28M147.23,74.86V181a34.73,34.73,0,0,1-67.65,11.09",
                                :letter-src   "/raw/img/letters/g.png",}}
                        {:name "g (soft)",
                         :data {:concept-name "gemelo",
                                :concept-rest "emelo",
                                :image-src    "/raw/img/elements/twins.png",
                                :letter       "g",
                                :letter-big   "G",
                                :letter-path  "M147.23,92.62a37.5,37.5,0,1,0,0,39.28M147.23,74.86V181a34.73,34.73,0,0,1-67.65,11.09",
                                :letter-src   "/raw/img/letters/g.png",}}
                        {:name "h",
                         :data {:concept-name "hoja",
                                :concept-rest "oja",
                                :image-src    "/raw/img/elements/leaf.png",
                                :letter       "h",
                                :letter-big   "H",
                                :letter-path  "M77.77 0.71v149M77.77,92.64a37.51,37.51,0,0,1,69.46,19.64v37.5",
                                :letter-src   "/raw/img/letters/h.png",}}
                        {:name "i",
                         :data {:concept-name "iman",
                                :concept-rest "man",
                                :image-src    "/raw/img/elements/magnet.png",
                                :letter       "i",
                                :letter-big   "I",
                                :letter-path  "M101.7,74.26v75M107.7,34.78a6,6,0,1,1-6-6A6,6,0,0,1,107.7,34.78Z",
                                :letter-src   "/raw/img/letters/i.png",}}
                        {:name "j",
                         :data {:concept-name "jardín",
                                :concept-rest "ardín",
                                :image-src    "/raw/img/elements/garden.png",
                                :letter       "j",
                                :letter-big   "J",
                                :letter-path  "M106.5,74V180.1a34.73,34.73,0,0,1-67.64,11.09M112.5,34.21a6,6,0,1,1-6-6A6,6,0,0,1,112.5,34.21Z",
                                :letter-src   "/raw/img/letters/j.png",}}
                        {:name "k",
                         :data {:concept-name "kimono",
                                :concept-rest "imono",
                                :image-src    "/raw/img/elements/kimono.png",
                                :letter       "k",
                                :letter-big   "K",
                                :letter-path  "M112.5-0.22v150M162.63,74.78l-48,29.06l61.55,45.94",
                                :letter-src   "/raw/img/letters/k.png",}}
                        {:name "l",
                         :data {:concept-name "leon",
                                :concept-rest "eon",
                                :image-src    "/raw/img/elements/lion.png",
                                :letter       "l",
                                :letter-big   "L",
                                :letter-path  "M112.5 0.19v150",
                                :letter-src   "/raw/img/letters/l.png",}}
                        {:name "ll",
                         :data {:concept-name         "llave",
                                :concept-rest         "ave",
                                :image-src            "/raw/img/elements/key.png",
                                :letter               "ll",
                                :letter-big           "Ll",
                                :letter-path          "M84.5 0.19v150M140.5 0.19v150",
                                :letter-src           "/raw/img/letters/ll.png",
                                :letter-tutorial-path "M.5,5.52S8.31-1.42,12.19,1,14.81,8.64,15,21.89"}}
                        {:name "m",
                         :data {:concept-name "mano",
                                :concept-rest "ano",
                                :image-src    "/raw/img/elements/hand.png",
                                :letter       "m",
                                :letter-big   "M",
                                :letter-path  "M71.86,75v75M71.85,91.79C79.19,80,93.46,76.45,101.17,78.27c11.89,2.8,19.93,12.51,19.93,23.27V150M119.85,91.79c7.34,-11.77,21.61,-15.34,29.32,-13.52c11.89,2.8,19.93,12.51,19.93,23.27V150",
                                :letter-src   "/raw/img/letters/m.png",}}
                        {:name "n",
                         :data {:concept-name "niño",
                                :concept-rest "iño",
                                :image-src    "/raw/img/elements/nino.png",
                                :letter       "n",
                                :letter-big   "N",
                                :letter-path  "M82.05,74.42v75M82.05,91.55A32.35,32.35,0,0,1,143,106.77v42.65",
                                :letter-src   "/raw/img/letters/n.png",}}
                        {:name "o",
                         :data {:concept-name "oso",
                                :concept-rest "so",
                                :image-src    "/raw/img/elements/bear.png",
                                :letter       "o",
                                :letter-big   "O",
                                :letter-path  "M110.65,74a37.5,37.5,0,1,0,37.5,37.5A37.5,37.5,0,0,0,110.65,74Z",
                                :letter-src   "/raw/img/letters/o.png",}}
                        {:name "p",
                         :data {:concept-name "pelota",
                                :concept-rest "elota",
                                :image-src    "/raw/img/elements/ball.png",
                                :letter       "p",
                                :letter-big   "P",
                                :letter-path  "M77.77,74.37v149M77.77,93.1a37.5,37.5,0,1,1,0,39.28",
                                :letter-src   "/raw/img/letters/p.png",}}
                        {:name "qu",
                         :data {:concept-name "queso",
                                :concept-rest "eso",
                                :image-src    "/raw/img/elements/cheese.png",
                                :letter       "qu",
                                :letter-big   "Qu",
                                :letter-path  "M148.58,92.61a37.5,37.5,0,1,0,0,39.27M148.58,74.37v149",
                                :letter-src   "/raw/img/letters/q.png",}}
                        {:name "r",
                         :data {:concept-name "rana",
                                :concept-rest "ana",
                                :image-src    "/raw/img/elements/frog.png",
                                :letter       "r",
                                :letter-big   "R",
                                :letter-path  "M85.86,75.66v75M85.85,92.79a32.37,32.37,0,0,1,53.3-5.63",
                                :letter-src   "/raw/img/letters/r.png",}}
                        {:name "s",
                         :data {:concept-name "serpiente",
                                :concept-rest "erpiente",
                                :image-src    "/raw/img/elements/snake.png",
                                :letter       "s",
                                :letter-big   "S",
                                :letter-path  "M131.91,90.75c1.33-9.39-10.49-14.17-18.28-14.39A21.71,21.71,0,0,0,97.69,82.7a16.08,16.08,0,0,0-3.89,16c1.65,5.12,6.13,8.58,10.93,10.57c6.67,2.77,13.9,4,20.26,7.62c7,3.94,10.77,12.34,8,20.14c-5.06,14.06-27.62,17.67-37.73,5.77a18.91,18.91,0,0,1-4.11-10.2",
                                :letter-src   "/raw/img/letters/s.png",}}
                        {:name "t",
                         :data {:concept-name "tomate",
                                :concept-rest "omate",
                                :image-src    "/raw/img/elements/tomato.png",
                                :letter       "t",
                                :letter-big   "T",
                                :letter-path  "M112.5,35.29v115M89.4,75.29h46.2",
                                :letter-src   "/raw/img/letters/t.png",}}
                        {:name "u",
                         :data {:concept-name "uvas",
                                :concept-rest "vas",
                                :image-src    "/raw/img/elements/grapes.png",
                                :letter       "u",
                                :letter-big   "U",
                                :letter-path  "M82.05,75v42.65A32.35,32.35,0,0,0,143,132.87M143,74.38V149.6",
                                :letter-src   "/raw/img/letters/u.png",}}
                        {:name "v",
                         :data {:concept-name "violin",
                                :concept-rest "iolin",
                                :image-src    "/raw/img/elements/violin.png",
                                :letter       "v",
                                :letter-big   "V",
                                :letter-path  "M79.56,75l32.88,75M112.56,150l32.88-75",
                                :letter-src   "/raw/img/letters/v.png",}}
                        {:name "w",
                         :data {:concept-name "web",
                                :concept-rest "eb",
                                :image-src    "/raw/img/elements/website.png",
                                :letter       "w",
                                :letter-big   "W",
                                :letter-path  "M73.71,75.66l18.58,75M93.71,150.66l18.58-75M112.71,75.66l18.58,75M132.71,150.66l18.58-75",
                                :letter-src   "/raw/img/letters/w.png",}}
                        {:name "x",
                         :data {:concept-name "xilofono",
                                :concept-rest "ilofono",
                                :image-src    "/raw/img/elements/xylophone.png",
                                :letter       "x",
                                :letter-big   "X",
                                :letter-path  "M81.53,75.66l61.94,75M143.47,75.66l-61.94,75",
                                :letter-src   "/raw/img/letters/x.png",}}
                        {:name "y",
                         :data {:concept-name "yoyo",
                                :concept-rest "oyo",
                                :image-src    "/raw/img/elements/yoyo.png",
                                :letter       "y",
                                :letter-big   "Y",
                                :letter-path  "M78.19,74.78l34.84,75M146.81,74.78,96.61,188.91",
                                :letter-src   "/raw/img/letters/y.png",}}
                        {:name "z",
                         :data {:concept-name "zapato",
                                :concept-rest "apato",
                                :image-src    "/raw/img/elements/boot.png",
                                :letter       "z",
                                :letter-big   "Z",
                                :letter-path  "M79.86,75.66h61.29l-57.29,75h61.28",
                                :letter-src   "/raw/img/letters/z.png",}}
                        {:name "ñ",
                         :data {:concept-name "ñandu",
                                :concept-rest "andu",
                                :image-src    "/raw/img/elements/ostrich.png",
                                :letter       "ñ",
                                :letter-big   "ñ",
                                :letter-path  "M82,74.83v75M82,92a32.36,32.36,0,0,1,60.9,15.23v42.65M86,48.06s4.12-11.83,13.63-7.88c3.71,1.54,7.49,2.94,11.29,4.27c10.27,3.6,22.52,2.17,28-8.67",
                                :letter-src   "/raw/img/letters/ñ.png",}}]
                       })

(def datasets [{:id 1 :name "English alphabet" :dataset english-alphabet}
               {:id 2 :name "Spanish alphabet" :dataset spanish-alphabet}])

(defn get-datasets
  []
  (->> datasets
       (map #(select-keys % [:id :name]))))

(defn get-dataset
  [id]
  (->> datasets
       (filter #(= id (:id %)))
       first))

(defn create-dataset!
  [course-slug library-dataset-id]
  (let [{:keys [name dataset]} (-> library-dataset-id get-dataset)
        {dataset-id :id} (core/create-dataset! {:course-slug course-slug
                                                :name        name
                                                :scheme      {:fields (:fields dataset)}})]
    (doseq [item (:items dataset)]
      (core/create-dataset-item! (-> item (assoc :dataset-id dataset-id))))))
