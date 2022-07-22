(ns webchange.lesson-builder.tools.template-options.select-video.state
  (:require
    [re-frame.core :as re-frame]
    [re-frame.std-interceptors :as i]
    [webchange.lesson-builder.tools.template-options.state :refer [path-to-db] :as template-options-state]))

(def english
  {"a" "/raw/video/letters/en/Letra_A_EN_sq.mp4"
   "b" "/raw/video/letters/en/Letra_B_EN_sq.mp4"
   "c" "/raw/video/letters/en/Letra_C_EN_sq.mp4"
   "d" "/raw/video/letters/en/Letra_D_EN_sq.mp4"
   "e" "/raw/video/letters/en/Letra_E_EN_sq.mp4"
   "f" "/raw/video/letters/en/Letra_F_EN_sq.mp4"
   "g" "/raw/video/letters/en/Letra_G_EN_sq.mp4"
   "h" "/raw/video/letters/en/Letra_H_EN_sq.mp4"
   "i" "/raw/video/letters/en/Letra_I_EN_sq.mp4"
   "j" "/raw/video/letters/en/Letra_J_EN_sq.mp4"
   "k" "/raw/video/letters/en/Letra_K_EN_sq.mp4"
   "l" "/raw/video/letters/en/Letra_L_EN_sq.mp4"
   "m" "/raw/video/letters/en/Letra_M_EN_sq.mp4"
   "n" "/raw/video/letters/en/Letra_N_EN_sq.mp4"
   "o" "/raw/video/letters/en/Letra_O_EN_sq.mp4"
   "p" "/raw/video/letters/en/Letra_P_EN_sq.mp4"
   "q" "/raw/video/letters/en/Letra_Q_EN_sq.mp4"
   "r" "/raw/video/letters/en/Letra_R_EN_sq.mp4"
   "s" "/raw/video/letters/en/Letra_S_EN_sq.mp4"
   "t" "/raw/video/letters/en/Letra_T_EN_sq.mp4"
   "u" "/raw/video/letters/en/Letra_U_EN_sq.mp4"
   "v" "/raw/video/letters/en/Letra_V_EN_sq.mp4"
   "w" "/raw/video/letters/en/Letra_W_EN_sq.mp4"
   "x" "/raw/video/letters/en/Letra_X_EN_sq.mp4"
   "y" "/raw/video/letters/en/Letra_Y_EN_sq.mp4"
   "z" "/raw/video/letters/en/Letra_Z_EN_sq.mp4"})

(def spanish
  {"a" "/raw/video/letters/es/Letra_A_ES_sq.mp4"
   "b" "/raw/video/letters/es/Letra_B_ES_sq.mp4"
   "c" "/raw/video/letters/es/Letra_C_ES_sq.mp4"
   "d" "/raw/video/letters/es/Letra_D_ES_sq.mp4"
   "e" "/raw/video/letters/es/Letra_E_ES_sq.mp4"
   "f" "/raw/video/letters/es/Letra_F_ES_sq.mp4"
   "g" "/raw/video/letters/es/Letra_G_ES_sq.mp4"
   "h" "/raw/video/letters/es/Letra_H_ES_sq.mp4"
   "i" "/raw/video/letters/es/Letra_I_ES_sq.mp4"
   "j" "/raw/video/letters/es/Letra_J_ES_sq.mp4"
   "k" "/raw/video/letters/es/Letra_K_ES_sq.mp4"
   "l" "/raw/video/letters/es/Letra_L_ES_sq.mp4"
   "m" "/raw/video/letters/es/Letra_M_ES_sq.mp4"
   "n" "/raw/video/letters/es/Letra_N_ES_sq.mp4"
   "o" "/raw/video/letters/es/Letra_O_ES_sq.mp4"
   "p" "/raw/video/letters/es/Letra_P_ES_sq.mp4"
   "q" "/raw/video/letters/es/Letra_Q_ES_sq.mp4"
   "r" "/raw/video/letters/es/Letra_R_ES_sq.mp4"
   "s" "/raw/video/letters/es/Letra_S_ES_sq.mp4"
   "t" "/raw/video/letters/es/Letra_T_ES_sq.mp4"
   "u" "/raw/video/letters/es/Letra_U_ES_sq.mp4"
   "v" "/raw/video/letters/es/Letra_V_ES_sq.mp4"
   "w" "/raw/video/letters/es/Letra_W_ES_sq.mp4"
   "x" "/raw/video/letters/es/Letra_X_ES_sq.mp4"
   "y" "/raw/video/letters/es/Letra_Y_ES_sq.mp4"
   "z" "/raw/video/letters/es/Letra_Z_ES_sq.mp4"})

(re-frame/reg-sub
  ::video-options
  :<- [path-to-db]
  (fn [db]
    (let [files (case (:lang db)
                  "english" english
                  "spanish" spanish)]
      (map (fn [[k v]]
             {:text k
              :value v}) files))))

(re-frame/reg-sub
  ::selected-video
  :<- [path-to-db]
  (fn [db]
    (get-in db [:form :video-src])))

(re-frame/reg-event-fx
  ::init
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_]]
    {}))

(re-frame/reg-event-fx
  ::select-video
  [(i/path path-to-db)]
  (fn [{:keys [db]} [_ src]]
    {:db (assoc-in db [:form :video-src] src)}))
