(ns webchange.utils.languages)

(def languages (-> (map #(assoc % :metadata {:primary? true})
                        [{:name "English" :value "english"}
                         {:name "Spanish" :value "spanish"}])
                   (concat [{:name "Alur" :value "alur"}
                            {:name "Afrikaans" :value "afrikaans"}
                            {:name "Amharic" :value "amharic"}
                            {:name "Anii" :value "anii"}
                            {:name "Anywak" :value "anywak"}
                            {:name "Arabic" :value "arabic"}
                            {:name "Bahasa Indonesia" :value "bahasa-indonesia"}
                            {:name "Bengali" :value "bengali"}
                            {:name "Chinese (simplified)" :value "chinese-simplified"}
                            {:name "Chinese (traditional)" :value "chinese-traditional"}
                            {:name "ChiShona" :value "chi-shona"}
                            {:name "ChiTonga" :value "chi-tonga"}
                            {:name "CiNyanja" :value "ci-nyanja"}
                            {:name "Cinyungwe" :value "cinyungwe"}
                            {:name "Cisena" :value "cisena"}
                            {:name "Dhopadhola" :value "dhopadhola"}
                            {:name "Echiziinza" :value "echiziinza"}
                            {:name "Echuwabo" :value "echuwabo"}
                            {:name "Ekegusii" :value "ekegusii"}
                            {:name "Elomwe" :value "elomwe"}
                            {:name "Emakhuwa" :value "emakhuwa"}
                            {:name "Filipino" :value "filipino"}
                            {:name "French" :value "french"}
                            {:name "Fulfulde Mbororoore" :value "fulfulde-mbororoore"}
                            {:name "Hindi" :value "hindi"}
                            {:name "IciBemba" :value "ici-bemba"}
                            {:name "Ikisimbete" :value "ikisimbete"}
                            {:name "Imeetto" :value "imeetto"}
                            {:name "isiNdebele" :value "isi-ndebele"}
                            {:name "isiXhosa" :value "isi-xhosa"}
                            {:name "isiZulu" :value "isi-zulu"}
                            {:name "Italian" :value "italian"}
                            {:name "Khmer" :value "khmer"}
                            {:name "Kikamba" :value "kikamba"}
                            {:name "Kimiiru" :value "kimiiru"}
                            {:name "Kinyarwanda" :value "kinyarwanda"}
                            {:name "Kiswahili" :value "kiswahili"}
                            {:name "kwaSizwe" :value "kwa-sizwe"}
                            {:name "Lao" :value "lao"}
                            {:name "Lesotho" :value "lesotho"}
                            {:name "Lingala" :value "lingala"}
                            {:name "Lubukusu" :value "lubukusu"}
                            {:name "Luganda" :value "luganda"}
                            {:name "Lugbarati" :value "lugbarati"}
                            {:name "Lusamia" :value "lusamia"}
                            {:name "Lusoga" :value "lusoga"}
                            {:name "Marathi" :value "marathi"}
                            {:name "Myanmar" :value "myanmar"}
                            {:name "Nepali" :value "nepali"}
                            {:name "Ng'aturkana" :value "ng'aturkana"}
                            {:name "Olunyaneka" :value "olunyaneka"}
                            {:name "Oluwanga" :value "oluwanga"}
                            {:name "Persian" :value "persian"}
                            {:name "Portuguese" :value "portuguese"}
                            {:name "Runyankore" :value "runyankore"}
                            {:name "Runyoro" :value "runyoro"}
                            {:name "Runyoro-Rutooro" :value "runyoro-rutooro"}
                            {:name "Rutooro" :value "rutooro"}
                            {:name "Sepedi" :value "sepedi"}
                            {:name "Sesotho" :value "sesotho"}
                            {:name "Setswana" :value "setswana"}
                            {:name "SiLozi" :value "si-lozi"}
                            {:name "Sinhala" :value "sinhala"}
                            {:name "Siswati" :value "siswati"}
                            {:name "Tamil" :value "tamil"}
                            {:name "Telugu" :value "telugu"}
                            {:name "Tijikalanga" :value "tijikalanga"}
                            {:name "Tiv" :value "tiv"}
                            {:name "Tshivenda" :value "tshivenda"}
                            {:name "Tunisian" :value "tunisian"}
                            {:name "Urdu" :value "urdu"}
                            {:name "Vietnamese" :value "vietnamese"}
                            {:name "Xitsonga" :value "xitsonga"}
                            {:name "Yoruba" :value "yoruba"}])))

(def language-options (->> languages
                           (map (fn [{:keys [name value]}]
                                  {:text  name
                                   :value value}))))
