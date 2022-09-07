(ns webchange.emails.templates.email-confirmation
  (:require
    [config.core :refer [env]]))

(def hostname (get env :platform-host "webchange.local"))

(defn- verify-code-link
  [user-id code]
  (str  "https://" hostname "/activation-page/?key=" code "&user=" user-id))

(defn email-confirmation-template
  [{:keys [email id code name]}]
  [:html 
   [:head 
    [:title "BLUE BRICK SCHOOL"] 
    [:meta {:http-equiv "Content-Type", :content "text/html; charset=UTF-8"}] 
    [:link {:href "https://fonts.googleapis.com/css2?family=Noto+Sans:wght@100;200;300;400;500;600;700;800;900&display=swap", :rel "stylesheet"}]] 
   [:body {:bgcolor "#e3e0d8"
           :style "#e3e0d8;"
           :leftmargin "0"
           :topmargin "0"
           :marginwidth "0"
           :marginheight "0"} 
    [:center 
     [:table {:width "600"
              :border "0"
              :cellspacing "0"
              :cellpadding "0"
              :align "center"
              :style "margin: 0 auto; display:block;"} 
      [:tbody 
       [:tr 
        [:td
         [:table {:width "600"
                  :border "0"
                  :cellspacing "0"
                  :cellpadding "0"
                  :style "background: #e9f5ff;"} 
          [:tbody 
           [:tr 
            [:td {:width "100%"
                  :valign "center"
                  :style "text-align:center;padding:30px 0 30px;"} 
             [:img {:align "center"
                    :height "45"
                    :alt "SynapseIndia"
                    :src "https://bluebrickschool.org/wp-content/themes/blue-bricks-school/assets/images/logo.png"
                    :style "border: 0px none;"}]]] 
           [:tr 
            [:td {:width "100%"
                  :valign "center"
                  :style "text-align:center;"} 
             [:h2 {:style "text-align:center;color:#3452A5;font-size:18px;font-weight:400;font-family: 'Noto Sans', sans-serif;margin:0;padding:10px 0 5px 0"}
              (str "Hello " name "!")]]]
           [:tr 
            [:td {:width "100%"
                  :valign "center"
                  :style "text-align:center;"} 
             [:h2 {:style "text-align:center;color:#3452A5;font-size:18px;font-weight:400;font-family: 'Noto Sans', sans-serif;padding:2px 0 5px 0;margin:0;"}
              "Welcome to Blue Brick School,"]]]
           [:tr 
            [:td {:valign "top", :height "20"}]]
           [:tr 
            [:td {:width "100%"
                  :valign "center"
                  :style "text-align:center;"} 
             [:h2 {:style "text-align:center;color:#3452A5;font-size:18px;font-weight:400;font-family: 'Noto Sans', sans-serif;margin:0;padding:10px 0 0px 0;"}
              "Please click the button below to verify"]]] 
           [:tr 
            [:td {:width "100%"
                  :valign "center"
                  :style "text-align:center;"} 
             [:h2 {:style "text-align:center;color:#3452A5;font-weight:700;font-size:18px;font-family: 'Noto Sans', sans-serif;margin:0;padding:2px 0 5px 0"}
              email]]]
           [:tr 
            [:td {:valign "top"
                  :height "30"}]]
           [:tr 
            [:td {:width "100%"
                  :valign "center"
                  :style "text-align:center;"} 
             [:a {:href (verify-code-link id code)
                  :style "background:#F5A36C;font-size:13px;font-weight:600;color:#fff;border-radius:30px;margin:0 auto;padding:16px 80px;border:none;outline:none;font-family: 'Noto Sans', sans-serif;text-decoration:none;"}
              "VERIFY EMAIL"]]] 
           [:tr 
            [:td {:valign "top"
                  :height "50"}]]]]
         [:table {:width "600"
                  :cellspacing "0"
                  :cellpadding "0"
                  :border "0"
                  :style "border-collapse:collapse;background:#e9f5ff url(https://bluebrickschool.org/wp-content/themes/blue-bricks-school/assets/images/temp-bg-1.png) center no-repeat;background-size:100% 100%;padding:40px 0;"} 
          [:tbody 
           [:tr 
            [:td {:width "100%"
                  :valign "center"
                  :style "padding:60px 80px 10px;"} 
             [:p {:style "text-align:left;color:#3452A5;font-size:16px;font-weight:400;font-family: 'Noto Sans', sans-serif;margin:0;padding:2px 0 5px 0;line-height:26px;"} 
              [:b "Problems verifying?"] "Copy this link to your browser and you should be all set:"]]]
           [:tr 
            [:td {:width "100%"
                  :valign "center"
                  :style "padding:0 80px 60px;"} 
             [:p {:style "text-align:left;color:#3452A5;font-size:16px;font-weight:400;font-family: 'Noto Sans', sans-serif;margin:0;padding:2px 0 5px 0"}
              "generated email verify link written out:" 
              [:br] (verify-code-link id code)]]]]]
         [:table {:width "600"
                  :cellspacing "0"
                  :cellpadding "0"
                  :border "0"
                  :style "border-collapse:collapse;background:#e9f5ff"} 
          [:tbody 
           [:tr 
            [:td {:valign "top"
                  :height "40"}]]
           [:tr 
            [:td {:width "100%"
                  :valign "center"
                  :style "padding:0 80px;"} 
             [:p {:style "text-align:center;color:#3452A5;font-size:18px;font-weight:400;font-family: 'Noto Sans', sans-serif;margin:0;padding:2px 0 5px 0;line-height:34px;"}
              "After your email is verified you’ll receive another email going over next steps to start playing! You’re able to log in and start on your own right away as well."]]] 
           [:tr 
            [:td {:valign "top"
                  :height "40"}]]]]
         [:table {:width "600"
                  :cellspacing "0"
                  :cellpadding "0"
                  :border "0"
                  :style "border-collapse:collapse;background:#80BFE5;"} 
          [:tbody 
           [:tr 
            [:td {:valign "top"
                  :height "30"}]]
           [:tr 
            [:td {:width "100%"
                  :valign "center"
                  :style "text-align:center;"} 
             [:h2 {:style "text-align:center;color:#3452A5;font-size:14px;font-weight:700;font-family: 'Noto Sans', sans-serif;margin:0;padding:10px 0 0px 0"}
              "Blue Brick School"]]] 
           [:tr 
            [:td {:width "100%"
                  :valign "center"
                  :style "padding:0 70px;"} 
             [:p {:style "text-align:center;color:#3452A5;font-size:14px;font-weight:400;font-family: 'Noto Sans', sans-serif;margin:0;padding:2px 0 5px 0;line-height:30px;"}
              "18800 Von Karman Ave., Suite A, Irvine, CA, 92612"]]] 
           [:tr 
            [:td {:valign "top"
                  :height "20"}]]
           [:tr 
            [:td {:width "100%"
                  :valign "center"
                  :style "padding:0 70px;"} 
             [:p {:style "text-align:center;color:#3452A5;font-size:12px;font-weight:400;font-family: 'Noto Sans', sans-serif;margin:0;padding:2px 0 5px 0;line-height:30px;"}
              "Want to unsubscribe?" 
              [:a {:href "#", :style "font-weight:700;color:#3452A5;text-decoration:none;"} 
               [:b "Click here."]
               ]]
             ]] 
           [:tr 
            [:td {:valign "top", :height "20"}]]]]]]]]]]])
