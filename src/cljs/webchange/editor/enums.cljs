(ns webchange.editor.enums)

(def actions-with-selected-actions
  [{:key :to-sequence :value :to-sequence :text "Combine to sequence"}
   {:key :to-parallel :value :to-parallel :text "Combine to parallel"}
   {:key :to-sequence-data :value :to-sequence-data :text "Convert to sequence"}
   {:key :to-parallel-data :value :to-parallel-data :text "Convert to parallel"}])

(def asset-types
  [{:key :image :value :image :text "Image"}
   {:key :audio :value :audio :text "Audio"}
   {:key :anim-text :value :anim-text :text "Animation Text"}
   {:key :anim-texture :value :anim-texture :text "Animation Texture"}])

(def object-types
  [{:key :image :value :image :text "Image"}
   {:key :transparent :value :transparent :text "Transparent"}
   {:key :group :value :group :text "Group"}
   {:key :placeholder :value :placeholder :text "Placeholder"}
   {:key :animation :value :animation :text "Animation"}
   {:key :text :value :text :text "Text"}
   {:key :background :value :background :text "Background"}
   {:key :painting-area :value :painting-area :text "Painting Area"}])
