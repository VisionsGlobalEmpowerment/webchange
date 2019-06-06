(ns webchange.dashboard.students.views)

;(defn add-student-form
;  []
;  (let [data (r/atom {})]
;    (fn []
;      (let [loading @(re-frame/subscribe [:loading])]
;        [na/segment {:loading? (when (:add-student loading))}
;         [na/header {:as "h4" :content "Add student"}]
;         [na/divider {:clearing? true}]
;         [na/form {}
;          [na/form-input {:fluid? true :placeholder "First Name"
;                          :on-change #(swap! data assoc :first-name (-> %2 .-value))}]
;          [na/form-input {:fluid? true :placeholder "Last Name"
;                          :on-change #(swap! data assoc :last-name (-> %2 .-value))}]
;          [na/form-input {:fluid? true :placeholder "E-mail address"
;                          :on-change #(swap! data assoc :email (-> %2 .-value))}]
;          [na/form-input {:fluid? true :placeholder "Password" :type "password"
;                          :on-change #(swap! data assoc :password (-> %2 .-value))}]
;          [na/form-input {:fluid? true :placeholder "Confirm Password" :type "password"
;                          :on-change #(swap! data assoc :confirm-password (-> %2 .-value))}]
;          [na/divider {}]
;          [na/form-button {:content "Save" :on-click #(re-frame/dispatch [::de/add-student @data])}]
;          ]]))))
;
;(defn option-from-class [class]
;  {:key (:id class)
;   :value (:id class)
;   :text (:name class)})
;
;(defn options-from-classes [classes]
;  (map #(option-from-class %) classes))
;
;(defn edit-student-form
;  []
;  (let [classes @(re-frame/subscribe [::ds/classes])
;        {id :id class-id :class-id {first-name :first-name last-name :last-name} :user} @(re-frame/subscribe [::ds/current-student])
;        data (r/atom {:class-id class-id})]
;    (fn []
;      (let [loading @(re-frame/subscribe [:loading])]
;        [na/segment {:loading? (when (:edit-class loading))}
;         [na/header {:as "h4" :content (str "Edit student " first-name " " last-name)}]
;         [na/divider {:clearing? true}]
;         [na/form {}
;          [sa/Dropdown {:placeholder "Class" :search true :selection true :options (options-from-classes classes)
;                        :default-value class-id :on-change #(swap! data assoc :class-id (.-value %2))}]
;          [na/divider {}]
;          [na/form-button {:content "Save" :on-click #(re-frame/dispatch [::de/edit-student id @data])}]
;          ]]))))
;
;(defn manage-students
;  []
;  (let [loading @(re-frame/subscribe [:loading])
;        class-id @(re-frame/subscribe [::ds/current-class-id])
;        students @(re-frame/subscribe [::ds/class-students class-id])]
;    [na/segment {:loading? (when (:classes loading))}
;     [na/header {:as "h4"}
;      "Students"
;      [:div {:style {:float "right"}}
;       [na/icon {:name "add" :link? true
;                 :on-click #(re-frame/dispatch [::de/set-main-content :add-student-form])}]]]
;     [na/divider {:clearing? true}]
;     [sa/ItemGroup {}
;      (for [{id :id {first-name :first-name last-name :last-name} :user} students]
;        ^{:key (str id)}
;        [sa/Item {}
;         [sa/ItemContent {}
;          [:div {:style {:float "left"}} [:p (str first-name " " last-name)]]
;          [:div {:style {:float "right"}}
;           [na/icon {:name "edit" :link? true
;                     :on-click #(re-frame/dispatch [::de/show-edit-student-form id])}]
;           [na/icon {:name "remove" :link? true
;                     :on-click #(re-frame/dispatch [::de/delete-student id])}]]
;          ]])]]))
