(ns webchange.editor.common.action-form.animation-sequence-panel-snapshots)

(def animation-sequence-panel-snapshot
  (str
    "<div>\n"
    "  <a label=\"Target\" placeholder=\"Target\" search={true} selection={true} inline={true} options={{...}} defaultValue=\"mari\" onChange={[Function]} as={[Function: a]} control={[Function: t]} />\n"
    "  <sodium.core.form_input argv={{...}} />\n"
    "  <sodium.core.form_input argv={{...}} />\n"
    "  <sodium.core.divider argv={{...}} />\n"
    "  <a>\n"
    "    <webchange.editor.form_elements.audio_asset_dropdown argv={{...}} />\n"
    "    <sodium.core.button argv={{...}} />\n"
    "  </a>\n"
    "  <sodium.core.form_input argv={{...}} />\n"
    "  <sodium.core.form_input argv={{...}} />\n"
    "  <webchange.editor.form_elements.wavesurfer.animation_sequence_waveform_modal argv={{...}} />\n"
    "  <sodium.core.divider argv={{...}} />\n"
    "  <webchange.editor.common.actions.action_forms.animation_sequence.animation_sequence_items argv={{...}} />\n"
    "  <sodium.core.divider argv={{...}} />\n"
    "</div>"
    ))

(def animation-sequence-items-snapshot
  (str
    "<div>\n"
    "  <a>\n"
    "    <a>\n"
    "      <a>\n"
    "        <sodium.core.header argv={{...}} />\n"
    "        <div style={{...}}>\n"
    "          <sodium.core.icon argv={{...}} />\n"
    "        </div>\n"
    "      </a>\n"
    "    </a>\n"
    "    <webchange.editor.common.actions.action_forms.animation_sequence.animation_sequence_item argv={{...}} />\n"
    "    <webchange.editor.common.actions.action_forms.animation_sequence.animation_sequence_item argv={{...}} />\n"
    "  </a>\n"
    "</div>"
    ))

(def animation-sequence-item-snapshot
  (str
    "<webchange.editor.common.actions.action_forms.animation_sequence.animation_sequence_item argv={{...}}>\n"
    "  <reagent1 argv={{...}}>\n"
    "    <a>\n"
    "      <div className=\"content\">\n"
    "        <a>\n"
    "          start: 14.552 end: 16.639 anim: talk\n"
    "        </a>\n"
    "        <div style={{...}}>\n"
    "          <sodium.core.icon argv={{...}}>\n"
    "            <t name=\"edit\" link={true} onClick={[Function]} as=\"i\">\n"
    "              <i onClick={[Function]} aria-hidden=\"true\" className=\"edit link icon\" />\n"
    "            </t>\n"
    "          </sodium.core.icon>\n"
    "          <sodium.core.icon argv={{...}}>\n"
    "            <t name=\"remove\" link={true} onClick={[undefined]} as=\"i\">\n"
    "              <i onClick={[undefined]} aria-hidden=\"true\" className=\"remove link icon\" />\n"
    "            </t>\n"
    "          </sodium.core.icon>\n"
    "        </div>\n"
    "      </div>\n"
    "    </a>\n"
    "  </reagent1>\n"
    "</webchange.editor.common.actions.action_forms.animation_sequence.animation_sequence_item>"
    ))
