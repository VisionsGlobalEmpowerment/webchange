(ns webchange.editor.common.action-form.action-form-test-snapshots)

(def dispatch-animation-sequence-snapshot
  (str
    "<div>\n"
    "  <webchange.editor.common.actions.action_form.common argv={{...}} />\n"
    "  <a />\n"
    "  <webchange.editor.common.actions.action_forms.animation_sequence.animation_sequence_panel argv={{...}} />\n"
    "</div>"
    ))

(def main-action-form-general-snapshot
  (str
    "<div>\n"
    "  <sodium.core.menu argv={{...}} />\n"
    "  <webchange.editor.common.actions.action_form.dispatch argv={{...}} />\n"
    "</div>"
    ))
